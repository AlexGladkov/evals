package com.dev.nicehash.domain

import com.dev.nicehash.data.providers.models.ApiProviderCurrent
import com.dev.nicehash.data.providers.models.ApiProviderData
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.providers.models.ApiProviderPast
import com.dev.nicehash.data.providers.models.ApiProviderPastData
import com.dev.nicehash.domain.models.Income
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [IncomeCalculator] (task C5 extraction, adapted to the task/c2
 * bug fixes per the integration rule "c5 architecture + c2 math"):
 *  - empty data yields 0.0 in every btc field (empty-guard in MathUtils.getAverage);
 *  - month income uses the real number of days in the current month of nowMillis;
 *  - a past entry whose algorithm has zero current speed is skipped.
 *
 * Time is injected ([IncomeCalculator.calculate] `nowMillis`), so every test
 * runs against a fixed instant and is fully deterministic.
 */
class IncomeCalculatorTest {

    private companion object {
        /**
         * Fixed "current" time for all tests: 2023-11-14T22:13:20Z.
         */
        const val NOW = 1_700_000_000_000L
        const val HOUR_SECONDS = 3600L
        const val HOUR_MILLIS = 3600_000L
    }

    private val calculator = IncomeCalculator()

    // region helpers

    /**
     * 5-min api interval which is ~secondsAgo before [NOW]
     * (the api reports time in 5-min interval numbers).
     */
    private fun intervalFor(secondsAgo: Long): Long {
        return (NOW / 1000 - secondsAgo) / 300
    }

    private fun pastDataAt(interval: Long, a: Float): ApiProviderPastData {
        return ApiProviderPastData(time = interval, a = a, unpaid = 0.0)
    }

    private fun past(algo: Int, data: List<ApiProviderPastData>): ApiProviderPast {
        return ApiProviderPast(algo = algo, data = data)
    }

    private fun current(algo: Int, profitability: Double, speed: Float): ApiProviderCurrent {
        return ApiProviderCurrent(profitability = profitability, suffix = "H/s", name = "algo$algo",
                algo = algo, data = ApiProviderData(a = speed, rs = 0f, unpaid = 0.0))
    }

    private fun providerEx(current: List<ApiProviderCurrent>,
                           past: List<ApiProviderPast>): ApiProviderEx {
        return ApiProviderEx(method = "stats.provider.ex", current = current, past = past,
                payments = emptyList(), addr = "test-wallet-address")
    }

    /** Real days in the month containing the given instant (same API as production code). */
    private fun expectedMonthDays(nowMillis: Long): Int {
        return DateTime(nowMillis).dayOfMonth().getMaximumValue()
    }

    private fun assertAllZero(income: Income) {
        assertEquals("hourBtc should be 0.0", 0.0, income.hourBtc, 0.0)
        assertEquals("dayBtc should be 0.0", 0.0, income.dayBtc, 0.0)
        assertEquals("weekBtc should be 0.0", 0.0, income.weekBtc, 0.0)
        assertEquals("monthBtc should be 0.0", 0.0, income.monthBtc, 0.0)
    }

    // endregion

    @Test
    fun emptyDataProducesZeroInAllBtcFields() {
        val stats = providerEx(current = emptyList(), past = emptyList())

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        // Fixed in c2: getAverage of an empty collection is 0.0, not NaN
        assertAllZero(income)
        assertEquals(0.0, income.hourValue, 0.0)
        assertEquals(0.0, income.dayValue, 0.0)
        assertEquals(0.0, income.weekValue, 0.0)
        assertEquals(0.0, income.monthValue, 0.0)
    }

    @Test
    fun singleBucketComputesExactPeriodValues() {
        // one algo, single past bucket 1h ago with unchanged speed:
        // profitability = 2.0 * 10.0 = 20.0, multi = 10 / 10 = 1.0 -> value 20.0
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 10f)),
                past = listOf(past(algo = 0,
                        data = listOf(pastDataAt(intervalFor(HOUR_SECONDS), a = 10f)))))

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        assertEquals(20.0, income.dayBtc, 1e-9)
        assertEquals(20.0 / 24, income.hourBtc, 1e-9)
        assertEquals(20.0 * 7, income.weekBtc, 1e-9)
        // Fixed in c2: month uses the real number of days in the month of nowMillis
        assertEquals(20.0 * expectedMonthDays(NOW), income.monthBtc, 1e-9)
    }

    @Test
    fun multipleAlgorithmsAccumulateSameTimestampBuckets() {
        // two algos reporting the same 5-min interval:
        // algo0: profitability = 2.0 * 10.0 = 20.0, multi = 10/10 = 1.0 -> 20.0
        // algo1: profitability = 4.0 * 5.0 = 20.0, multi = 5/5 = 1.0 -> 20.0
        // same timestamp key -> accumulated into a single bucket of 40.0
        val interval = intervalFor(HOUR_SECONDS)
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 10f),
                        current(algo = 1, profitability = 4.0, speed = 5f)),
                past = listOf(past(algo = 0, data = listOf(pastDataAt(interval, a = 10f))),
                        past(algo = 1, data = listOf(pastDataAt(interval, a = 5f)))))

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        assertEquals(40.0, income.dayBtc, 1e-9)
        assertEquals(40.0 / 24, income.hourBtc, 1e-9)
    }

    @Test
    fun multipleAlgorithmsAndBucketsAverageOverAllBuckets() {
        // algo0: @1h a=10f -> 20.0; @2h a=5f -> 10.0
        // algo1: @90m a=5f -> 20.0; @23h a=2.5f -> 10.0
        // four distinct timestamps inside the 24h window -> average = 60/4 = 15.0
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 10f),
                        current(algo = 1, profitability = 4.0, speed = 5f)),
                past = listOf(
                        past(algo = 0, data = listOf(
                                pastDataAt(intervalFor(HOUR_SECONDS), a = 10f),
                                pastDataAt(intervalFor(2 * HOUR_SECONDS), a = 5f))),
                        past(algo = 1, data = listOf(
                                pastDataAt(intervalFor(3 * HOUR_SECONDS / 2), a = 5f),
                                pastDataAt(intervalFor(23 * HOUR_SECONDS), a = 2.5f)))))

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        assertEquals(15.0, income.dayBtc, 1e-9)
        assertEquals(15.0 * expectedMonthDays(NOW), income.monthBtc, 1e-9)
    }

    @Test
    fun fixedTimeParameterDeterministicallyDrivesWindowFiltering() {
        // single bucket ~23h old relative to NOW: inside the 24h window...
        val interval = intervalFor(23 * HOUR_SECONDS)
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 10f)),
                past = listOf(past(algo = 0, data = listOf(pastDataAt(interval, a = 10f)))))

        val inside = calculator.calculate(stats = stats, nowMillis = NOW)
        assertEquals(20.0, inside.dayBtc, 1e-9)

        // deterministic: same inputs, same nowMillis -> identical result
        assertEquals(inside, calculator.calculate(stats = stats, nowMillis = NOW))

        // ...but ~2 hours later the very same bucket is >24h old and filtered out,
        // which empties the window and yields 0.0 (no hidden clock reads; fixed in c2)
        val later = NOW + 2 * HOUR_MILLIS
        val outside = calculator.calculate(stats = stats, nowMillis = later)
        assertAllZero(outside)
    }

    @Test
    fun oldBucketOutsideDayWindowIsExcluded() {
        // bucket ~25h old (relative to NOW) is filtered out of the 24h window;
        // fresh bucket inside the window is the only contributor
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 10f)),
                past = listOf(past(algo = 0, data = listOf(
                        pastDataAt(intervalFor(25 * HOUR_SECONDS), a = 10f),
                        pastDataAt(intervalFor(HOUR_SECONDS), a = 10f)))))

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        assertEquals(20.0, income.dayBtc, 1e-9)
    }

    @Test
    fun zeroCurrentSpeedSkipsBucketAndYieldsZero() {
        // Fixed in c2: current speed 0.0 skips the bucket instead of dividing by
        // zero, so the window stays empty and every period is 0.0
        val stats = providerEx(
                current = listOf(current(algo = 0, profitability = 2.0, speed = 0f)),
                past = listOf(past(algo = 0,
                        data = listOf(pastDataAt(intervalFor(HOUR_SECONDS), a = 10f)))))

        val income = calculator.calculate(stats = stats, nowMillis = NOW)

        assertAllZero(income)
    }
}
