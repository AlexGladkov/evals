package com.dev.nicehash.app.views

import com.dev.nicehash.domain.models.HashrateOrder
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * View for marketplace tab (hashrate orders)
 */
@StateStrategyType(value = OneExecutionStateStrategy::class)
interface MarketView: MvpView {
    fun showLoading()
    fun showOrders(orders: List<HashrateOrder>)
    fun showError(message: String)
    fun showEmpty()
}
