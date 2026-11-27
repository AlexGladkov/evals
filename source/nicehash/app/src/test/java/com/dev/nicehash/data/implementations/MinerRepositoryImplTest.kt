package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.converters.BalanceConverterImpl
import com.dev.nicehash.data.converters.MinerConverterImpl
import com.dev.nicehash.data.converters.PaymentsConverterImpl
import com.dev.nicehash.data.converters.WorkerConverterImpl
import com.dev.nicehash.data.providers.models.ApiPayment
import com.dev.nicehash.data.providers.models.ApiWorker
import com.dev.nicehash.data.room.models.CachedPayoutsEntity
import com.dev.nicehash.data.room.models.CachedWorkersEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/**
 * Offline-first (cache-then-network) tests for MinerRepositoryImpl (workers & payouts).
 * Plain JVM test with a fake provider + fake Room DAOs + real converters.
 */
class MinerRepositoryImplTest {

    private val gson = Gson()
    private val minerDao = FakeMinerDao()
    private val workersDao = FakeCachedWorkersDao()
    private val payoutsDao = FakeCachedPayoutsDao()
    private val provider = FakeMinerProvider()

    private fun repository() = MinerRepositoryImpl(minerDao = minerDao, minerProvider = provider,
            minerConverter = MinerConverterImpl(), paymentsConverter = PaymentsConverterImpl(),
            balanceConverter = BalanceConverterImpl(), workerConverter = WorkerConverterImpl(),
            cachedWorkersDao = workersDao, cachedPayoutsDao = payoutsDao, gson = gson)

    companion object {
        private const val MINER = "3Aqj4o27ixprba67GvBctDkXijxxpbD4hS"
        private val workersType = object : TypeToken<List<ApiWorker>>() {}.type
        private val paymentsType = object : TypeToken<List<ApiPayment>>() {}.type
    }

    private fun workersFixture(): List<ApiWorker> = listOf(ApiWorker(title = "rig1", time = 68),
            ApiWorker(title = "rig2", time = 46))

    private fun paymentsFixture(): List<ApiPayment> = listOf(ApiPayment(amount = 0.1, fee = 0.001,
            time = "2022-01-01 10:00:00", type = 0, TXID = "tx1"))

    // MARK: - Workers

    @Test
    fun `online workers fetch returns hubs and writes cache`() {
        val workers = workersFixture()
        provider.enqueueWorkers(response = Single.just(workers))

        val observer = repository().fetchWorkers(miner = MINER).test()

        observer.assertComplete()
        assertEquals(listOf("rig1", "rig2"), observer.values()[0].map { it.title })

        val cached = workersDao.getByMiner(miner = MINER)
        assertNotNull(cached)
        assertEquals(MINER, cached!!.miner)
        assertTrue(cached.cachedAt > 0)
        assertEquals(workers, gson.fromJson<List<ApiWorker>>(cached.data, workersType))
    }

    @Test
    fun `offline workers with cache serves cached hubs`() {
        workersDao.save(entity = CachedWorkersEntity(miner = MINER, data = gson.toJson(workersFixture()),
                cachedAt = 42L))
        provider.enqueueWorkers(response = Single.error(IOException("offline")))

        val observer = repository().fetchWorkers(miner = MINER).test()

        observer.assertComplete()
        assertEquals(listOf("rig1", "rig2"), observer.values()[0].map { it.title })
    }

    @Test
    fun `offline workers without cache propagates network error`() {
        provider.enqueueWorkers(response = Single.error(IOException("offline")))

        repository().fetchWorkers(miner = MINER).test()
                .assertError(IOException::class.java)
                .assertValueCount(0)
    }

    @Test
    fun `workers recovery online after offline updates cache`() {
        workersDao.save(entity = CachedWorkersEntity(miner = MINER,
                data = gson.toJson(listOf(ApiWorker(title = "old-rig", time = 10))), cachedAt = 1L))

        // offline: cached workers served
        provider.enqueueWorkers(response = Single.error(IOException("offline")))
        repository().fetchWorkers(miner = MINER).test()
                .assertComplete()
                .assertValue { it.map { hub -> hub.title } == listOf("old-rig") }

        // back online: fresh workers served and cache updated
        val fresh = listOf(ApiWorker(title = "new-rig", time = 99))
        provider.enqueueWorkers(response = Single.just(fresh))
        repository().fetchWorkers(miner = MINER).test()
                .assertComplete()
                .assertValue { it.map { hub -> hub.title } == listOf("new-rig") }

        assertEquals(fresh, gson.fromJson<List<ApiWorker>>(workersDao.getByMiner(miner = MINER)!!.data,
                workersType))
    }

    // MARK: - Payouts

    @Test
    fun `online payouts fetch returns payouts and writes cache`() {
        val payments = paymentsFixture()
        provider.enqueuePayments(response = Single.just(payments))

        val observer = repository().fetchMinerPayments(miner = MINER).test()

        observer.assertComplete()
        observer.assertValueCount(1)
        assertEquals(0.1, observer.values()[0][0].payBtc, 1e-12)
        assertEquals(1f, observer.values()[0][0].fee) // (0.001 / 0.1) * 100

        val cached = payoutsDao.getByMiner(miner = MINER)
        assertNotNull(cached)
        assertTrue(cached!!.cachedAt > 0)
        assertEquals(payments, gson.fromJson<List<ApiPayment>>(cached.data, paymentsType))
    }

    @Test
    fun `offline payouts with cache serves cached payouts`() {
        payoutsDao.save(entity = CachedPayoutsEntity(miner = MINER, data = gson.toJson(paymentsFixture()),
                cachedAt = 42L))
        provider.enqueuePayments(response = Single.error(IOException("offline")))

        val observer = repository().fetchMinerPayments(miner = MINER).test()

        observer.assertComplete()
        assertEquals(0.1, observer.values()[0][0].payBtc, 1e-12)
    }

    @Test
    fun `offline payouts without cache propagates network error`() {
        provider.enqueuePayments(response = Single.error(IOException("offline")))

        repository().fetchMinerPayments(miner = MINER).test()
                .assertError(IOException::class.java)
                .assertValueCount(0)
    }

    // MARK: - Existing miner CRUD unaffected (regression)

    @Test
    fun `addMiner and loadMinersFromDb still work`() {
        repository().addMiner(com.dev.nicehash.domain.models.Miner(id = 0, name = "Test",
                hash = "wallet", isSelected = false)).test().assertComplete()

        repository().loadMinersFromDb(minerId = -1).test()
                .assertValue { it.size == 1 && it[0].name == "Test" }
    }
}
