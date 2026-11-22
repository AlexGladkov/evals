package com.dev.nicehash.data.providers;

import com.dev.nicehash.data.providers.models.ApiExchangeRate;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Provider for accessing exchange info and other currency operations
 */
public interface CurrencyProvider {
    Single<List<ApiExchangeRate>> fetchExchangeRates();
}
