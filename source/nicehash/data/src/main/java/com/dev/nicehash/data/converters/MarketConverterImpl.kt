package com.dev.nicehash.data.converters

import com.dev.nicehash.data.providers.models.ApiOrder
import com.dev.nicehash.domain.models.HashrateOrder
import java.util.LinkedList

/**
 * Implementation for marketplace converter interface
 * @see MarketConverter
 */
class MarketConverterImpl : MarketConverter {

    override fun apiToModel(apiOrder: ApiOrder): HashrateOrder {
        return HashrateOrder(id = apiOrder.id.orEmpty(),
                algo = apiOrder.algo ?: 0,
                price = apiOrder.price ?: 0.0,
                speed = apiOrder.speed ?: 0.0,
                alive = apiOrder.alive ?: false)
    }

    override fun apiToModels(result: List<ApiOrder>): List<HashrateOrder> {
        val orders = LinkedList<HashrateOrder>()
        result.forEach { order ->
            orders.add(apiToModel(apiOrder = order))
        }

        return orders
    }
}
