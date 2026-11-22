package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.providers.models.ApiBalance;
import com.dev.nicehash.domain.models.Balance;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Implementation for Balance Converter
 */
public class BalanceConverterImpl implements BalanceConverter {

    @Override
    public Balance apiToModel(ApiBalance apiBalance) {
        // Kotlin named args ported positionally: id = 0, title = name, value = 0f,
        // btc = data[1], incomeValue = 0f, incomeBtc = profitability, suffix = suffix
        return new Balance(0, apiBalance.getName(), 0f, apiBalance.getData().get(1).getAsDouble(),
                0f, apiBalance.getProfitability(), apiBalance.getSuffix());
    }
}
