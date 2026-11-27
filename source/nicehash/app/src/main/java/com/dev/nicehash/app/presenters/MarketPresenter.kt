package com.dev.nicehash.app.presenters

import com.dev.nicehash.app.views.MarketView
import com.dev.nicehash.domain.repositories.MarketRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

/**
 * Presenter for marketplace tab (hashrate orders)
 */
@InjectViewState
class MarketPresenter(val repository: MarketRepository) : MvpPresenter<MarketView>() {

    fun fetchOrders() {
        viewState.showLoading()
        repository.fetchOrders()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ orders ->
                    if (orders.isEmpty()) {
                        viewState.showEmpty()
                    } else {
                        viewState.showOrders(orders = orders)
                    }
                }, { error ->
                    viewState.showError(message = error.localizedMessage ?: "Error")
                })
    }
}
