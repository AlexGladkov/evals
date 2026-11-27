package com.dev.nicehash.data.providers.implementations

import com.dev.nicehash.data.providers.MarketProvider
import com.dev.nicehash.data.providers.models.ApiOrder
import com.dev.nicehash.data.providers.models.ApiOrdersResult
import com.dev.nicehash.data.providers.services.RemoteMinerService
import com.google.gson.Gson
import io.reactivex.Single
import java.util.LinkedList

/**
 * Implementation for marketplace provider interface
 * @see MarketProvider
 */
class MarketProviderImpl(val remoteMinerService: RemoteMinerService) : MarketProvider {
    private val TAG = MarketProviderImpl::class.java.simpleName
    private val gson = Gson()

    /**
     * Get hashrate orders from marketplace.
     */
    override fun fetchOrders(): Single<ApiOrdersResult> {
        return remoteMinerService.getOrders()
                .flatMap { response ->
                    val orders = response.result.get("orders")?.asJsonArray
                    if (orders == null) {
                        Single.error(NullPointerException("orders is missing in response"))
                    } else {
                        val list = LinkedList<ApiOrder>()
                        orders.forEach { order ->
                            try {
                                list.add(gson.fromJson(order, ApiOrder::class.java))
                            } catch (error: Exception) {
                                // skip malformed order instead of failing the whole request
                            }
                        }

                        Single.just(ApiOrdersResult(method = response.method, result = list))
                    }
                }
    }
}
