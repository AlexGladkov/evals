package com.dev.nicehash.domain.repositories;

import com.dev.nicehash.domain.models.ExchangeRate;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Repository for Currency API and DB
 */
public interface CurrencyRepository {
    Single<List<ExchangeRate>> fetchExchangeRates();
    Single<List<ExchangeRate>> fetchExchangeRatesFromDB();
    Single<Boolean> updateExchangeRate(ExchangeRate exchangeRate);
}
