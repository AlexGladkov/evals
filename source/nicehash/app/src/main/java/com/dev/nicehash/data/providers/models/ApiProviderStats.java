package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Models for stats provider request
 */
public class ApiProviderStats {
    private final double balance;
    private final double rejected_speed;
    private final int algo;
    private final double accepted_speed;

    public ApiProviderStats(double balance, double rejected_speed, int algo, double accepted_speed) {
        this.balance = balance;
        this.rejected_speed = rejected_speed;
        this.algo = algo;
        this.accepted_speed = accepted_speed;
    }

    public double getBalance() {
        return balance;
    }

    public double getRejected_speed() {
        return rejected_speed;
    }

    public int getAlgo() {
        return algo;
    }

    public double getAccepted_speed() {
        return accepted_speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderStats)) return false;
        ApiProviderStats that = (ApiProviderStats) o;
        return Double.compare(that.balance, balance) == 0
                && Double.compare(that.rejected_speed, rejected_speed) == 0
                && algo == that.algo
                && Double.compare(that.accepted_speed, accepted_speed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, rejected_speed, algo, accepted_speed);
    }

    @Override
    public String toString() {
        return "ApiProviderStats(balance=" + balance + ", rejected_speed=" + rejected_speed
                + ", algo=" + algo + ", accepted_speed=" + accepted_speed + ")";
    }
}
