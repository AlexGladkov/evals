package com.dev.nicehash.domain.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Data class for exchange rate model (domain)
 */
public class ExchangeRate {
    private final String code;
    private final double value;

    public ExchangeRate(String code, double value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRate)) return false;
        ExchangeRate that = (ExchangeRate) o;
        return Double.compare(that.value, value) == 0 && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, value);
    }

    @Override
    public String toString() {
        return "ExchangeRate(code=" + code + ", value=" + value + ")";
    }
}
