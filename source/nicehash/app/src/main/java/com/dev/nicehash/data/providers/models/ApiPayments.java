package com.dev.nicehash.data.providers.models;

import java.util.List;
import java.util.Objects;

/**
 * Created by neura on 11/10/2018.
 */
public class ApiPayments {
    private final String method;
    private final List<ApiPaymentsData> payments;
    private final boolean nh_wallet;
    private final String addr;

    public ApiPayments(String method, List<ApiPaymentsData> payments, boolean nh_wallet, String addr) {
        this.method = method;
        this.payments = payments;
        this.nh_wallet = nh_wallet;
        this.addr = addr;
    }

    public String getMethod() {
        return method;
    }

    public List<ApiPaymentsData> getPayments() {
        return payments;
    }

    public boolean isNh_wallet() {
        return nh_wallet;
    }

    public String getAddr() {
        return addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiPayments)) return false;
        ApiPayments that = (ApiPayments) o;
        return nh_wallet == that.nh_wallet
                && Objects.equals(method, that.method)
                && Objects.equals(payments, that.payments)
                && Objects.equals(addr, that.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, payments, nh_wallet, addr);
    }

    @Override
    public String toString() {
        return "ApiPayments(method=" + method + ", payments=" + payments + ", nh_wallet=" + nh_wallet
                + ", addr=" + addr + ")";
    }
}
