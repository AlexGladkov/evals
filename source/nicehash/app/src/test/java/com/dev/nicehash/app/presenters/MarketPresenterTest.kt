package com.dev.nicehash.app.presenters

import com.dev.nicehash.app.views.MarketView
import com.dev.nicehash.domain.models.HashrateOrder
import com.dev.nicehash.domain.repositories.MarketRepository
import com.dev.nicehash.testing.RxSchedulerRule
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Unit tests for marketplace presenter states
 */
class MarketPresenterTest {

    @get:Rule
    val schedulerRule = RxSchedulerRule()

    private class FakeMarketRepository(val result: Single<List<HashrateOrder>>) : MarketRepository {
        override fun fetchOrders(): Single<List<HashrateOrder>> = result
    }

    private class RecordingMarketView : MarketView {
        var loadingShown = false
        val ordersShown = mutableListOf<List<HashrateOrder>>()
        val errorsShown = mutableListOf<String>()
        var emptyShown = false

        override fun showLoading() {
            loadingShown = true
        }

        override fun showOrders(orders: List<HashrateOrder>) {
            ordersShown.add(orders)
        }

        override fun showError(message: String) {
            errorsShown.add(message)
        }

        override fun showEmpty() {
            emptyShown = true
        }
    }

    private fun order(id: String, price: Double): HashrateOrder {
        return HashrateOrder(id = id, algo = 24, price = price, speed = 0.5, alive = true)
    }

    @Test
    fun loadingStateShownOnStart() {
        val view = RecordingMarketView()
        val presenter = MarketPresenter(repository = FakeMarketRepository(Single.just(listOf(order("abc123", 0.12)))))
        presenter.attachView(view)

        presenter.fetchOrders()

        assertTrue(view.loadingShown)
    }

    @Test
    fun dataStateShownWithOrders() {
        val view = RecordingMarketView()
        val presenter = MarketPresenter(repository = FakeMarketRepository(Single.just(
                listOf(order("abc123", 0.12), order("def456", 0.35)))))
        presenter.attachView(view)

        presenter.fetchOrders()

        assertEquals(1, view.ordersShown.size)
        assertEquals(2, view.ordersShown[0].size)
        assertEquals("abc123", view.ordersShown[0][0].id)
        assertTrue(view.emptyShown.not())
        assertTrue(view.errorsShown.isEmpty())
    }

    @Test
    fun emptyStateShownWhenNoOrders() {
        val view = RecordingMarketView()
        val presenter = MarketPresenter(repository = FakeMarketRepository(Single.just(emptyList())))
        presenter.attachView(view)

        presenter.fetchOrders()

        assertTrue(view.emptyShown)
        assertTrue(view.ordersShown.isEmpty())
        assertFalse(view.loadingShown.not())
        assertTrue(view.errorsShown.isEmpty())
    }

    @Test
    fun errorStateShownOnNetworkFailure() {
        val view = RecordingMarketView()
        val presenter = MarketPresenter(repository = FakeMarketRepository(
                Single.error(RuntimeException("Network is unreachable"))))
        presenter.attachView(view)

        presenter.fetchOrders()

        assertEquals(1, view.errorsShown.size)
        assertEquals("Network is unreachable", view.errorsShown[0])
        assertTrue(view.ordersShown.isEmpty())
        assertTrue(view.emptyShown.not())
    }
}
