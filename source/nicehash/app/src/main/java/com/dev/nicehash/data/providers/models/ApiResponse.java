package com.dev.nicehash.data.providers.models;

import com.google.gson.JsonObject;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 05.08.18.
 * General class for all api responses
 */
public class ApiResponse {
    private final JsonObject result;
    private final String method;

    public ApiResponse(JsonObject result, String method) {
        this.result = result;
        this.method = method;
    }

    public JsonObject getResult() {
        return result;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiResponse)) return false;
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(result, that.result) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, method);
    }

    @Override
    public String toString() {
        return "ApiResponse(result=" + result + ", method=" + method + ")";
    }
}
