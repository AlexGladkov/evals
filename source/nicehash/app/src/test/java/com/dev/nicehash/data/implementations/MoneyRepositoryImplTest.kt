package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.models.ApiProviderCurrent
import com.dev.nicehash.data.providers.models.ApiProviderData
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.providers.models.ApiProviderPast
import com.dev.nicehash.data.providers.models.ApiProviderPastData
import com.dev.nicehash.data.room.models.CachedStatsEntity
import com.dev.nicehash.domain.models.Income
import com.google.gson.Gson
import io.reactivex.Single
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/**
 * Offline-first (cache-then-network) tests for MoneyRepositoryImpl.
 * Covers: online fetch+cache, offline with cache, offline without cache, recovery.
 * Plain JVM test with a fake provider + fake Room DAO.
 */
class MoneyRepositoryImplTest {

    private val gson = Gson()
    private val statsDao = FakeCachedStatsDao()
    private val provider = FakeNicehashProvider()
    private val repository = MoneyRepositoryImpl(nicehashProvider = provider,
            cachedStatsDao = statsDao, gson = gson)

    companion object {
        private const val MINER = "3Aqj4o27ixprba67GvBctDkXijxxpbD4hS"
        private const val DAY_INCOME = 5.0 // derived from fixture below (see expectedIncome)
    }

    /**
     * Fixture where current speed = 10, profitability = 0.25 and two past points with speed 20
     * (both within the last 24h). Income math: multi = 20/10 = 2, per-point = 2 * 0.25 * 10 = 5.
     */
    private fun statsFixture(unpaid: Double = 0.0025): ApiProviderEx {
        val nowSlot = DateTime().millis / 1000 / 300
        return ApiProviderEx(method = "stats.provider.ex",
                current = listOf(ApiProviderCurrent(profitability = 0.25, suffix = "PH/s",
                        name = "DaggerHashimoto", algo = 0,
                        data = ApiProviderData(a = 10f, rs = 0f, unpaid = unpaid))),
                past = listOf(ApiProviderPast(algo = 0, data = listOf(
                        ApiProviderPastData(time = nowSlot - 1, a = 20f, unpaid = 0.0),
                        ApiProviderPastData(time = nowSlot - 2, a = 20f, unpaid = 0.0)))),
                payments = emptyList(), addr = MINER)
    }

    private fun expectedIncome(cachedAt: Long? = null): Income =
            Income(hourValue = 0.0, hourBtc = DAY_INCOME / 24, dayValue = 0.0, dayBtc = DAY_INCOME,
                    weekValue = 0.0, weekBtc = DAY_INCOME * 7, monthValue = 0.0,
                    monthBtc = DAY_INCOME * 31, cachedAt = cachedAt)

    // MARK: - Online: data fetched and cached

    @Test
    fun `online fetch computes income from network and writes cache`() {
        val stats = statsFixture()
        provider.enqueueStats(response = Single.just(stats))

        val observer = repository.fetchMinerIncome(miner = MINER).test()

        observer.assertComplete()
        observer.assertValueCount(1)
        val income = observer.values()[0]
        assertEquals(expectedIncome().dayBtc, income.dayBtc, 1e-9)
        assertEquals(expectedIncome().hourBtc, income.hourBtc, 1e-9)
        assertEquals(expectedIncome().weekBtc, income.weekBtc, 1e-9)
        assertEquals(expectedIncome().monthBtc, income.monthBtc, 1e-9)
        assertNull(income.cachedAt) // fresh network data has no cache timestamp

        // cache written from network response
        val cached = statsDao.getByMiner(miner = MINER)
        assertNotNull(cached)
        assertEquals(MINER, cached!!.miner)
        assertTrue(cached.cachedAt > 0)
        assertEquals(stats, gson.fromJson(cached.data, ApiProviderEx::class.java))
    }

    @Test
    fun `online unpaid money computes from network and writes cache`() {
        provider.enqueueStats(response = Single.just(statsFixture(unpaid = 0.0042)))

        val observer = repository.fetchUnpaidMoney(miner = MINER).test()

        observer.assertComplete()
        observer.assertValue { Math.abs(it - 0.0042) < 1e-12 }
        assertNotNull(statsDao.getByMiner(miner = MINER))
    }

    // MARK: - Offline with cache: cached data returned

    @Test
    fun `offline with cache serves cached income with freshness timestamp`() {
        statsDao.save(entity = CachedStatsEntity(miner = MINER, data = gson.toJson(statsFixture()),
                cachedAt = 12345L))
        provider.enqueueStats(response = Single.error(IOException("offline")))

        val observer = repository.fetchMinerIncome(miner = MINER).test()

        observer.assertComplete()
        observer.assertValueCount(1)
        val income = observer.values()[0]
        assertEquals(expectedIncome(cachedAt = 12345L).dayBtc, income.dayBtc, 1e-9)
        assertEquals(expectedIncome(cachedAt = 12345L).monthBtc, income.monthBtc, 1e-9)
        assertEquals(12345L, income.cachedAt) // freshness indicator data
    }

    @Test
    fun `offline unpaid money falls back to cache`() {
        statsDao.save(entity = CachedStatsEntity(miner = MINER,
                data = gson.toJson(statsFixture(unpaid = 0.007)), cachedAt = 7L))
        provider.enqueueStats(response = Single.error(IOException("offline")))

        repository.fetchUnpaidMoney(miner = MINER).test()
                .assertComplete()
                .assertValue { Math.abs(it - 0.007) < 1e-12 }
    }

    // MARK: - Offline without cache: proper error state

    @Test
    fun `offline without cache propagates network error for income`() {
        val error = IOException("offline")
        provider.enqueueStats(response = Single.error(error))

        val observer = repository.fetchMinerIncome(miner = MINER).test()

        observer.assertError(IOException::class.java)
        observer.assertValueCount(0) // presenter shows offline state instead of data
    }

    @Test
    fun `offline without cache propagates network error for unpaid money`() {
        provider.enqueueStats(response = Single.error(IOException("offline")))

        repository.fetchUnpaidMoney(miner = MINER).test()
                .assertError(IOException::class.java)
                .assertValueCount(0)
    }

    // MARK: - Cache granularity is per miner

    @Test
    fun `cache is per miner - other miner without cache still errors offline`() {
        provider.enqueueStats(response = Single.just(statsFixture()))
        repository.fetchMinerIncome(miner = MINER).test().assertComplete()

        provider.enqueueStats(response = Single.error(IOException("offline")))
        repository.fetchMinerIncome(miner = "other-miner-wallet").test().assertError(IOException::class.java)
    }

    // MARK: - Recovery: online after offline updates cache

    @Test
    fun `recovery online after offline refreshes cache`() {
        val stale = statsFixture(unpaid = 0.001)
        statsDao.save(entity = CachedStatsEntity(miner = MINER, data = gson.toJson(stale), cachedAt = 1L))

        // offline: cached data served
        provider.enqueueStats(response = Single.error(IOException("offline")))
        repository.fetchMinerIncome(miner = MINER).test().assertComplete()

        // back online: fresh data returned and cache updated
        val fresh = statsFixture(unpaid = 0.009)
        provider.enqueueStats(response = Single.just(fresh))
        repository.fetchMinerIncome(miner = MINER).test()
                .assertComplete()
                .assertValue { it.cachedAt == null }

        val cached = statsDao.getByMiner(miner = MINER)
        assertNotNull(cached)
        assertEquals(fresh, gson.fromJson(cached!!.data, ApiProviderEx::class.java))
        assertTrue(cached.cachedAt > 1L)
    }
}
