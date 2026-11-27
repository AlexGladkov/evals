package com.dev.nicehash.data.implementations

import android.util.Log
import com.dev.nicehash.data.converters.BalanceConverter
import com.dev.nicehash.data.converters.MinerConverter
import com.dev.nicehash.data.converters.PaymentsConverter
import com.dev.nicehash.data.converters.WorkerConverter
import com.dev.nicehash.data.providers.MinerProvider
import com.dev.nicehash.data.providers.models.ApiPayment
import com.dev.nicehash.data.providers.models.ApiWorker
import com.dev.nicehash.data.room.dao.CachedPayoutsDao
import com.dev.nicehash.data.room.dao.CachedWorkersDao
import com.dev.nicehash.data.room.dao.MinerDao
import com.dev.nicehash.data.room.models.CachedPayoutsEntity
import com.dev.nicehash.data.room.models.CachedWorkersEntity
import com.dev.nicehash.data.room.models.MinerEntity
import com.dev.nicehash.domain.models.*
import com.dev.nicehash.domain.repositories.MinerRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Alex Gladkov on 23.06.18.
 * Repository for any miners activity
 *
 * Offline-first (cache-then-network) for workers & payouts: network is the primary
 * source (online behavior unchanged); every successful response is written to Room.
 * When network fails, the Room cache is served. If there is no cache either,
 * the original network error is propagated.
 */
class MinerRepositoryImpl(private val minerDao: MinerDao,
                          val minerProvider: MinerProvider,
                          private val minerConverter: MinerConverter,
                          private val paymentsConverter: PaymentsConverter,
                          private val balanceConverter: BalanceConverter,
                          private val workerConverter: WorkerConverter,
                          private val cachedWorkersDao: CachedWorkersDao,
                          private val cachedPayoutsDao: CachedPayoutsDao,
                          private val gson: Gson) : MinerRepository {
    private val TAG: String = MinerRepositoryImpl::class.java.simpleName

    companion object {
        // Demo wallet used for statistics warm-up request (kept identical to previous inline literal)
        private const val DEMO_MINER_ADDRESS = "3Aqj4o27ixprba67GvBctDkXijxxpbD4hS"
    }

    override fun fetchMinerStatistics() {
        minerProvider.getBalance(miner = DEMO_MINER_ADDRESS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                }, { error ->
                })
    }

    override fun fetchMinerBalance(miner: String): Single<List<Balance>> {
        return minerProvider.getBalance(miner = miner)
                .flatMap { response -> Single.just(response.map { model -> balanceConverter.apiToModel(apiBalance = model) }) }
    }

    override fun fetchWorkers(miner: String): Single<List<WorkerHub>> {
        return minerProvider.getWorkers(miner = miner)
                .doOnSuccess { response -> cacheWorkers(miner = miner, workers = response) }
                .flatMap { response ->
                    Single.just(
                            workerConverter.workerToHub(response.map { model -> workerConverter.apiToModel(apiWorker = model) }))
                }
                .onErrorResumeNext { error -> cachedWorkers(miner = miner, networkError = error) }
    }

    override fun fetchMinerPayments(miner: String): Single<List<Payout>> {
        return minerProvider.getPayments(miner = miner)
                .doOnSuccess { response -> cachePayouts(miner = miner, payments = response) }
                .flatMap { response -> Single.just(response.map { model -> paymentsConverter.apiToModel(apiPayment = model) }) }
                .onErrorResumeNext { error -> cachedPayouts(miner = miner, networkError = error) }
    }

    override fun fetchMinerChart(miner: String, time: Long, elapsedHours: Int): Single<List<ChartItem>> {
        return minerProvider.getChart(miner = miner, time = time, elapsedHours = elapsedHours)

    }

    override fun loadMinersFromDb(minerId: Int): Single<List<Miner>> {
        return Single.create { subscriber ->
            try {
                val miners = minerDao.getMiners()

                val result = ArrayList<Miner>()
                if (miners.isEmpty()) {
                    result.add(Miner(id = -1, name = "Demo Miner", hash = "Your BTC wallet address", isSelected = false))
                } else {
                    result.addAll(miners.map { minerConverter.dbToModel(minerEntity = it, minerId = minerId) }
                            .sortedWith(compareBy { it.isSelected })
                            .reversed())
                }

                subscriber.onSuccess(result)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    override fun updateMiner(miner: Miner?, newName: String, newHash: String): Single<Boolean> {
        return Single.create { subscriber ->
            if (miner == null) subscriber.onError(Throwable(NullPointerException()))
            try {
                minerDao.addMiner(MinerEntity(id = miner?.id, name = newName, hash = newHash,
                        payouts = ""))
                subscriber.onSuccess(true)
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    override fun addMiner(miner: Miner): Single<Long> {
        return Single.create { subscriber ->
            try {
                subscriber.onSuccess(minerDao.addMiner(MinerEntity(id = null, name = miner.name,
                        hash = miner.hash, payouts = "")))
            } catch (e: Exception) {
                subscriber.onError(e)
            }
        }
    }

    // MARK: - Offline-first helpers (cache-then-network)

    /**
     * Serves cached workers when network is unavailable; falls back to the original
     * network error when cache is empty / unreadable.
     */
    private fun cachedWorkers(miner: String, networkError: Throwable): Single<List<WorkerHub>> {
        val cached = readWorkersCache(miner = miner) ?: return Single.error(networkError)

        return try {
            val type = object : TypeToken<List<ApiWorker>>() {}.type
            val workers: List<ApiWorker> = gson.fromJson(cached.data, type)
            Single.just(workerConverter.workerToHub(workers.map { model -> workerConverter.apiToModel(apiWorker = model) }))
        } catch (e: Exception) {
            Single.error(networkError)
        }
    }

    private fun cachedPayouts(miner: String, networkError: Throwable): Single<List<Payout>> {
        val cached = readPayoutsCache(miner = miner) ?: return Single.error(networkError)

        return try {
            val type = object : TypeToken<List<ApiPayment>>() {}.type
            val payments: List<ApiPayment> = gson.fromJson(cached.data, type)
            Single.just(payments.map { model -> paymentsConverter.apiToModel(apiPayment = model) })
        } catch (e: Exception) {
            Single.error(networkError)
        }
    }

    private fun readWorkersCache(miner: String): CachedWorkersEntity? {
        return try {
            cachedWorkersDao.getByMiner(miner = miner)
        } catch (e: Exception) {
            null
        }
    }

    private fun readPayoutsCache(miner: String): CachedPayoutsEntity? {
        return try {
            cachedPayoutsDao.getByMiner(miner = miner)
        } catch (e: Exception) {
            null
        }
    }

    private fun cacheWorkers(miner: String, workers: List<ApiWorker>) {
        cachedWorkersDao.save(CachedWorkersEntity(miner = miner, data = gson.toJson(workers),
                cachedAt = System.currentTimeMillis()))
    }

    private fun cachePayouts(miner: String, payments: List<ApiPayment>) {
        cachedPayoutsDao.save(CachedPayoutsEntity(miner = miner, data = gson.toJson(payments),
                cachedAt = System.currentTimeMillis()))
    }
}
