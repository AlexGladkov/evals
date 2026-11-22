package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by neura on 11/10/2018.
 */
public class ApiPaymentsData {
    private final double amount;
    private final double fee;
    private final String TXID;
    private final long time;
    private final int type;

    public ApiPaymentsData(double amount, double fee, String TXID, long time, int type) {
        this.amount = amount;
        this.fee = fee;
        this.TXID = TXID;
        this.time = time;
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public double getFee() {
        return fee;
    }

    public String getTXID() {
        return TXID;
    }

    public long getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiPaymentsData)) return false;
        ApiPaymentsData that = (ApiPaymentsData) o;
        return Double.compare(that.amount, amount) == 0
                && Double.compare(that.fee, fee) == 0
                && time == that.time
                && type == that.type
                && Objects.equals(TXID, that.TXID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, fee, TXID, time, type);
    }

    @Override
    public String toString() {
        return "ApiPaymentsData(amount=" + amount + ", fee=" + fee + ", TXID=" + TXID
                + ", time=" + time + ", type=" + type + ")";
    }
}
