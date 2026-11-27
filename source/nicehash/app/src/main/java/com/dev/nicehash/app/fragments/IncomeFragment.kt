package com.dev.nicehash.app.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.dev.nicehash.R
import com.dev.nicehash.app.App
import com.dev.nicehash.app.compose.IncomeScreen
import com.dev.nicehash.app.interfaces.RouterProvider
import com.dev.nicehash.app.servers.ExchangeCode
import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.app.servers.ExchangeServerImpl
import com.dev.nicehash.app.viewmodels.IncomeViewModel
import com.dev.nicehash.app.views.MainView
import com.dev.nicehash.base.BaseContainer
import com.dev.nicehash.domain.models.Income
import com.dev.nicehash.domain.repositories.MoneyRepository
import com.dev.nicehash.enums.Keys
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for income tab.
 * Migrated from Moxy presenter + XML to ViewModel + Jetpack Compose
 * (IncomeViewModel + IncomeScreen, following the AboutActivity/AboutScreen pattern).
 */
class IncomeFragment : BaseContainer() {

    // MARK: - Injects
    @Inject lateinit var exchangeServer: ExchangeServer
    @Inject lateinit var moneyRepository: MoneyRepository

    // MARK: - ViewModel setup (manual factory — dependencies come from Dagger fields)
    private val incomeViewModel: IncomeViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return IncomeViewModel(exchangeServer = exchangeServer,
                        repository = moneyRepository) as T
            }
        }).get(IncomeViewModel::class.java)
    }

    companion object {
        fun getNewInstance(name: String, miner: String, minerID: Int): IncomeFragment {
            val fragment = IncomeFragment()
            val args = Bundle()
            args.putString(Keys.Name.value, name)
            args.putString(Keys.Miner.value, miner)
            args.putInt(Keys.MinerID.value, minerID)

            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(fragment = this@IncomeFragment)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Use ComposeView with late content setting to avoid ViewTreeLifecycleOwner crash
        // (same approach as AboutActivity).
        installComposeLifecycleBridge()
        return ComposeView(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view as ComposeView).setContent {
            IncomeScreen(state = incomeViewModel.state,
                    accentColor = resolveThemeAccentColor(),
                    isDark = App.isDark,
                    onIncomeLoaded = { income -> onIncomeLoaded(income) })
        }

        incomeViewModel.fetchIncome(currencyCode = ExchangeCode.US.value,
                currencyValue = ExchangeCode.Ru.value,
                miner = arguments?.getString(Keys.Miner.value).orEmpty())
    }

    // MARK: - Base implementation
    override fun doubleTap() {

    }

    override fun updateContainer() {

    }

    // MARK: - View implementation
    private fun onIncomeLoaded(income: Income) {
        (activity as? MainView)?.displayNavigationInfo(leftValue = DecimalFormat("##.######").format(income.dayBtc),
                rightValue = DecimalFormat("##.##").format(income.dayValue))
    }

    private fun resolveThemeAccentColor(): Int {
        val typedValue = TypedValue()
        return if (requireContext().theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)) {
            typedValue.data
        } else {
            0
        }
    }

    /**
     * Compose requires an androidx ViewTreeLifecycleOwner on the window root to create
     * the window Recomposer. The host activity is a support-library AppCompatActivity
     * remapped by the Jetifier to androidx.appcompat 1.0.0, whose base classes do not
     * install ViewTree owners, so install a bridge lifecycle owner on the window decor
     * root, mirroring the host activity lifecycle.
     */
    private fun installComposeLifecycleBridge() {
        val hostActivity = activity ?: return
        val windowRoot = hostActivity.window?.decorView ?: return
        if (windowRoot.findViewTreeLifecycleOwner() != null) return

        ViewTreeBridgeLifecycleOwner(hostActivity).also { bridge ->
            windowRoot.setViewTreeLifecycleOwner(bridge)
        }
    }

    /**
     * androidx LifecycleOwner bridged from the host activity's lifecycle.
     * Syncs to the current state first: the fragment view can be created while the
     * activity is already resumed (tab re-attachment), so no later events arrive for it.
     */
    private class ViewTreeBridgeLifecycleOwner(source: LifecycleOwner) : LifecycleOwner {
        private val registry = LifecycleRegistry(this)

        init {
            when (source.lifecycle.currentState) {
                Lifecycle.State.INITIALIZED -> Unit
                Lifecycle.State.CREATED -> registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                Lifecycle.State.STARTED -> {
                    registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                    registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                }
                Lifecycle.State.RESUMED -> {
                    registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
                    registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
                    registry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
                }
                Lifecycle.State.DESTROYED -> registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            }

            source.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    registry.handleLifecycleEvent(event)
                }
            })
        }

        override val lifecycle: Lifecycle
            get() = registry
    }
}
