package com.dev.nicehash.data.implementations

import android.util.Log
import com.dev.nicehash.data.providers.NicehashProvider
import com.dev.nicehash.domain.IncomeCalculator
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Money repository implementation
 */
class MoneyRepositoryImpl(val nicehashProvider: NicehashProvider,
                          private val incomeCalculator: IncomeCalculator) : MoneyRepository {
    private val TAG = MoneyRepositoryImpl::class.java.simpleName

    /**
     * Miner unpaid balance
     * @param miner - wallet BTC address
     * @return single wrap of unpaid balance value
     */
    override fun fetchUnpaidMoney(miner: String): Single<Double> {
        return nicehashProvider.fetchStatsProviderEx(miner = miner, from = 0L)
                .flatMap { result ->
                    Single.just(result.current.sumByDouble { it.data.unpaid })
                }
    }

    /**
     * Miner income for week, day, month, year
     * @param miner - wallet BTC address
     * @return single wrap of Income model
     */
    override fun fetchMinerIncome(miner: String): Single<Income> {
        return nicehashProvider.fetchStatsProviderEx(miner = miner, from = DateTime().minusHours(25).millis / 1000)
                .flatMap { result ->
                    Single.just(incomeCalculator.calculate(stats = result, nowMillis = DateTime().millis))
                }
    }
}