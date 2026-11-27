package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.FakeNicehashProvider
import com.dev.nicehash.data.providers.models.ApiProviderCurrent
import com.dev.nicehash.data.providers.models.ApiProviderData
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.providers.models.ApiProviderPast
import com.dev.nicehash.data.providers.models.ApiProviderPastData
import com.dev.nicehash.domain.IncomeCalculator
import com.dev.nicehash.domain.models.Income
import com.google.gson.Gson
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test

/**
 * Unit tests for MoneyRepositoryImpl income calculation (task C2):
 * no NaN or Infinity on empty data or zero speed, and real month days.
 */
class MoneyRepositoryImplTest {

    /**
     * Integration note: since c5 the repository delegates math to IncomeCalculator,
     * and since a5 it carries a Room cache (not exercised by these math tests —
     * a plain fake DAO is injected so the cache writes succeed silently).
     */
    private fun buildRepository(provider: com.dev.nicehash.data.providers.FakeNicehashProvider): MoneyRepositoryImpl {
        return MoneyRepositoryImpl(nicehashProvider = provider, incomeCalculator = IncomeCalculator(),
                cachedStatsDao = FakeCachedStatsDao(), gson = Gson())
    }

    private companion object {
        const val WALLET = "test-wallet"

        /**
         * 5-min api interval which is minusMinutes ago (inside the last day)
         */
        @JvmStatic
        private fun intervalFor(minusMinutes: Long): Long {
            return (DateTime().millis / 1000 - minusMinutes * 60) / 300
        }

        /**
         * Past (historical) stats tuple from api
         */
        @JvmStatic
        private fun pastDataAt(interval: Long, a: Float): ApiProviderPastData {
            return ApiProviderPastData(time = interval, a = a, unpaid = 0.0)
        }

        /**
         * Current (latest) stats tuple for one algorithm
         */
        @JvmStatic
        private fun current(algo: Int, profitability: Double, speed: Float, unpaid: Double = 0.0): ApiProviderCurrent {
            return ApiProviderCurrent(profitability = profitability, suffix = "H/s", name = "algo" + algo,
                    algo = algo, data = ApiProviderData(a = speed, rs = 0f, unpaid = unpaid))
        }

        @JvmStatic
        private fun providerEx(current: List<ApiProviderCurrent>, past: List<ApiProviderPast>): ApiProviderEx {
            return ApiProviderEx(method = "stats.provider.ex", current = current, past = past,
                    payments = emptyList(), addr = WALLET)
        }
    }

    /**
     * Bug 1: empty current and past must produce all-zero Income, not NaN
     */
    @Test
    fun incomeIsAllZerosOnEmptyData() {
        val repository = buildRepository(FakeNicehashProvider(providerEx(current = emptyList(),
                past = emptyList())))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertAllFinite(income)
        assertEquals(0.0, income.hourBtc, 0.0)
        assertEquals(0.0, income.dayBtc, 0.0)
        assertEquals(0.0, income.weekBtc, 0.0)
        assertEquals(0.0, income.monthBtc, 0.0)
    }

    /**
     * Bug 2: zero current speed must not produce NaN or Infinity buckets
     */
    @Test
    fun incomeHasNoNaNOrInfinityWhenCurrentSpeedIsZero() {
        // algo 30 has speed 0 - its past bucket would be Infinity/NaN before the fix and must be skipped now
        // algo 40 has no current entry at all - same skip path
        // algo 20 is healthy: a=5.0 with speed 10.0 and price 100.0 -> bucket 500.0
        val interval30 = intervalFor(minusMinutes = 10)
        val interval20 = intervalFor(minusMinutes = 20)
        val repository = buildRepository(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 30, profitability = 100.0, speed = 0.0f),
                        current(algo = 20, profitability = 100.0, speed = 10.0f)),
                past = listOf(ApiProviderPast(algo = 30,
                        data = listOf(pastDataAt(interval = interval30, a = 10.0f))),
                        ApiProviderPast(algo = 40,
                                data = listOf(pastDataAt(interval = interval30, a = 7.0f))),
                        ApiProviderPast(algo = 20,
                                data = listOf(pastDataAt(interval = interval20, a = 5.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertAllFinite(income)
        // only the healthy algo 20 bucket survives, average over one bucket = 500.0
        assertEquals(500.0, income.dayBtc, 0.001)
        assertEquals(500.0 / 24, income.hourBtc, 0.001)
        assertEquals(500.0 * 7, income.weekBtc, 0.001)
        assertEquals(500.0 * DateTime().dayOfMonth().getMaximumValue(), income.monthBtc, 0.001)
    }

    /**
     * Bug 3: monthBtc must use the real number of days in the current month
     */
    @Test
    fun monthBtcUsesRealNumberOfDaysInCurrentMonth() {
        val monthDaysBefore = DateTime().dayOfMonth().getMaximumValue()
        val repository = buildRepository(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 20, profitability = 100.0, speed = 10.0f)),
                past = listOf(ApiProviderPast(algo = 20,
                        data = listOf(pastDataAt(interval = intervalFor(minusMinutes = 10), a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()
        val monthDaysAfter = DateTime().dayOfMonth().getMaximumValue()

        // guard against a test running exactly across a month boundary
        assumeTrue(monthDaysBefore == monthDaysAfter)

        assertEquals(1000.0, income.dayBtc, 0.001)
        assertEquals(income.dayBtc * monthDaysAfter, income.monthBtc, 0.001)
    }

    /**
     * Regression: normal (non-empty, non-zero speed) data keeps the old values except the month fix
     */
    @Test
    fun normalDataProducesCorrectIncome() {
        val interval1 = intervalFor(minusMinutes = 10)
        val interval2 = intervalFor(minusMinutes = 20)
        // algo 20 at interval1: a=5.0, speed=10.0, price=100.0 -> 0.5 * 1000.0 = 500.0
        // algo 21 at interval1: a=10.0, speed=10.0, price=50.0 -> 1.0 * 500.0 = 500.0 (accumulates to 1000.0)
        // algo 20 at interval2: a=15.0 -> 1.5 * 1000.0 = 1500.0
        val repository = buildRepository(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 20, profitability = 100.0, speed = 10.0f),
                        current(algo = 21, profitability = 50.0, speed = 10.0f)),
                past = listOf(ApiProviderPast(algo = 20,
                        data = listOf(pastDataAt(interval = interval1, a = 5.0f),
                                pastDataAt(interval = interval2, a = 15.0f))),
                        ApiProviderPast(algo = 21,
                                data = listOf(pastDataAt(interval = interval1, a = 10.0f)))))))

        val income = repository.fetchMinerIncome(miner = WALLET).blockingGet()

        assertAllFinite(income)
        assertEquals(1250.0, income.dayBtc, 0.001)
        assertEquals(1250.0 / 24, income.hourBtc, 0.001)
        assertEquals(1250.0 * 7, income.weekBtc, 0.001)
        assertEquals(1250.0 * DateTime().dayOfMonth().getMaximumValue(), income.monthBtc, 0.001)
    }

    /**
     * fetchUnpaidMoney: sum of unpaid across all algorithms
     */
    @Test
    fun unpaidMoneyIsSumAcrossAlgorithms() {
        val repository = buildRepository(FakeNicehashProvider(providerEx(
                current = listOf(current(algo = 0, profitability = 1.0, speed = 1.0f, unpaid = 1.5),
                        current(algo = 1, profitability = 2.0, speed = 1.0f, unpaid = 2.25)),
                past = emptyList())))

        assertEquals(3.75, repository.fetchUnpaidMoney(miner = WALLET).blockingGet(), 0.000001)
    }

    /**
     * No NaN and no Infinity in any Income field
     */
    private fun assertAllFinite(income: Income) {
        for (value in listOf(income.hourBtc, income.dayBtc, income.weekBtc, income.monthBtc)) {
            assertFalse(value.isNaN())
            assertFalse(value.isInfinite())
            assertTrue(value >= 0.0)
        }
    }
}
