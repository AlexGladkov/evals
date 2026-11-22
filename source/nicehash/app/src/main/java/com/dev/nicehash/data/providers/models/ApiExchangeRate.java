package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Data class for backend exchange rate
 */
public class ApiExchangeRate {
    private final String code;
    private final float value;
    private final String symbol;

    public ApiExchangeRate(String code, float value, String symbol) {
        this.code = code;
        this.value = value;
        this.symbol = symbol;
    }

    public String getCode() {
        return code;
    }

    public float getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiExchangeRate)) return false;
        ApiExchangeRate that = (ApiExchangeRate) o;
        return Float.compare(that.value, value) == 0
                && Objects.equals(code, that.code)
                && Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, value, symbol);
    }

    @Override
    public String toString() {
        return "ApiExchangeRate(code=" + code + ", value=" + value + ", symbol=" + symbol + ")";
    }
}
