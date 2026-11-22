package com.dev.nicehash.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.BalanceAdapter;
import com.dev.nicehash.app.presenters.BalancePresenter;
import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.app.views.BalanceView;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.base.BaseAdapterCallback;
import com.dev.nicehash.base.BaseContainer;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.domain.repositories.MoneyRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.helpers.ListConfig;
import com.github.terrakok.cicerone.Navigator;

import java.util.List;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for balance tab
 */
public class BalanceFragment extends BaseContainer implements BalanceView {

    // MARK: - Injects
    @Inject
    MoneyRepository moneyRepository;
    @Inject
    MinerRepository minerRepository;
    @Inject
    ExchangeServer exchangeServer;

    // MARK: - Presenter setup
    @InjectPresenter
    public BalancePresenter balancePresenter;

    @ProvidePresenter
    public BalancePresenter provideBalancePresenter() {
        return new BalancePresenter(minerRepository, exchangeServer,
                moneyRepository, ExchangeServerImpl.ExchangeCode.Ru);
    }

    // MARK: - UI Setup
    private final BalanceAdapter mAdapter = new BalanceAdapter();

    public static BalanceFragment getNewInstance(String name, String miner, int minerID) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putString(Keys.Name.getValue(), name);
        args.putString(Keys.Miner.getValue(), miner);
        args.putInt(Keys.MinerID.getValue(), minerID);

        fragment.setArguments(args);
        return fragment;
    }

    private Navigator navigator = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        mAdapter.attachCallback(new BaseAdapterCallback<Balance>() {
            @Override
            public void onItemClick(Balance model, View view) {
//                (activity as? RouterProvider)?.getRouter()?.replaceScreen(ScreenKeys.Detail.value, model)
            }

            @Override
            public boolean onLongClick(Balance model, View view) {
                return true;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        android.content.Context context = getContext();
        if (context != null) {
            ListConfig listConfig = new ListConfig.Builder(mAdapter)
                    .setHasFixedSize(true)
                    .setHasNestedScroll(true)
                    .build(context);
//            listConfig.applyConfig(it, recyclerBalance)
        }

        Bundle arguments = getArguments();
        balancePresenter.fetchUnpaid(arguments != null && arguments.getString(Keys.Miner.getValue()) != null
                        ? arguments.getString(Keys.Miner.getValue()) : "",
                arguments != null ? arguments.getInt(Keys.MinerID.getValue(), -1) : -1);
        balancePresenter.fetchBalance(arguments != null && arguments.getString(Keys.Miner.getValue()) != null
                        ? arguments.getString(Keys.Miner.getValue()) : "",
                arguments != null ? arguments.getInt(Keys.MinerID.getValue(), -1) : -1,
                mAdapter.getItems().isEmpty());
    }

    @Override
    public Navigator getNavigator() {
//        return if (navigator == null) {
//            navigator = object: SupportAppNavigator(activity, childFragmentManager, R.id.container) {
//                override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
//                    return when (screenKey) {
//                        else -> null
//                    }
//                }
//
//                override fun createFragment(screenKey: String, data: Any?): Fragment? {
//                    return when (screenKey) {
//                        else -> null
//                    }
//                }
//
//                override fun exit() {
//                    super.exit()
//                    activity?.let { (it as RouterProvider).getRouter().exit() }
//                }
//            }
//
//            navigator as SupportAppNavigator
//        } else {
//            navigator!!
//        }

        return null;
    }

    // MARK: - Base implementation
    @Override
    public void doubleTap() {

    }

    @Override
    public void updateContainer() {

    }

    // MARK: - View implementation
    @Override
    public void setupBalance(List<Balance> data) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(data);
        } else {
            mAdapter.setList(data);
        }

//        recyclerBalance.visibility = View.VISIBLE
//        txtBalanceNoItems.visibility = View.GONE
    }

    @Override
    public void setupBarItems(String unpaid, String usd) {
        if (getActivity() instanceof MainView) {
            ((MainView) getActivity()).displayNavigationInfo(unpaid, usd);
        }
    }

    @Override
    public void startLoading() {
//        cpvBalance.visibility = View.VISIBLE
//        recyclerBalance.visibility = View.GONE
//        txtBalanceNoItems.visibility = View.GONE
    }

    @Override
    public void endLoading() {
//        cpvBalance.visibility = View.GONE
    }

    @Override
    public void showNoItems() {
//        recyclerBalance.visibility = View.GONE
//        txtBalanceNoItems.visibility = View.VISIBLE
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}
