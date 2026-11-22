package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiProviderEx {
    private final String method;
    private final List<ApiProviderCurrent> current;
    private final List<ApiProviderPast> past;
    private final List<ApiPayment> payments;
    private final String addr;

    public ApiProviderEx(String method, List<ApiProviderCurrent> current, List<ApiProviderPast> past,
                         List<ApiPayment> payments, String addr) {
        this.method = method;
        this.current = current;
        this.past = past;
        this.payments = payments;
        this.addr = addr;
    }

    public String getMethod() {
        return method;
    }

    public List<ApiProviderCurrent> getCurrent() {
        return current;
    }

    public List<ApiProviderPast> getPast() {
        return past;
    }

    public List<ApiPayment> getPayments() {
        return payments;
    }

    public String getAddr() {
        return addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderEx)) return false;
        ApiProviderEx that = (ApiProviderEx) o;
        return Objects.equals(method, that.method)
                && Objects.equals(current, that.current)
                && Objects.equals(past, that.past)
                && Objects.equals(payments, that.payments)
                && Objects.equals(addr, that.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, current, past, payments, addr);
    }

    @Override
    public String toString() {
        return "ApiProviderEx(method=" + method + ", current=" + current + ", past=" + past
                + ", payments=" + payments + ", addr=" + addr + ")";
    }
}
