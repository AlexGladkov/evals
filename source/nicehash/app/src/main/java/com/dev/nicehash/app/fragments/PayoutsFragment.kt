package com.dev.nicehash.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.adapters.PayoutAdapter
import com.dev.nicehash.app.presenters.PayoutsPresenter
import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.app.views.MainView
import com.dev.nicehash.app.views.PayoutsView
import com.dev.nicehash.base.BaseContainer
import com.dev.nicehash.domain.models.Payout
import com.dev.nicehash.domain.repositories.MinerRepository
import com.dev.nicehash.enums.Keys
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for payouts tab
 */
class PayoutsFragment: BaseContainer(), PayoutsView {
    private val TAG = PayoutsFragment::class.java.simpleName

    // MARK: - Injects
    @Inject lateinit var minerRepository: MinerRepository
    @Inject lateinit var exchangeServer: ExchangeServer

    // MARK: - Presenter setup
    @InjectPresenter
    lateinit var payoutsPresenter: PayoutsPresenter
    @ProvidePresenter
    fun providePresenter(): PayoutsPresenter {
        return PayoutsPresenter(minerRepository = minerRepository, exchangeServer = exchangeServer)
    }

    // MARK: - UI Setup
    private val mAdapter = PayoutAdapter()

    companion object {
        fun getNewInstance(name: String, miner: String, minerID: Int): PayoutsFragment {
            return BaseContainer.getNewInstance(name = name, miner = miner, minerID = minerID)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(fragment = this@PayoutsFragment)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        payoutsPresenter.fetchPayouts(miner = arguments?.getString(Keys.Miner.value).orEmpty(),
                id = arguments?.getInt(Keys.MinerID.value) ?: 0,
                isRefresh = mAdapter.getItems().isEmpty())
    }

    // MARK: - Base implementation
    override fun doubleTap() {

    }

    override fun updateContainer() {

    }

    // MARK: - View implementation
    override fun setupPayouts(data: List<Payout>) {
        if (mAdapter.hasItems) {
            mAdapter.updateItems(itemsList = data)
        } else {
            mAdapter.setList(dataList = data)
        }
    }

    override fun setupBarInfo(paidBTC: Double, usd: Float) {
        (activity as? MainView)?.displayNavigationInfo(leftValue = DecimalFormat("##.######")
                .format(paidBTC), rightValue = DecimalFormat("##.##").format(usd))
    }

    override fun setupNoItems() {

    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun startLoading() {

    }

    override fun endLoading() {

    }
}
