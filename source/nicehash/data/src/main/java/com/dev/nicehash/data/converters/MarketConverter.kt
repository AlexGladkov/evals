package com.dev.nicehash.data.converters

import com.dev.nicehash.data.providers.models.ApiOrder
import com.dev.nicehash.domain.models.HashrateOrder

/**
 * Converter between api and domain layer for marketplace orders
 */
interface MarketConverter {
    fun apiToModel(apiOrder: ApiOrder): HashrateOrder
    fun apiToModels(result: List<ApiOrder>): List<HashrateOrder>
}
