package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Models for stats provider request
 */
public class ApiProvider {
    private final String method;
    private final List<ApiProviderStats> result;

    public ApiProvider(String method, List<ApiProviderStats> result) {
        this.method = method;
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public List<ApiProviderStats> getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProvider)) return false;
        ApiProvider that = (ApiProvider) o;
        return Objects.equals(method, that.method) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, result);
    }

    @Override
    public String toString() {
        return "ApiProvider(method=" + method + ", result=" + result + ")";
    }
}
