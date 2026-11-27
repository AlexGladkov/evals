package com.dev.nicehash.data.implementations

import com.dev.nicehash.data.converters.MarketConverter
import com.dev.nicehash.data.providers.MarketProvider
import com.dev.nicehash.domain.models.HashrateOrder
import com.dev.nicehash.domain.repositories.MarketRepository
import io.reactivex.Single

/**
 * Marketplace repository implementation
 */
class MarketRepositoryImpl(val marketProvider: MarketProvider,
                           val marketConverter: MarketConverter) : MarketRepository {
    private val TAG = MarketRepositoryImpl::class.java.simpleName

    /**
     * Hashrate orders from marketplace
     * @return single wrap of orders list
     */
    override fun fetchOrders(): Single<List<HashrateOrder>> {
        return marketProvider.fetchOrders()
                .map { response -> marketConverter.apiToModels(result = response.result) }
    }
}
