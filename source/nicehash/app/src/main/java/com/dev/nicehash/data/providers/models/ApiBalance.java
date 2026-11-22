package com.dev.nicehash.data.providers.models;

import com.google.gson.JsonArray;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Data class for backend models
 */
public class ApiBalance {
    private final double profitability;
    private final JsonArray data;
    private final String name;
    private final String suffix;
    private final int algo;

    public ApiBalance(double profitability, JsonArray data, String name, String suffix, int algo) {
        this.profitability = profitability;
        this.data = data;
        this.name = name;
        this.suffix = suffix;
        this.algo = algo;
    }

    public double getProfitability() {
        return profitability;
    }

    public JsonArray getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getAlgo() {
        return algo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiBalance)) return false;
        ApiBalance that = (ApiBalance) o;
        return Double.compare(that.profitability, profitability) == 0
                && algo == that.algo
                && Objects.equals(data, that.data)
                && Objects.equals(name, that.name)
                && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profitability, data, name, suffix, algo);
    }

    @Override
    public String toString() {
        return "ApiBalance(profitability=" + profitability + ", data=" + data + ", name=" + name
                + ", suffix=" + suffix + ", algo=" + algo + ")";
    }
}
