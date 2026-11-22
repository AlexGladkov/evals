package com.dev.nicehash.app.viewmodels

import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

/**
 * JVM unit tests for IncomeViewModel (former IncomePresenter behavior).
 *
 * The ViewModel subscribes on Schedulers.newThread() and observes on
 * AndroidSchedulers.mainThread(); both are substituted with trampoline here so the
 * whole chain runs synchronously on the JVM.
 */
class IncomeViewModelTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }

        @AfterClass
        @JvmStatic
        fun tearDownClass() {
            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
        }
    }

    @Before
    fun setUp() {
        // reset in case a previous test in the same JVM left stale hooks
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    // MARK: - Test doubles

    private class FakeMoneyRepository(var result: Single<Income>) : MoneyRepository {
        var requestedMiner: String? = null
            private set

        override fun fetchMinerIncome(miner: String): Single<Income> {
            requestedMiner = miner
            return result
        }

        override fun fetchUnpaidMoney(miner: String): Single<Double> = Single.never()
    }

    private class FakeExchangeServer(private val rates: Map<String, Float>) : ExchangeServer {
        val convertedFor = mutableListOf<Pair<String, Double>>()

        override fun convertBtcFor(code: String, value: Double): Float {
            convertedFor.add(code to value)
            val rate = rates[code] ?: return -1f
            return (rate * value).toFloat()
        }

        override fun fetchRate(code: String): Float = rates[code] ?: -1f
    }

    private val testIncome = Income(
            hourValue = 0.0, hourBtc = 0.000534,
            dayValue = 0.0, dayBtc = 0.02323,
            weekValue = 0.0, weekBtc = 0.004544,
            monthValue = 0.0, monthBtc = 0.00343)

    private val rates = mapOf("RUB" to 75.0f, "USD" to 60000.0f)

    // MARK: - Tests

    @Test
    fun stateIsLoadingInitially() {
        val exchange = FakeExchangeServer(rates)
        val repository = FakeMoneyRepository(Single.never())
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        assertTrue(viewModel.state.value is IncomeViewModel.UiState.Loading)
    }

    @Test
    fun fetchIncomeEmitsDataWithConvertedValues() {
        val exchange = FakeExchangeServer(rates)
        val repository = FakeMoneyRepository(Single.just(testIncome))
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")

        val state = viewModel.state.value
        assertTrue(state is IncomeViewModel.UiState.Data)
        state as IncomeViewModel.UiState.Data

        // Rates reported as-is
        assertEquals(75.0f, state.currencyRate, 1e-6f)
        assertEquals(60000.0f, state.usdRate, 1e-6f)

        // Conversions applied to the income model (rate * btc, stored through Float like production)
        assertEquals(75.0 * testIncome.hourBtc, state.income.hourValue, 1e-6)
        assertEquals(75.0 * testIncome.dayBtc, state.income.dayValue, 1e-6)
        assertEquals(75.0 * testIncome.weekBtc, state.income.weekValue, 1e-6)
        assertEquals(75.0 * testIncome.monthBtc, state.income.monthValue, 1e-6)

        // Same btc values passed through unchanged
        assertEquals(testIncome.hourBtc, state.income.hourBtc, 1e-12)
        assertEquals(testIncome.dayBtc, state.income.dayBtc, 1e-12)
        assertEquals(testIncome.weekBtc, state.income.weekBtc, 1e-12)
        assertEquals(testIncome.monthBtc, state.income.monthBtc, 1e-12)

        // Exchange server was asked to convert every period for the target currency
        assertEquals(4, exchange.convertedFor.size)
        assertTrue(exchange.convertedFor.all { it.first == "RUB" })

        // Repository received the miner id
        assertEquals("miner-1", repository.requestedMiner)
    }

    @Test
    fun fetchIncomeFallsBackToNegativeRateWhenRateMissing() {
        // ExchangeServerImpl returns -1f for unknown codes; behavior must be preserved
        // (the raw -1f rate goes through conversion and into the stored value)
        val exchange = FakeExchangeServer(emptyMap())
        val repository = FakeMoneyRepository(Single.just(testIncome))
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")

        val state = viewModel.state.value
        assertTrue(state is IncomeViewModel.UiState.Data)
        state as IncomeViewModel.UiState.Data
        assertEquals(-1f, state.currencyRate, 1e-6f)
        assertEquals(-1f, state.usdRate, 1e-6f)
        assertEquals(-1.0, state.income.dayValue, 1e-9)
    }

    @Test
    fun fetchIncomeEmitsErrorStateOnRepositoryFailure() {
        val exchange = FakeExchangeServer(rates)
        val repository = FakeMoneyRepository(Single.error(RuntimeException("network broken")))
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")

        val state = viewModel.state.value
        assertTrue(state is IncomeViewModel.UiState.Error)
        assertEquals("network broken", (state as IncomeViewModel.UiState.Error).message)
    }

    @Test
    fun fetchIncomeErrorFallsBackToGenericMessageWhenNull() {
        val exchange = FakeExchangeServer(rates)
        val repository = FakeMoneyRepository(Single.error(RuntimeException()))
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")

        val state = viewModel.state.value
        assertTrue(state is IncomeViewModel.UiState.Error)
        assertEquals("Error", (state as IncomeViewModel.UiState.Error).message)
    }

    @Test
    fun fetchIncomeReturnsToDataAfterRefetch() {
        val exchange = FakeExchangeServer(rates)
        val repository = FakeMoneyRepository(Single.error(RuntimeException("first fails")))
        val viewModel = IncomeViewModel(exchangeServer = exchange, repository = repository)

        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")
        assertTrue(viewModel.state.value is IncomeViewModel.UiState.Error)

        repository.result = Single.just(testIncome)
        viewModel.fetchIncome(currencyCode = "USD", currencyValue = "RUB", miner = "miner-1")
        assertTrue(viewModel.state.value is IncomeViewModel.UiState.Data)
    }
}
