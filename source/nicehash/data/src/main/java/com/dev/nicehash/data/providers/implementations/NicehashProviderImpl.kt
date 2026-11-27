package com.dev.nicehash.data.providers.implementations

import com.dev.nicehash.data.providers.NicehashProvider
import com.dev.nicehash.data.providers.models.*
import com.dev.nicehash.data.providers.services.RemoteMinerService
import com.google.gson.Gson
import io.reactivex.Single
import java.util.*

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Implementation for NiceHash provider interface
 */
class NicehashProviderImpl(val remoteMinerService: RemoteMinerService): NicehashProvider {
    private val gson = Gson()

    private var lastProviderEx = 0L

    /**
     * Get current profitability (price) and hashing speed for all algorithms. Refreshed every 30 seconds.
     */
    override fun fetchGlobalCurrent(): Single<ApiGlobalCurrent> {
        return remoteMinerService.getStatsGlobalCurrent()
                .flatMap { parseGlobalCurrent(response = it) }
    }

    /**
     * Get average profitability (price) and hashing speed for all algorithms in past 24 hours.
     */
    override fun fetchGlobal24(): Single<ApiGlobal24> {
        return remoteMinerService.getStatsGlobal24H()
                .flatMap { parseGlobal24(response = it) }
    }

    /**
     * Get current stats for provider for all algorithms. Refreshed every 30 seconds. It also returns past 56 payments.
     * @param miner - Provider's BTC address
     */
    override fun fetchStatsProvider(miner: String): Single<ApiProvider> {
        return remoteMinerService.getStatsProvider(miner = miner)
                .flatMap { parseStatsProvider(response = it) }
    }

    /**
     * Get detailed stats for provider for all algorithms including history data and payments in past 7 days.
     * @param miner - Provider's BTC address
     * @param from - Get history data from this time (UNIX timestamp). This parameter is optional and is by default considered to be 0 (return complete history).
     */
    override fun fetchStatsProviderEx(miner: String, from: Long): Single<ApiProviderEx> {
        return remoteMinerService.getStatsProviderEx(miner = miner, from = from)
                .cache()
                .flatMap { parseStatsProviderEx(response = it) }
    }

    /**
     * Get payments for provider.
     * @param miner - Provider's BTC address
     * @param from - Get history data from this time (UNIX timestamp). This parameter is optional and is by default considered to be 0 (return complete history).
     */
    override fun fetchStatsProviderPayments(miner: String, from: Long): Single<ApiPayments> {
        return remoteMinerService.getStatsProviderPayments(miner = miner, from = from)
                .flatMap { parseStatsProviderPayments(response = it) }
    }

    // MARK: - JSON parsing (extracted from inline flatMap lambdas)

    private fun parseGlobalCurrent(response: ApiResponse): Single<ApiGlobalCurrent> {
        val stats = response.result.get("stats")?.asJsonArray
        if (stats == null) {
            return Single.error(NullPointerException())
        }

        val list = LinkedList<ApiGlobalCurrentStats>()
        stats.forEach { list.add(gson.fromJson(it, ApiGlobalCurrentStats::class.java)) }
        return Single.just(ApiGlobalCurrent(method = response.method, result = list))
    }

    private fun parseGlobal24(response: ApiResponse): Single<ApiGlobal24> {
        val stats = response.result.get("stats")?.asJsonArray
        if (stats == null) {
            return Single.error(NullPointerException())
        }

        val list = LinkedList<ApiGlobal24Stats>()
        stats.forEach { list.add(gson.fromJson(it, ApiGlobal24Stats::class.java)) }
        return Single.just(ApiGlobal24(method = response.method, result = list))
    }

    private fun parseStatsProvider(response: ApiResponse): Single<ApiProvider> {
        val stats = response.result.get("stats")?.asJsonArray
        if (stats == null) {
            return Single.error(NullPointerException())
        }

        val list = LinkedList<ApiProviderStats>()
        stats.forEach { list.add(gson.fromJson(it, ApiProviderStats::class.java)) }
        return Single.just(ApiProvider(method = response.method, result = list))
    }

    private fun parseStatsProviderEx(response: ApiResponse): Single<ApiProviderEx> {
        val current = response.result.get("current")?.asJsonArray
        val past = response.result.get("past")?.asJsonArray
        val payments = response.result.get("payments")?.asJsonArray
        val addr = response.result.get("addr")?.asString
        if (current == null || past == null || payments == null || addr == null) {
            return Single.error(NullPointerException())
        }

        val currentData = LinkedList<ApiProviderCurrent>()
        val pastData = LinkedList<ApiProviderPast>()
        current.forEach {
            val dataItem = it.asJsonObject.get("data").asJsonArray
            currentData.add(ApiProviderCurrent(profitability = it.asJsonObject.get("profitability").asDouble,
                    suffix = it.asJsonObject.get("suffix").asString,
                    name = it.asJsonObject.get("name").asString,
                    algo = it.asJsonObject.get("algo").asInt,
                    data = ApiProviderData(a = dataItem[0].asJsonObject.get("a").asFloat,
                            rs = dataItem[0].asJsonObject.get("rs")?.asFloat ?: 0f,
                            unpaid = dataItem[1].asDouble)))
        }

        past.forEach {
            val data = LinkedList<ApiProviderPastData>()
            val jsonData = it.asJsonObject.get("data").asJsonArray

            try {
                jsonData.forEach { dataItem ->
                    data.add(ApiProviderPastData(time = dataItem.asJsonArray[0].asLong,
                            a = dataItem.asJsonArray[1].asJsonObject.get("a").asFloat,
                            unpaid = dataItem.asJsonArray[2].asDouble))
                }
            } catch (e: Exception) {
                // malformed history entries are skipped, same as before
            }

            pastData.add(ApiProviderPast(algo = it.asJsonObject.get("algo").asInt,
                    data = data))
        }

        return Single.just(ApiProviderEx(method = response.method, current = currentData,
                past = pastData, payments = LinkedList(), addr = addr))
    }

    private fun parseStatsProviderPayments(response: ApiResponse): Single<ApiPayments> {
        val payments = response.result.get("payments")?.asJsonArray
        val addr = response.result.get("addr")?.asString
        val nh_wallet = response.result.get("nh_wallet")?.asBoolean
        if (payments == null || addr == null || nh_wallet == null) {
            return Single.error(NullPointerException())
        }

        val list = LinkedList<ApiPaymentsData>()
        payments.forEach { list.add(gson.fromJson(it, ApiPaymentsData::class.java)) }
        return Single.just(ApiPayments(method = response.method, addr = addr, nh_wallet = nh_wallet,
                payments = list))
    }
}
