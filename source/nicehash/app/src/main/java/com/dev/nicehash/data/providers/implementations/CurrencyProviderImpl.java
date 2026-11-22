package com.dev.nicehash.data.providers.implementations;

import android.util.Log;

import com.dev.nicehash.data.providers.CurrencyProvider;
import com.dev.nicehash.data.providers.models.ApiExchangeRate;
import com.dev.nicehash.data.providers.services.RemoteCurrencyService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Implementation for currency provider
 */
public class CurrencyProviderImpl implements CurrencyProvider {
    private final String TAG = CurrencyProviderImpl.class.getSimpleName();

    private final RemoteCurrencyService remoteCurrencyService;

    public CurrencyProviderImpl(RemoteCurrencyService remoteCurrencyService) {
        this.remoteCurrencyService = remoteCurrencyService;
    }

    @Override
    public Single<List<ApiExchangeRate>> fetchExchangeRates() {
        return remoteCurrencyService.fetchExchangeRate()
                .flatMap(it -> {
                    List<ApiExchangeRate> converted = new LinkedList<>();
                    for (String key : it.keySet()) {
                        converted.add(new ApiExchangeRate(key,
                                it.get(key).getAsJsonObject().get("15m").getAsFloat(),
                                it.get(key).getAsJsonObject().get("symbol").getAsString()));
                    }

                    return Single.just(converted);
                });
    }
}
