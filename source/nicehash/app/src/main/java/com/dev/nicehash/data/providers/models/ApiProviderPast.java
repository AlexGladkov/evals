package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiProviderPast {
    private final int algo;
    private final List<ApiProviderPastData> data;

    public ApiProviderPast(int algo, List<ApiProviderPastData> data) {
        this.algo = algo;
        this.data = data;
    }

    public int getAlgo() {
        return algo;
    }

    public List<ApiProviderPastData> getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderPast)) return false;
        ApiProviderPast that = (ApiProviderPast) o;
        return algo == that.algo && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algo, data);
    }

    @Override
    public String toString() {
        return "ApiProviderPast(algo=" + algo + ", data=" + data + ")";
    }
}
