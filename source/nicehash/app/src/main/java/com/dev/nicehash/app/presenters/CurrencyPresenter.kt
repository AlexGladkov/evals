package com.dev.nicehash.app.presenters

import android.annotation.SuppressLint
import android.util.Log
import com.dev.nicehash.R
import com.dev.nicehash.app.views.CurrencyView
import com.dev.nicehash.domain.models.Currency
import com.dev.nicehash.domain.repositories.ConfigurationRepository
import com.dev.nicehash.helpers.CurrencyResources
import com.dev.nicehash.helpers.EnumCollections
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.LinkedList

/**
 * Created by Alex Gladkov on 25.07.18.
 * Presenter for currency activity (domain level)
 * @see .app.activities.CurrencyActivity
 */
@InjectViewState
class CurrencyPresenter(val configurationRepository: ConfigurationRepository): MvpPresenter<CurrencyView>() {
    private val TAG = CurrencyPresenter::class.java.simpleName

    fun fetchCurrencies() {
        val data = LinkedList<Currency>()

        data.add(Currency(id = EnumCollections.Currency.Russian.id,
                title = CurrencyResources.titleRes(EnumCollections.Currency.Russian), header = R.string.currency_base,
                btc = "413.558,23", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Russian), prototype = EnumCollections.Currency.Russian))

        data.add(Currency(id = EnumCollections.Currency.Euro.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Euro),
                header = R.string.currency_popular, btc = "5,664", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Euro),
                prototype = EnumCollections.Currency.Euro))
        data.add(Currency(id = EnumCollections.Currency.BritainPound.id, imgRes = CurrencyResources.flagRes(EnumCollections.Currency.BritainPound),
                title = CurrencyResources.titleRes(EnumCollections.Currency.BritainPound), header = -1, btc = "5,104",
                prototype = EnumCollections.Currency.BritainPound))
        data.add(Currency(id = EnumCollections.Currency.USD.id, imgRes = CurrencyResources.flagRes(EnumCollections.Currency.USD),
                title = CurrencyResources.titleRes(EnumCollections.Currency.USD), header = -1, btc = "7,188",
                prototype = EnumCollections.Currency.USD))

        data.add(Currency(id = EnumCollections.Currency.Australian.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Australian),
                header = R.string.currency_all, btc = "9,344", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Australian),
                prototype = EnumCollections.Currency.Australian))
        data.add(Currency(id = EnumCollections.Currency.Belgium.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Belgium),
                header = -1, btc = "11,404", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Belgium),
                prototype = EnumCollections.Currency.Belgium))
        data.add(Currency(id = EnumCollections.Currency.Canada.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Canada),
                header = -1, btc = "9,273", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Canada),
                prototype = EnumCollections.Currency.Canada))
        data.add(Currency(id = EnumCollections.Currency.CHF.id, title = CurrencyResources.titleRes(EnumCollections.Currency.CHF),
                header = -1, btc = "6,874", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.CHF),
                prototype = EnumCollections.Currency.CHF))
        data.add(Currency(id = EnumCollections.Currency.China.id, title = CurrencyResources.titleRes(EnumCollections.Currency.China),
                header = -1, btc = "45,273", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.China),
                prototype = EnumCollections.Currency.China))
        data.add(Currency(id = EnumCollections.Currency.Czech.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Czech),
                header = -1, btc = "148,404", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Czech),
                prototype = EnumCollections.Currency.Czech))
        data.add(Currency(id = EnumCollections.Currency.Dania.id, title = CurrencyResources.titleRes(EnumCollections.Currency.Dania),
                header = -1, btc = "43,480.21", imgRes = CurrencyResources.flagRes(EnumCollections.Currency.Dania),
                prototype = EnumCollections.Currency.Dania))

        viewState.setupView(data = data)
    }

    @SuppressLint("CheckResult")
    fun setCurrency(currency: Currency) {
        configurationRepository.fetchConfiguration()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { config ->
                    config.currency = currency.prototype
                    configurationRepository.updateConfiguration(configuration = config)
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.e(TAG, "configuration updated")
                            }, { error ->
                                Log.e(TAG, "error updating ${error.localizedMessage}")
                            })
                }
    }
}