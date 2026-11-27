package com.dev.nicehash.domain.repositories

import com.dev.nicehash.domain.models.HashrateOrder
import io.reactivex.Single

/**
 * Repository for marketplace api (hashrate orders)
 */
interface MarketRepository {
    fun fetchOrders(): Single<List<HashrateOrder>>
}
