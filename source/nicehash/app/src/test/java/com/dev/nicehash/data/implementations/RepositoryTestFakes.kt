package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.MinerProvider
import com.dev.nicehash.data.providers.NicehashProvider
import com.dev.nicehash.data.providers.models.ApiBalance
import com.dev.nicehash.data.providers.models.ApiGlobalCurrent
import com.dev.nicehash.data.providers.models.ApiGlobal24
import com.dev.nicehash.data.providers.models.ApiPayment
import com.dev.nicehash.data.providers.models.ApiProvider
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.providers.models.ApiPayments
import com.dev.nicehash.data.providers.models.ApiWorker
import com.dev.nicehash.data.room.dao.CachedPayoutsDao
import com.dev.nicehash.data.room.dao.CachedStatsDao
import com.dev.nicehash.data.room.dao.CachedWorkersDao
import com.dev.nicehash.data.room.dao.MinerDao
import com.dev.nicehash.data.room.models.CachedPayoutsEntity
import com.dev.nicehash.data.room.models.CachedStatsEntity
import com.dev.nicehash.data.room.models.CachedWorkersEntity
import com.dev.nicehash.data.room.models.MinerEntity
import com.dev.nicehash.domain.models.ChartItem
import io.reactivex.Single
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

/**
 * Hand-written fakes for offline-first (cache-then-network) repository tests.
 * Plain JVM tests — no Android runtime / Robolectric required.
 */

class FakeNicehashProvider : NicehashProvider {
    private val statsQueue = ArrayDeque<Single<ApiProviderEx>>()
    val requestedFrom = mutableListOf<Long>()

    /** Queue the next fetchStatsProviderEx result; when the queue is empty the provider behaves as offline. */
    fun enqueueStats(response: Single<ApiProviderEx>) {
        statsQueue.addLast(response)
    }

    override fun fetchStatsProviderEx(miner: String, from: Long): Single<ApiProviderEx> {
        requestedFrom.add(from)
        return statsQueue.removeFirstOrNull() ?: Single.error(IOException("offline"))
    }

    override fun fetchGlobalCurrent(): Single<ApiGlobalCurrent> = throw UnsupportedOperationException()
    override fun fetchGlobal24(): Single<ApiGlobal24> = throw UnsupportedOperationException()
    override fun fetchStatsProvider(miner: String): Single<ApiProvider> = throw UnsupportedOperationException()
    override fun fetchStatsProviderPayments(miner: String, from: Long): Single<ApiPayments> = throw UnsupportedOperationException()
}

class FakeMinerProvider : MinerProvider {
    private val workersQueue = ArrayDeque<Single<List<ApiWorker>>>()
    private val paymentsQueue = ArrayDeque<Single<List<ApiPayment>>>()

    fun enqueueWorkers(response: Single<List<ApiWorker>>) { workersQueue.addLast(response) }
    fun enqueuePayments(response: Single<List<ApiPayment>>) { paymentsQueue.addLast(response) }

    override fun getWorkers(miner: String): Single<List<ApiWorker>> =
            workersQueue.removeFirstOrNull() ?: Single.error(IOException("offline"))

    override fun getPayments(miner: String): Single<List<ApiPayment>> =
            paymentsQueue.removeFirstOrNull() ?: Single.error(IOException("offline"))

    override fun getStatistics(miner: String, time: Long) = throw UnsupportedOperationException()
    override fun getChart(miner: String, time: Long, elapsedHours: Int): Single<List<ChartItem>> = throw UnsupportedOperationException()
    override fun getBalance(miner: String): Single<List<ApiBalance>> = throw UnsupportedOperationException()
}

class FakeMinerDao : MinerDao {
    val store = mutableListOf<MinerEntity>()
    override fun getMiners(): List<MinerEntity> = store.toList()
    override fun addMiner(minerEntity: MinerEntity): Long {
        store.add(minerEntity)
        return store.size.toLong()
    }
    override fun clearMiners() = store.clear()
}

class FakeCachedStatsDao : CachedStatsDao {
    val store = ConcurrentHashMap<String, CachedStatsEntity>()
    override fun getByMiner(miner: String): CachedStatsEntity? = store[miner]
    override fun save(entity: CachedStatsEntity) { store[entity.miner] = entity }
    override fun clear() = store.clear()
}

class FakeCachedWorkersDao : CachedWorkersDao {
    val store = ConcurrentHashMap<String, CachedWorkersEntity>()
    override fun getByMiner(miner: String): CachedWorkersEntity? = store[miner]
    override fun save(entity: CachedWorkersEntity) { store[entity.miner] = entity }
    override fun clear() = store.clear()
}

class FakeCachedPayoutsDao : CachedPayoutsDao {
    val store = ConcurrentHashMap<String, CachedPayoutsEntity>()
    override fun getByMiner(miner: String): CachedPayoutsEntity? = store[miner]
    override fun save(entity: CachedPayoutsEntity) { store[entity.miner] = entity }
    override fun clear() = store.clear()
}
