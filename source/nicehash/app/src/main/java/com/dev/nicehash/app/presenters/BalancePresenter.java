package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;

import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.app.views.BalanceView;
import com.dev.nicehash.domain.models.Balance;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.domain.repositories.MoneyRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Presenter for Balance tab
 */
@InjectViewState
public class BalancePresenter extends MvpPresenter<BalanceView> {
    private final String TAG = BalancePresenter.class.getSimpleName();

    private final MinerRepository minerRepository;
    private final ExchangeServer exchangeServer;
    private final MoneyRepository moneyRepository;
    private final ExchangeServerImpl.ExchangeCode currency;

    public BalancePresenter(MinerRepository minerRepository, ExchangeServer exchangeServer,
                            MoneyRepository moneyRepository, ExchangeServerImpl.ExchangeCode currency) {
        this.minerRepository = minerRepository;
        this.exchangeServer = exchangeServer;
        this.moneyRepository = moneyRepository;
        this.currency = currency;
    }

    @SuppressLint("CheckResult")
    public void fetchUnpaid(String miner, int id) {
        moneyRepository.fetchUnpaidMoney(miner)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(unpaid -> {
                    float usd = exchangeServer.convertBtcFor(ExchangeServerImpl.ExchangeCode.US.getValue(), unpaid);
                    getViewState().setupBarItems(new DecimalFormat("##.######").format(unpaid),
                            new DecimalFormat("##.##").format(usd));
                }, error -> error.printStackTrace());
    }

    @SuppressLint("CheckResult")
    public void fetchBalance(String miner, int id, boolean isRefresh) {
        if (id < 0) {
            ArrayList<Balance> array = new ArrayList<>();
            // Balance ctor declared order: (id, title, value, btc, incomeValue, incomeBtc, suffix)
            array.add(new Balance(0, "DaggerHashimoto",
                    exchangeServer.convertBtcFor(currency.getValue(), 0.00000025),
                    0.00000025,
                    exchangeServer.convertBtcFor(currency.getValue(), 0.00005),
                    0.00005,
                    "Mh"));
            array.add(new Balance(0, "Equihash", exchangeServer.convertBtcFor(currency.getValue(), 0.000004256),
                    0.000004256,
                    exchangeServer.convertBtcFor(currency.getValue(), 0.0017),
                    0.0017,
                    "Mh"));
            array.add(new Balance(0, "Pascal", exchangeServer.convertBtcFor(currency.getValue(), 0.000000078),
                    0.000000078,
                    exchangeServer.convertBtcFor(currency.getValue(), 0.0003252),
                    0.0003252,
                    "Mh"));
            getViewState().setupBalance(array);
        } else {
            if (isRefresh) getViewState().startLoading();
            minerRepository.fetchMinerBalance(miner)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        List<Balance> result = new ArrayList<>();
                        for (Balance item : response) {
                            item.setIncomeValue(exchangeServer.convertBtcFor(currency.getValue(), item.getIncomeBtc()));
                            item.setValue(exchangeServer.convertBtcFor(currency.getValue(), item.getBtc()));
                            result.add(item);
                        }

                        if (result.isEmpty()) {
                            getViewState().showNoItems();
                        } else {
                            getViewState().setupBalance(result);
                        }

                        getViewState().endLoading();
                    }, error -> {
                        getViewState().showError(error.getLocalizedMessage());
                        getViewState().endLoading();
                    });
        }
    }
}
