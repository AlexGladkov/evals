package com.dev.nicehash.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.dev.nicehash.R;
import com.dev.nicehash.app.App;
import com.dev.nicehash.app.presenters.IncomePresenter;
import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.app.views.IncomeView;
import com.dev.nicehash.app.views.MainView;
import com.dev.nicehash.base.BaseContainer;
import com.dev.nicehash.domain.models.Income;
import com.dev.nicehash.enums.Keys;
import com.github.terrakok.cicerone.Navigator;

import java.text.DecimalFormat;

import javax.inject.Inject;

import moxy.presenter.InjectPresenter;
import moxy.presenter.ProvidePresenter;

/**
 * Created by Alex Gladkov on 27.06.18.
 * Fragment for income tab
 */
public class IncomeFragment extends BaseContainer implements IncomeView {

    // MARK: - Presenter setup
    @InjectPresenter
    public IncomePresenter incomePresenter;

    @ProvidePresenter
    public IncomePresenter provideIncomePresenter() {
        return new IncomePresenter(exchangeServer, moneyRepository);
    }

    // MARK: - Injects
    @Inject
    ExchangeServer exchangeServer;
    @Inject
    com.dev.nicehash.domain.repositories.MoneyRepository moneyRepository;

    public static IncomeFragment getNewInstance(String name, String miner, int minerID) {
        IncomeFragment fragment = new IncomeFragment();
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
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        incomePresenter.fetchIncome(ExchangeServerImpl.ExchangeCode.Ru,
                arguments != null && arguments.getString(Keys.Miner.getValue()) != null
                        ? arguments.getString(Keys.Miner.getValue()) : "");
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
    @SuppressLint("SetTextI18n")
    @Override
    public void setupIncome(Income income, float currencyRate, float usdRate) {
//        txtIncomeHourValue.text = DecimalFormat("##.##").format(income.hourValue)
//        txtIncomeHourBtc.text = DecimalFormat("##.########").format(income.hourBtc)
//
//        txtIncomeDayValue.text = DecimalFormat("##.##").format(income.dayValue)
//        txtIncomeDayBtc.text = DecimalFormat("##.########").format(income.dayBtc)
//
//        txtIncomeWeekValue.text = DecimalFormat("##.##").format(income.weekValue)
//        txtIncomeWeekBtc.text = DecimalFormat("##.########").format(income.weekBtc)
//
//        txtIncomeMonthValue.text = DecimalFormat("##.##").format(income.monthValue)
//        txtIncomeMonthBtc.text = DecimalFormat("##.########").format(income.monthBtc)
//
//        txtIncomeBtcUsd.text = " ${DecimalFormat("##.##").format(usdRate)}"
//        txtIncomeCurrency.text = "${getString(R.string.currency_btc)}/${getString(R.string.currency_rub)} = "
//        txtIncomeCurrencyValue.text = DecimalFormat("##.##").format(currencyRate)

        if (getActivity() instanceof MainView) {
            ((MainView) getActivity()).displayNavigationInfo(
                    new DecimalFormat("##.######").format(income.getDayBtc()),
                    new DecimalFormat("##.##").format(income.getDayValue()));
        }
    }
}
