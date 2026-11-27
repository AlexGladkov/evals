package com.dev.nicehash.app.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel for income tab.
 * Replaces the former Moxy IncomePresenter, keeping identical behavior
 * (same repository call, same exchange conversions, same rates).
 */
class IncomeViewModel(
    private val exchangeServer: ExchangeServer,
    private val repository: MoneyRepository
) : ViewModel() {

    sealed class UiState {
        object Loading : UiState()
        data class Data(val income: Income, val currencyRate: Float, val usdRate: Float) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state: StateFlow<UiState> = _state.asStateFlow()

    @SuppressLint("CheckResult")
    fun fetchIncome(currencyCode: String, currencyValue: String, miner: String) {
        repository.fetchMinerIncome(miner = miner)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    // Apply exchange conversion (same as the former presenter)
                    result.monthValue = exchangeServer.convertBtcFor(code = currencyValue, value = result.monthBtc).toDouble()
                    result.weekValue = exchangeServer.convertBtcFor(code = currencyValue, value = result.weekBtc).toDouble()
                    result.dayValue = exchangeServer.convertBtcFor(code = currencyValue, value = result.dayBtc).toDouble()
                    result.hourValue = exchangeServer.convertBtcFor(code = currencyValue, value = result.hourBtc).toDouble()
                    _state.value = UiState.Data(income = result,
                            currencyRate = exchangeServer.fetchRate(code = currencyValue),
                            usdRate = exchangeServer.fetchRate(code = currencyCode))
                }, { error ->
                    error.printStackTrace()
                    _state.value = UiState.Error(message = error.localizedMessage ?: "Error")
                })
    }
}
