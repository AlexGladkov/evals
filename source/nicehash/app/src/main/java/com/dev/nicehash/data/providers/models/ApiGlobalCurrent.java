package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Models for global current api request
 */
public class ApiGlobalCurrent {
    private final String method;
    private final List<ApiGlobalCurrentStats> result;

    public ApiGlobalCurrent(String method, List<ApiGlobalCurrentStats> result) {
        this.method = method;
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public List<ApiGlobalCurrentStats> getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiGlobalCurrent)) return false;
        ApiGlobalCurrent that = (ApiGlobalCurrent) o;
        return Objects.equals(method, that.method) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, result);
    }

    @Override
    public String toString() {
        return "ApiGlobalCurrent(method=" + method + ", result=" + result + ")";
    }
}
