package com.dev.nicehash.data.providers.implementations

import com.dev.nicehash.contracts.RemoteContract
import com.dev.nicehash.data.providers.MinerProvider
import com.dev.nicehash.data.providers.models.*
import com.dev.nicehash.data.providers.services.RemoteMinerService
import com.dev.nicehash.domain.models.ChartItem
import com.google.gson.Gson
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.util.*

/**
 * Created by Alex Gladkov on 05.08.18.
 * Provider implementation for Miner provider interface
 */
class MinerProviderImpl(private val remoteMinerService: RemoteMinerService,
                        private val gson: Gson) : MinerProvider {

    override fun getChart(miner: String, time: Long, elapsedHours: Int): Single<List<ChartItem>> {
        return Single.create { subscriber ->
            remoteMinerService.getWorkerHistory(method = RemoteContract.METHOD_STATS_PROVIDER_EX, miner = miner, from = time)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ chartResponse ->
                        val past = chartResponse.result.get("past")?.asJsonArray
                        val current = chartResponse.result.get("current")?.asJsonArray
                        val profitabilityItems = LinkedHashMap<Int, ApiAlgo>()
                        val chartItems = LinkedHashMap<Long, Double>()

                        current?.forEach { algo ->
                            val accepted = algo.asJsonObject.get("data")!!.asJsonArray[0]?.asJsonObject?.get("a")
                            accepted?.let { acceptedElement ->
                                profitabilityItems[algo.asJsonObject.get("algo")?.asInt
                                        ?: -1] = ApiAlgo(speed = acceptedElement.asDouble,
                                        price = algo.asJsonObject.get("profitability")?.asDouble
                                                ?: 0.0)
                            }
                        }

                        past?.forEach { it ->
                            val data = it.asJsonObject.get("data").asJsonArray
                            val algo = it.asJsonObject.get("algo")?.asInt ?: -2
                            data.forEach { dataItem ->
                                val timestamp = dataItem.asJsonArray[0].asLong * 300
                                val localDateTime = DateTime(timestamp * 1000).millis

                                val speedObject = dataItem.asJsonArray[1].asJsonObject
                                speedObject.get("a")?.asDouble?.let { value ->
                                    val profitability = (profitabilityItems[algo]?.price ?: 0.0) *
                                            (profitabilityItems[algo]?.speed ?: 0.0)
                                    val currentSpeed = profitabilityItems[algo]?.speed ?: 0.0
                                    val multi = value / currentSpeed
                                    val pastProfitability = multi * profitability

                                    if (chartItems.containsKey(localDateTime)) {
                                        chartItems[localDateTime] = (chartItems[localDateTime] ?: 0.0) + pastProfitability
                                    } else {
                                        chartItems[localDateTime] = pastProfitability
                                    }
                                }
                            }
                        }

                        val tempData = LinkedList<ChartItem>()
                        chartItems.keys.forEach { key ->
                            tempData.add(ChartItem(x = key / 1000, y = chartItems[key] ?: 0.0))
                        }

                        subscriber.onSuccess(tempData)
                    }, {
                        subscriber.onError(it)
                    })
        }
    }

    override fun getBalance(miner: String): Single<List<ApiBalance>> {
        return remoteMinerService.getStatistics(method = RemoteContract.METHOD_STATS_PROVIDER_EX, miner = miner)
                .flatMap {
                    val current = it.result.get("current").asJsonArray
                    if (current == null) {
                        Single.error<List<ApiBalance>>(NullPointerException())
                    } else {
                        val converted = ArrayList<ApiBalance>(current.size())
                        current.forEach {
                            converted.add(gson.fromJson(it.asJsonObject, ApiBalance::class.java))
                        }
                        Single.just(converted)
                    }
                }
    }

    override fun getWorkers(miner: String): Single<List<ApiWorker>> {
        return remoteMinerService.getWorkers(method = RemoteContract.METHOD_STATS_PROVIDER_WORKERS, miner = miner)
                .flatMap {
                    val workers = it.result.get("workers").asJsonArray
                    if (workers == null) {
                        Single.error<List<ApiWorker>>(NullPointerException())
                    } else {
                        val converted = ArrayList<ApiWorker>(workers.size())
                        workers.forEach {
                            converted.add(ApiWorker(it.asJsonArray[0].asString,
                                    it.asJsonArray[2].asInt))
                        }
                        Single.just(converted)
                    }
                }
    }

    override fun getPayments(miner: String): Single<List<ApiPayment>> {
        return remoteMinerService.getStatistics(method = RemoteContract.METHOD_STATS_PROVIDER, miner = miner)
                .flatMap {
                    val payments = it.result.get("payments").asJsonArray
                    if (payments == null) {
                        Single.error<List<ApiPayment>>(NullPointerException())
                    } else {
                        val converted = ArrayList<ApiPayment>(payments.size())
                        payments.forEach { converted.add(gson.fromJson(it.asJsonObject, ApiPayment::class.java)) }
                        Single.just(converted)
                    }
                }
    }
}
