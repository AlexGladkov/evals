package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiExchangeRate;
import com.dev.nicehash.data.room.models.ExchangeRateEntity;
import com.dev.nicehash.domain.models.ExchangeRate;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Converter between api, db, domain for currency
 */
public interface ExchangeRateConverter {
    ExchangeRate dbToModel(ExchangeRateEntity currencyEntity);
    ExchangeRateEntity modelToDB(ExchangeRate exchangeRate);
    ExchangeRateEntity apiToDB(ApiExchangeRate apiExchange);
    ExchangeRate apiToModel(ApiExchangeRate apiExchange);
}
