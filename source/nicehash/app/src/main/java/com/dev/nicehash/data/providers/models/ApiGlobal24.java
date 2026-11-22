package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiGlobal24 {
    private final String method;
    private final List<ApiGlobal24Stats> result;

    public ApiGlobal24(String method, List<ApiGlobal24Stats> result) {
        this.method = method;
        this.result = result;
    }

    public String getMethod() {
        return method;
    }

    public List<ApiGlobal24Stats> getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiGlobal24)) return false;
        ApiGlobal24 that = (ApiGlobal24) o;
        return Objects.equals(method, that.method) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, result);
    }

    @Override
    public String toString() {
        return "ApiGlobal24(method=" + method + ", result=" + result + ")";
    }
}
