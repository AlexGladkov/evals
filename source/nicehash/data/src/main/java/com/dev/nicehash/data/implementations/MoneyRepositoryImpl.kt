package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.providers.NicehashProvider
import com.dev.nicehash.data.providers.models.ApiProviderEx
import com.dev.nicehash.data.room.dao.CachedStatsDao
import com.dev.nicehash.data.room.models.CachedStatsEntity
import com.dev.nicehash.domain.IncomeCalculator
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import com.google.gson.Gson
import io.reactivex.Single
import org.joda.time.DateTime

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Money repository implementation
 *
 * Offline-first (cache-then-network, task a5): stats are fetched from network
 * when online and written to Room cache. When the network fails, the Room cache
 * is served instead, so the app keeps working offline. If there is neither
 * network nor cache, the original network error is propagated.
 *
 * Income math is delegated to [IncomeCalculator] (task c5) with the task/c2
 * fixes (empty data -> 0.0, zero-speed buckets skipped, real month days).
 */
class MoneyRepositoryImpl(val nicehashProvider: NicehashProvider,
                          private val incomeCalculator: IncomeCalculator,
                          private val cachedStatsDao: CachedStatsDao,
                          private val gson: Gson) : MoneyRepository {
    private val TAG = MoneyRepositoryImpl::class.java.simpleName

    /**
     * Miner unpaid balance
     * @param miner - wallet BTC address
     * @return single wrap of unpaid balance value
     */
    override fun fetchUnpaidMoney(miner: String): Single<Double> {
        return nicehashProvider.fetchStatsProviderEx(miner = miner, from = 0L)
                .doOnSuccess { result -> cacheStats(miner = miner, stats = result) }
                .flatMap { result -> Single.just(computeUnpaid(result = result)) }
                .onErrorResumeNext { error -> cachedUnpaid(miner = miner, networkError = error) }
    }

    /**
     * Miner income for week, day, month, year
     * @param miner - wallet BTC address
     * @return single wrap of Income model
     */
    override fun fetchMinerIncome(miner: String): Single<Income> {
        return nicehashProvider.fetchStatsProviderEx(miner = miner, from = DateTime().minusHours(25).millis / 1000)
                .doOnSuccess { result -> cacheStats(miner = miner, stats = result) }
                .flatMap { result -> Single.just(computeIncome(result = result)) }
                .onErrorResumeNext { error -> cachedIncome(miner = miner, networkError = error) }
    }

    /**
     * Cache-then-network helper: serves last cached stats when network is unavailable.
     * Falls back to the original network error when cache is empty / unreadable.
     */
    private fun cachedIncome(miner: String, networkError: Throwable): Single<Income> {
        val cached = readStatsCache(miner = miner) ?: return Single.error(networkError)

        return try {
            val stats = gson.fromJson(cached.data, ApiProviderEx::class.java)
            Single.just(computeIncome(result = stats).copy(cachedAt = cached.cachedAt))
        } catch (e: Exception) {
            Single.error(networkError)
        }
    }

    private fun cachedUnpaid(miner: String, networkError: Throwable): Single<Double> {
        val cached = readStatsCache(miner = miner) ?: return Single.error(networkError)

        return try {
            Single.just(computeUnpaid(result = gson.fromJson(cached.data, ApiProviderEx::class.java)))
        } catch (e: Exception) {
            Single.error(networkError)
        }
    }

    private fun readStatsCache(miner: String): CachedStatsEntity? {
        return try {
            cachedStatsDao.getByMiner(miner = miner)
        } catch (e: Exception) {
            null
        }
    }

    private fun cacheStats(miner: String, stats: ApiProviderEx) {
        cachedStatsDao.save(CachedStatsEntity(miner = miner, data = gson.toJson(stats),
                cachedAt = System.currentTimeMillis()))
    }

    private fun computeUnpaid(result: ApiProviderEx): Double {
        return result.current.sumByDouble { it.data.unpaid }
    }

    private fun computeIncome(result: ApiProviderEx): Income {
        return incomeCalculator.calculate(stats = result, nowMillis = DateTime().millis)
    }
}
