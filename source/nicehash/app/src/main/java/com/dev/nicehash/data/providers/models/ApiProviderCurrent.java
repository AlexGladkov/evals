package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiProviderCurrent {
    private final double profitability;
    private final String suffix;
    private final String name;
    private final int algo;
    private final ApiProviderData data;

    public ApiProviderCurrent(double profitability, String suffix, String name, int algo,
                              ApiProviderData data) {
        this.profitability = profitability;
        this.suffix = suffix;
        this.name = name;
        this.algo = algo;
        this.data = data;
    }

    public double getProfitability() {
        return profitability;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getName() {
        return name;
    }

    public int getAlgo() {
        return algo;
    }

    public ApiProviderData getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderCurrent)) return false;
        ApiProviderCurrent that = (ApiProviderCurrent) o;
        return Double.compare(that.profitability, profitability) == 0
                && algo == that.algo
                && Objects.equals(suffix, that.suffix)
                && Objects.equals(name, that.name)
                && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profitability, suffix, name, algo, data);
    }

    @Override
    public String toString() {
        return "ApiProviderCurrent(profitability=" + profitability + ", suffix=" + suffix
                + ", name=" + name + ", algo=" + algo + ", data=" + data + ")";
    }
}
