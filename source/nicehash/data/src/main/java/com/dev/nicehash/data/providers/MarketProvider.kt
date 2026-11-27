package com.dev.nicehash.data.providers

import com.dev.nicehash.data.providers.models.ApiOrdersResult
import io.reactivex.Single

/**
 * Provider for marketplace api of NiceHash service (hashrate orders)
 */
interface MarketProvider {
    /**
     * @return single wrap of hashrate orders list
     */
    fun fetchOrders(): Single<ApiOrdersResult>
}
