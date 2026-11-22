package com.dev.nicehash.data.implementations;

import android.util.Log;

import com.dev.nicehash.data.converters.ExchangeRateConverter;
import com.dev.nicehash.data.providers.CurrencyProvider;
import com.dev.nicehash.data.room.RoomAppDataSource;
import com.dev.nicehash.domain.models.ExchangeRate;
import com.dev.nicehash.domain.repositories.CurrencyRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Implementation for currency repository
 */
public class CurrencyRepositoryImpl implements CurrencyRepository {
    private final String TAG = CurrencyRepositoryImpl.class.getSimpleName();

    private final ExchangeRateConverter exchangeRateConverter;
    private final CurrencyProvider exchangeRateProvider;
    private final RoomAppDataSource appDataSource;

    public CurrencyRepositoryImpl(ExchangeRateConverter exchangeRateConverter,
                                  CurrencyProvider exchangeRateProvider,
                                  RoomAppDataSource appDataSource) {
        this.exchangeRateConverter = exchangeRateConverter;
        this.exchangeRateProvider = exchangeRateProvider;
        this.appDataSource = appDataSource;
    }

    @Override
    public Single<List<ExchangeRate>> fetchExchangeRatesFromDB() {
        return Single.create(subscriber -> {
            List<ExchangeRate> exchanges = new ArrayList<>();
            for (com.dev.nicehash.data.room.models.ExchangeRateEntity entity
                    : appDataSource.getCurrencyDao().fetchCurrencies()) {
                exchanges.add(exchangeRateConverter.dbToModel(entity));
            }

            if (!exchanges.isEmpty()) {
                subscriber.onSuccess(exchanges);
            } else {
                subscriber.onError(new NullPointerException());
            }
        });
    }

    @Override
    public Single<List<ExchangeRate>> fetchExchangeRates() {
        return Single.create(subscriber -> {
            exchangeRateProvider.fetchExchangeRates()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> {
                        List<ExchangeRate> converted = new ArrayList<>();
                        for (com.dev.nicehash.data.providers.models.ApiExchangeRate apiExchange : response) {
                            converted.add(exchangeRateConverter.apiToModel(apiExchange));
                        }
                        subscriber.onSuccess(converted);

                        for (com.dev.nicehash.data.providers.models.ApiExchangeRate apiExchange : response) {
                            new Thread(() -> appDataSource.getCurrencyDao().updateCurrency(
                                    exchangeRateConverter.apiToDB(apiExchange))).start();
                        }
                    }, error -> subscriber.onError(error));
        });
    }

    @Override
    public Single<Boolean> updateExchangeRate(ExchangeRate exchangeRate) {
        return Single.just(true);
    }
}
