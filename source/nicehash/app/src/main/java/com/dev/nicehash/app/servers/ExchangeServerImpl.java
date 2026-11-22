package com.dev.nicehash.app.servers;

import android.util.Log;

import com.dev.nicehash.domain.models.ExchangeRate;
import com.dev.nicehash.domain.repositories.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 13.08.18.
 * Implementation for ExchangeServer
 */
@Singleton
public class ExchangeServerImpl implements ExchangeServer {
    private final String TAG = ExchangeServerImpl.class.getSimpleName();

    public enum ExchangeCode {
        Ru("RUB"), US("USD");

        private final String value;

        ExchangeCode(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static final List<ExchangeRate> data = new ArrayList<>();

    private final CurrencyRepository currencyRepository;

    public ExchangeServerImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;

        currencyRepository.fetchExchangeRatesFromDB()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(it -> {
                    data.addAll(it);

                    fetchExchange();
                }, throwable -> fetchExchange());
    }

    private void fetchExchange() {
        currencyRepository.fetchExchangeRates()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    data.clear();
                    data.addAll(response);
                }, throwable -> {
                });
    }

    @Override
    public float convertBtcFor(String code, double value) {
        ExchangeRate rate = null;
        for (ExchangeRate item : data) {
            if (item.getCode().equals(code)) {
                rate = item;
                break;
            }
        }

        if (rate == null) {
            return -1f;
        } else {
            return (float) (rate.getValue() * value);
        }
    }

    @Override
    public float fetchRate(String code) {
        ExchangeRate rate = null;
        for (ExchangeRate item : data) {
            if (item.getCode().equals(code)) {
                rate = item;
                break;
            }
        }

        return rate != null ? (float) rate.getValue() : -1f;
    }
}
