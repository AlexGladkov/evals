package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 05.08.18.
 * Data model class for backend payments
 */
public class ApiPayment {
    private final double amount;
    private final double fee;
    private final String time;
    private final int type;
    private final String TXID;

    public ApiPayment(double amount, double fee, String time, int type, String TXID) {
        this.amount = amount;
        this.fee = fee;
        this.time = time;
        this.type = type;
        this.TXID = TXID;
    }

    public double getAmount() {
        return amount;
    }

    public double getFee() {
        return fee;
    }

    public String getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

    public String getTXID() {
        return TXID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiPayment)) return false;
        ApiPayment that = (ApiPayment) o;
        return Double.compare(that.amount, amount) == 0
                && Double.compare(that.fee, fee) == 0
                && type == that.type
                && Objects.equals(time, that.time)
                && Objects.equals(TXID, that.TXID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, fee, time, type, TXID);
    }

    @Override
    public String toString() {
        return "ApiPayment(amount=" + amount + ", fee=" + fee + ", time=" + time + ", type=" + type
                + ", TXID=" + TXID + ")";
    }
}
