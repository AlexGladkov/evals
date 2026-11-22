package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiExchangeRate;
import com.dev.nicehash.data.room.models.ExchangeRateEntity;
import com.dev.nicehash.domain.models.ExchangeRate;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Implementation for currency converter
 */
public class ExchangeRateConverterImpl implements ExchangeRateConverter {
    @Override
    public ExchangeRateEntity apiToDB(ApiExchangeRate apiExchange) {
        // Kotlin: ExchangeRateEntity(id = null, code = ..., value = ...) -> declared order (id, value, code)
        return new ExchangeRateEntity(null, (double) apiExchange.getValue(), apiExchange.getCode());
    }

    @Override
    public ExchangeRate apiToModel(ApiExchangeRate apiExchange) {
        return new ExchangeRate(apiExchange.getCode(), (double) apiExchange.getValue());
    }

    @Override
    public ExchangeRate dbToModel(ExchangeRateEntity currencyEntity) {
        return new ExchangeRate(currencyEntity.getCode(), currencyEntity.getValue());
    }

    @Override
    public ExchangeRateEntity modelToDB(ExchangeRate exchangeRate) {
        return new ExchangeRateEntity(null, exchangeRate.getValue(), exchangeRate.getCode());
    }
}
