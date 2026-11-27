package com.dev.nicehash.app.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.presenters.IncomePresenter
import com.dev.nicehash.app.servers.ExchangeCode
import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.app.views.IncomeView
import com.dev.nicehash.app.views.MainView
import com.dev.nicehash.base.BaseContainer
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import com.dev.nicehash.enums.Keys
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for income tab
 */
class IncomeFragment: BaseContainer(), IncomeView {

    // MARK: - Presenter setup
    @InjectPresenter
    lateinit var incomePresenter: IncomePresenter
    @ProvidePresenter
    fun provideIncomePresenter(): IncomePresenter {
        return IncomePresenter(exchangeServer = exchangeServer,
                repository = moneyRepository)
    }

    // MARK: - Injects
    @Inject lateinit var exchangeServer: ExchangeServer
    @Inject lateinit var moneyRepository: MoneyRepository

    companion object {
        fun getNewInstance(name: String, miner: String, minerID: Int): IncomeFragment {
            return BaseContainer.getNewInstance(name = name, miner = miner, minerID = minerID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(fragment = this@IncomeFragment)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomePresenter.fetchIncome(currency = ExchangeCode.Ru,
                miner = arguments?.getString(Keys.Miner.value).orEmpty())
    }

    // MARK: - Base implementation
    override fun doubleTap() {

    }

    override fun updateContainer() {

    }

    // MARK: - View implementation
    @SuppressLint("SetTextI18n")
    override fun setupIncome(income: Income, currencyRate: Float, usdRate: Float) {
        (activity as? MainView)?.displayNavigationInfo(leftValue = DecimalFormat("##.######").format(income.dayBtc),
                rightValue = DecimalFormat("##.##").format(income.dayValue))
    }
}
