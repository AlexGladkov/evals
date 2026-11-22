package com.dev.nicehash.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.adapters.MarketAdapter
import com.dev.nicehash.app.presenters.MarketPresenter
import com.dev.nicehash.app.views.MainView
import com.dev.nicehash.app.views.MarketView
import com.dev.nicehash.base.BaseContainer
import com.dev.nicehash.domain.models.HashrateOrder
import com.dev.nicehash.domain.repositories.MarketRepository
import com.dev.nicehash.enums.Keys
import com.dev.nicehash.helpers.ListConfig
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.github.terrakok.cicerone.Navigator
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject

/**
 * Fragment for marketplace tab (hashrate orders)
 */
class MarketFragment: BaseContainer(), MarketView {
    private val TAG = MarketFragment::class.java.simpleName

    // MARK: - Injects
    @Inject lateinit var marketRepository: MarketRepository

    // MARK: - Presenter setup
    @InjectPresenter
    lateinit var marketPresenter: MarketPresenter
    @ProvidePresenter
    fun provideMarketPresenter(): MarketPresenter {
        return MarketPresenter(repository = marketRepository)
    }

    // MARK: - UI Setup
    private val mAdapter = MarketAdapter()
    private var recyclerMarket: RecyclerView? = null
    private var cpvMarket: CircularProgressView? = null
    private var txtMarketStatus: TextView? = null

    companion object {
        fun getNewInstance(name: String): MarketFragment {
            val fragment = MarketFragment()
            val args = Bundle()
            args.putString(Keys.Name.value, name)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(fragment = this@MarketFragment)
        super.onCreate(savedInstanceState)
    }

    private var navigator: Navigator? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerMarket = view.findViewById(R.id.recyclerMarket)
        cpvMarket = view.findViewById(R.id.cpvMarket)
        txtMarketStatus = view.findViewById(R.id.txtMarketStatus)

        context?.let {
            val listConfig = ListConfig.Builder(mAdapter)
                    .setHasFixedSize(true)
                    .setHasNestedScroll(true)
                    .build(it)

            listConfig.applyConfig(it, recyclerMarket!!)
        }

        marketPresenter.fetchOrders()

        (activity as? MainView)?.displayNavigationInfo(leftValue = "", rightValue = "")
    }

    override fun getNavigator(): Navigator? {
        return null
    }

    // MARK: - Base implementation
    override fun doubleTap() {

    }

    override fun updateContainer() {

    }

    // MARK: - View implementation
    override fun showLoading() {
        cpvMarket?.visibility = View.VISIBLE
        recyclerMarket?.visibility = View.GONE
        txtMarketStatus?.visibility = View.GONE
    }

    override fun showOrders(orders: List<HashrateOrder>) {
        cpvMarket?.visibility = View.GONE
        txtMarketStatus?.visibility = View.GONE
        recyclerMarket?.visibility = View.VISIBLE

        if (mAdapter.hasItems) {
            mAdapter.updateItems(itemsList = orders)
        } else {
            mAdapter.setList(dataList = orders)
        }
    }

    override fun showError(message: String) {
        cpvMarket?.visibility = View.GONE
        recyclerMarket?.visibility = View.GONE
        txtMarketStatus?.visibility = View.VISIBLE
        txtMarketStatus?.text = message
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun showEmpty() {
        cpvMarket?.visibility = View.GONE
        recyclerMarket?.visibility = View.GONE
        txtMarketStatus?.visibility = View.VISIBLE
        txtMarketStatus?.text = getString(R.string.market_no_orders)
    }
}
