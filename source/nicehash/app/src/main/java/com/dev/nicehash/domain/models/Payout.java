package com.dev.nicehash.domain.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Model for payout tab (domain)
 */
public class Payout {
    private final String dateTime;
    private final float fee;
    private final double payBtc;
    private final double feeBtc;

    public Payout(String dateTime, float fee, double payBtc, double feeBtc) {
        this.dateTime = dateTime;
        this.fee = fee;
        this.payBtc = payBtc;
        this.feeBtc = feeBtc;
    }

    public String getDateTime() {
        return dateTime;
    }

    public float getFee() {
        return fee;
    }

    public double getPayBtc() {
        return payBtc;
    }

    public double getFeeBtc() {
        return feeBtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Payout)) return false;
        Payout payout = (Payout) o;
        return Float.compare(payout.fee, fee) == 0
                && Double.compare(payout.payBtc, payBtc) == 0
                && Double.compare(payout.feeBtc, feeBtc) == 0
                && Objects.equals(dateTime, payout.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, fee, payBtc, feeBtc);
    }

    @Override
    public String toString() {
        return "Payout(dateTime=" + dateTime + ", fee=" + fee + ", payBtc=" + payBtc
                + ", feeBtc=" + feeBtc + ")";
    }
}
