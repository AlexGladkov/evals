package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.FakeNicehashProvider
import com.dev.nicehash.data.providers.models.ApiProviderCurrent
import com.dev.nicehash.data.providers.models.ApiProviderData
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.providers.models.ApiProviderPast
import com.dev.nicehash.data.providers.models.ApiProviderPastData
import com.dev.nicehash.domain.models.Income
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Ignore
import org.junit.Test

/**
 * Unit tests for the legacy income calculation in [MoneyRepositoryImpl] (task C4).
 *
 * The tests are written against the CURRENT implementation and pin down its behavior.
 * Known bugs found while writing these tests are documented as @Ignore tests that
 * describe the behavior the code SHOULD have:
 *  - empty api data poisons every Income field with NaN (getAverage of an empty collection);
 *  - a past bucket whose algorithm has no (or zero) current speed yields
 *    speed / 0.0 = Infinity and Infinity * 0.0 = NaN, which poisons the average too;
 *  - monthBtc is multiplied by a hardcoded 31 instead of the real number of days in month.
 */
class MoneyRepositoryImplTest {

    private companion object {
        const val WALLET = "test-wallet-address"
    }

    // region helpers

    /**
     * 5-min api interval which is minusMinutes ago (safely inside the last day)
     */
    private fun intervalFor(minusMinutes: Long): Long {
        return (DateTime().millis / 1000 - minusMinutes * 60) / 300
    }

    /**
     * 5-min api interval which is minusDays ago (outside the last day window)
     */
    private fun oldIntervalFor(minusDays: Long): Long {
        return (DateTime().millis / 1000 - minusDays * 24 * 60 * 60) / 300
    }

    private fun pastDataAt(interval: Long, a: Float): ApiProviderPastData {
        return ApiProviderPastData(time = interval, a = a, unpaid = 0.0)
    }

    private fun past(algo: Int, data: List<ApiProviderPastData>): ApiProviderPast {
        return ApiProviderPast(algo = algo, data = data)
    }

    private fun current(algo: Int, profitability: Double, speed: Float,
                        unpaid: Double = 0.0): ApiProviderCurrent {
        return ApiProviderCurrent(profitability = profitability, suffix = "H/s", name = "algo$algo",
                algo = algo, data = ApiProviderData(a = speed, rs = 0f, unpaid = unpaid))
    }

    private fun providerEx(current: List<ApiProviderCurrent>,
                           past: List<ApiProviderPast>): ApiProviderEx {
        return ApiProviderEx(method = "stats.provider.ex", current = current, past = past,
                payments = emptyList(), addr = WALLET)
    }

    private fun assertAllNaN(income: Income) {
        assertTrue("hourBtc should be NaN", income.hourBtc.isNaN())
        assertTrue("dayBtc should be NaN", income.dayBtc.isNaN())
        assertTrue("weekBtc should be NaN", income.weekBtc.isNaN())
        assertTrue("monthBtc should be NaN", income.monthBtc.isNaN())
    }

    // endregion

    // region fetchMinerIncome: normal data / period calculation

    /**
     * Single healthy algorithm: one past bucket a=10.0 at current speed 10.0 with
     * profitability 100.0 -> bucket = (10.0 / 10.0) * (100.0 * 10.0) = 1000.0.
     * Periods: hour = avg / 24, day = avg, week = avg * 7, month = avg * 31.
     */
    @Test
    fun normalSingleAlgoCalculatesAllPeriods() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 20, profitability = 100.0, speed = 10.0f)),
                past = listOf(past(algo = 20,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(1000.0, income.dayBtc, 1e-6)
        assertEquals(1000.0 / 24, income.hourBtc, 1e-6)
        assertEquals(1000.0 * 7, income.weekBtc, 1e-6)
        // month is multiplied by a hardcoded 31 on the current code
        assertEquals(1000.0 * 31, income.monthBtc, 1e-6)
    }

    /**
     * Past speed differs from current speed: a=2.0 at current speed 4.0 with
     * profitability 50.0 -> bucket = (2.0 / 4.0) * (50.0 * 4.0) = 100.0.
     */
    @Test
    fun speedIsRecalculatedWithSpeedRatio() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 50.0, speed = 4.0f)),
                past = listOf(past(algo = 0,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 2.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(100.0, income.dayBtc, 1e-6)
        assertEquals(100.0 / 24, income.hourBtc, 1e-6)
        assertEquals(100.0 * 7, income.weekBtc, 1e-6)
        assertEquals(100.0 * 31, income.monthBtc, 1e-6)
    }

    /**
     * Buckets of different algorithms at the same timestamp are summed up
     * (aggregation across algorithms): 500.0 + 500.0 = 1000.0 in a single bucket.
     */
    @Test
    fun bucketsFromMultipleAlgorithmsAggregateAtSameTimestamp() {
        val interval = intervalFor(minusMinutes = 10)
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 100.0, speed = 10.0f),
                        current(algo = 1, profitability = 50.0, speed = 10.0f)),
                past = listOf(past(algo = 0, data = listOf(pastDataAt(interval = interval, a = 5.0f))),
                        past(algo = 1, data = listOf(pastDataAt(interval = interval, a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        // algo 0: (5.0 / 10.0) * (100.0 * 10.0) = 500.0; algo 1: (10.0 / 10.0) * (50.0 * 10.0) = 500.0
        assertEquals(1000.0, income.dayBtc, 1e-6)
        assertEquals(1000.0 / 24, income.hourBtc, 1e-6)
        assertEquals(1000.0 * 7, income.weekBtc, 1e-6)
        assertEquals(1000.0 * 31, income.monthBtc, 1e-6)
    }

    /**
     * Two past points of the SAME algorithm with the same time value merge into one
     * chart bucket: 1000.0 + 500.0 = 1500.0 (single bucket -> also the average).
     */
    @Test
    fun sameTimestampsAccumulateIntoOneBucket() {
        val interval = intervalFor(minusMinutes = 10)
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 100.0, speed = 10.0f)),
                past = listOf(past(algo = 0, data = listOf(pastDataAt(interval = interval, a = 10.0f),
                        pastDataAt(interval = interval, a = 5.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(1500.0, income.dayBtc, 1e-6)
    }

    /**
     * Several buckets in the last day: the average over buckets drives all periods.
     * Buckets 10.0 and 20.0 -> avg = 15.0.
     */
    @Test
    fun multipleBucketsAreAveraged() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 10.0, speed = 1.0f)),
                past = listOf(past(algo = 0, data = listOf(
                        pastDataAt(interval = intervalFor(minusMinutes = 10), a = 1.0f),
                        pastDataAt(interval = intervalFor(minusMinutes = 20), a = 2.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(15.0, income.dayBtc, 1e-6)
        assertEquals(15.0 / 24, income.hourBtc, 1e-6)
        assertEquals(15.0 * 7, income.weekBtc, 1e-6)
        assertEquals(15.0 * 31, income.monthBtc, 1e-6)
    }

    /**
     * Buckets older than 24h are filtered out of the average: the old 30.0 bucket
     * must not be counted, only the fresh 10.0 one.
     */
    @Test
    fun bucketsOlderThanDayAreExcluded() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 10.0, speed = 1.0f)),
                past = listOf(past(algo = 0, data = listOf(
                        pastDataAt(interval = intervalFor(minusMinutes = 10), a = 1.0f),
                        pastDataAt(interval = oldIntervalFor(minusDays = 3), a = 3.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(10.0, income.dayBtc, 1e-6)
        assertEquals(10.0 / 24, income.hourBtc, 1e-6)
    }

    /**
     * The repository must ask for provider stats of the given miner starting ~25h back.
     */
    @Test
    fun incomeRequestsStatsOfTheMinerForLast25Hours() {
        val provider = FakeNicehashProvider(providerEx(current = emptyList(), past = emptyList()))
        val repository = MoneyRepositoryImpl(provider)

        val before = DateTime().minusHours(25).millis / 1000
        repository.fetchMinerIncome(miner = WALLET).blockingGet()
        val after = DateTime().minusHours(25).millis / 1000

        assertEquals(WALLET, provider.lastMiner)
        assertTrue("from should be within the captured 25h-ago range",
                provider.lastFrom in before..after)
    }

    // endregion

    // region fetchMinerIncome: known bugs (characterization + @Ignore)

    /**
     * Characterization test: CURRENT behavior for empty api data. An empty chart makes
     * getAverage divide 0.0 / 0 and every Income field comes out as NaN.
     * See [emptyDataShouldProduceZeroIncome] for the expected behavior.
     */
    @Test
    fun emptyDataCurrentBehaviorIsNaN() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = emptyList(), past = emptyList())))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertAllNaN(income)
    }

    /**
     * BUG: with no api data the repository returns NaN in every field instead of 0.0,
     * because MathUtils.getAverage of an empty collection is 0.0 / 0 = NaN.
     * Expected: an empty day chart means no income, i.e. all-zero values.
     */
    @Ignore("Known bug: empty data produces NaN income (getAverage of empty collection)")
    @Test
    fun emptyDataShouldProduceZeroIncome() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = emptyList(), past = emptyList())))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertEquals(0.0, income.hourBtc, 0.0)
        assertEquals(0.0, income.dayBtc, 0.0)
        assertEquals(0.0, income.weekBtc, 0.0)
        assertEquals(0.0, income.monthBtc, 0.0)
    }

    /**
     * Characterization test: a past bucket for an algorithm which is missing from
     * `current` (or has current speed 0.0) computes multi = a / 0.0 = Infinity and
     * pastProfitability = Infinity * 0.0 = NaN, so the whole income becomes NaN.
     * See [zeroOrMissingCurrentSpeedShouldNotPoisonIncome] for the expected behavior.
     */
    @Test
    fun missingCurrentAlgoCurrentBehaviorIsNaN() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = emptyList(),
                past = listOf(past(algo = 99,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertAllNaN(income)
    }

    /**
     * BUG: a past bucket whose algorithm has zero (or absent) current speed yields
     * multi = speed / 0.0 = Infinity and pastProfitability = Infinity * 0.0 = NaN.
     * One such NaN bucket poisons the chart average, so even healthy algorithms end
     * up with NaN income. Expected: such buckets are skipped and the healthy
     * algorithm's bucket (500.0) survives.
     */
    @Ignore("Known bug: zero/absent current speed turns a bucket into Infinity/NaN and poisons the average")
    @Test
    fun zeroOrMissingCurrentSpeedShouldNotPoisonIncome() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                // algo 30: current speed 0.0 -> its bucket becomes NaN; algo 40: no current entry at all
                current = listOf(current(algo = 30, profitability = 100.0, speed = 0.0f),
                        current(algo = 20, profitability = 100.0, speed = 10.0f)),
                past = listOf(past(algo = 30,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 10.0f))),
                        past(algo = 40,
                                data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 15), a = 7.0f))),
                        past(algo = 20,
                                data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 20), a = 5.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        // only the healthy algo 20 bucket (5.0 / 10.0 * 100.0 * 10.0 = 500.0) should count
        assertFalse(income.dayBtc.isNaN())
        assertEquals(500.0, income.dayBtc, 1e-6)
        assertEquals(500.0 / 24, income.hourBtc, 1e-6)
        assertEquals(500.0 * 7, income.weekBtc, 1e-6)
        assertEquals(500.0 * 31, income.monthBtc, 1e-6)
    }

    /**
     * BUG: monthBtc is multiplied by a hardcoded 31 although a month has 28..31 days,
     * so month income is overestimated in every month but the 31-day ones.
     * Expected: multiply by the real number of days of the current month.
     */
    @Ignore("Known bug: monthBtc uses a hardcoded 31 instead of the real days in month")
    @Test
    fun monthBtcShouldUseRealNumberOfDaysInMonth() {
        val monthDaysBefore = DateTime().dayOfMonth().getMaximumValue()
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 20, profitability = 100.0, speed = 10.0f)),
                past = listOf(past(algo = 20,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()
        val monthDaysAfter = DateTime().dayOfMonth().getMaximumValue()

        // guard against a run exactly across a month boundary
        assumeTrue(monthDaysBefore == monthDaysAfter)

        assertEquals(1000.0, income.dayBtc, 1e-6)
        assertEquals(income.dayBtc * monthDaysAfter, income.monthBtc, 1e-6)
    }

    // endregion

    // region fetchUnpaidMoney

    /**
     * Unpaid balance is the sum of `unpaid` across all algorithms in `current`.
     */
    @Test
    fun unpaidIsSumAcrossAlgorithms() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 1.0, speed = 1.0f, unpaid = 1.5),
                        current(algo = 1, profitability = 2.0, speed = 1.0f, unpaid = 2.25),
                        current(algo = 2, profitability = 3.0, speed = 1.0f, unpaid = 100.0)),
                past = emptyList())))

        assertEquals(103.75, repository.fetchUnpaidMoney(miner = WALLET).blockingGet(), 1e-9)
    }

    /**
     * A single algorithm: the sum is just its own unpaid value.
     */
    @Test
    fun unpaidWithSingleAlgorithm() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 1.0, speed = 1.0f, unpaid = 4.2)),
                past = emptyList())))

        assertEquals(4.2, repository.fetchUnpaidMoney(miner = WALLET).blockingGet(), 1e-9)
    }

    /**
     * No current data at all -> no unpaid balance -> 0.0 (sum of an empty list).
     */
    @Test
    fun unpaidWithEmptyDataIsZero() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = emptyList(), past = emptyList())))

        assertEquals(0.0, repository.fetchUnpaidMoney(miner = WALLET).blockingGet(), 0.0)
    }

    /**
     * All-zero unpaid values sum up to 0.0.
     */
    @Test
    fun unpaidWithZeroBalancesIsZero() {
        val repository = MoneyRepositoryImpl(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 1.0, speed = 1.0f, unpaid = 0.0),
                        current(algo = 1, profitability = 2.0, speed = 1.0f, unpaid = 0.0)),
                past = emptyList())))

        assertEquals(0.0, repository.fetchUnpaidMoney(miner = WALLET).blockingGet(), 0.0)
    }

    /**
     * The unpaid request must query stats from the very beginning (from = 0).
     */
    @Test
    fun unpaidRequestsStatsFromZero() {
        val provider = FakeNicehashProvider(providerEx(current = emptyList(), past = emptyList()))
        val repository = MoneyRepositoryImpl(provider)

        repository.fetchUnpaidMoney(miner = WALLET).blockingGet()

        assertEquals(WALLET, provider.lastMiner)
        assertEquals(0L, provider.lastFrom)
    }

    // endregion
}
