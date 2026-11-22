package com.dev.nicehash.app.presenters;

import android.annotation.SuppressLint;

import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.app.views.IncomeView;
import com.dev.nicehash.domain.repositories.MoneyRepository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moxy.InjectViewState;
import moxy.MvpPresenter;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Presenter for income tab
 */
@InjectViewState
public class IncomePresenter extends MvpPresenter<IncomeView> {

    private final ExchangeServer exchangeServer;
    private final MoneyRepository repository;

    public IncomePresenter(ExchangeServer exchangeServer, MoneyRepository repository) {
        this.exchangeServer = exchangeServer;
        this.repository = repository;
    }

    @SuppressLint("CheckResult")
    public void fetchIncome(ExchangeServerImpl.ExchangeCode currency, String miner) {
        repository.fetchMinerIncome(miner)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    result.setMonthValue(exchangeServer.convertBtcFor(currency.getValue(), result.getMonthBtc()));
                    result.setWeekValue(exchangeServer.convertBtcFor(currency.getValue(), result.getWeekBtc()));
                    result.setDayValue(exchangeServer.convertBtcFor(currency.getValue(), result.getDayBtc()));
                    result.setHourValue(exchangeServer.convertBtcFor(currency.getValue(), result.getHourBtc()));
                    getViewState().setupIncome(result,
                            exchangeServer.fetchRate(currency.getValue()),
                            exchangeServer.fetchRate(ExchangeServerImpl.ExchangeCode.US.getValue()));
                }, error -> error.printStackTrace());
    }
}
