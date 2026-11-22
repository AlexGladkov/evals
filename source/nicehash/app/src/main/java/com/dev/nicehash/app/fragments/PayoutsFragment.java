package com.dev.nicehash.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.adapters.PayoutAdapter;
import com.dev.nicehash.app.presenters.PayoutsPresenter;
import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.app.views.PayoutsView;
import com.dev.nicehash.base.BaseContainer;
import com.dev.nicehash.domain.models.Payout;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.enums.Keys;
import com.dev.nicehash.helpers.ListConfig;
import com.github.terrakok.cicerone.Navigator;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for payouts tab
 */
public class PayoutsFragment extends BaseContainer implements PayoutsView {
    private final String TAG = PayoutsFragment.class.getSimpleName();

    // MARK: - Injects
    @Inject
    MinerRepository minerRepository;
    @Inject
    ExchangeServer exchangeServer;

    // MARK: - Presenter setup
    @InjectPresenter
    public PayoutsPresenter payoutsPresenter;

    @ProvidePresenter
    public PayoutsPresenter providePresenter() {
        return new PayoutsPresenter(minerRepository, exchangeServer);
    }

    // MARK: - UI Setup
    private final PayoutAdapter mAdapter = new PayoutAdapter();

    public static PayoutsFragment getNewInstance(String name, String miner, int minerID) {
        PayoutsFragment fragment = new PayoutsFragment();
        Bundle args = new Bundle();
        args.putString(Keys.Name.getValue(), name);
        args.putString(Keys.Miner.getValue(), miner);
        args.putInt(Keys.MinerID.getValue(), minerID);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        App.appComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    private Navigator navigator = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payouts, container, false);
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
//            listConfig.applyConfig(it, recyclerPayouts)
        }

        Bundle arguments = getArguments();
        payoutsPresenter.fetchPayouts(arguments != null && arguments.getString(Keys.Miner.getValue()) != null
                        ? arguments.getString(Keys.Miner.getValue()) : "",
                arguments != null ? arguments.getInt(Keys.MinerID.getValue(), 0) : 0,
                mAdapter.getItems().isEmpty());
    }

    @Override
    public Navigator getNavigator() {
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
    public void setupPayouts(List<Payout> data) {
        if (mAdapter.hasItems()) {
            mAdapter.updateItems(data);
        } else {
            mAdapter.setList(data);
        }

//        recyclerPayouts.visibility = View.VISIBLE
//        txtPayoutsNoItems.visibility = View.GONE
    }

    @Override
    public void setupBarInfo(double paidBTC, float usd) {
        if (getActivity() instanceof MainView) {
            ((MainView) getActivity()).displayNavigationInfo(
                    new DecimalFormat("##.######").format(paidBTC),
                    new DecimalFormat("##.##").format(usd));
        }
    }

    @Override
    public void setupNoItems() {
//        txtPayoutsNoItems.visibility = View.VISIBLE
//        recyclerPayouts.visibility = View.GONE
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void startLoading() {
//        cpvPayouts.visibility = View.VISIBLE
//        recyclerPayouts.visibility = View.GONE
//        txtPayoutsNoItems.visibility = View.GONE
    }

    @Override
    public void endLoading() {
//        cpvPayouts.visibility = View.GONE
    }
}
