package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 * Models for global current api request
 */
public class ApiGlobalCurrentStats {
    private final double profitability_above_ltc;
    private final double price;
    private final double profitability_ltc;
    private final double speed;

    public ApiGlobalCurrentStats(double profitability_above_ltc, double price,
                                 double profitability_ltc, double speed) {
        this.profitability_above_ltc = profitability_above_ltc;
        this.price = price;
        this.profitability_ltc = profitability_ltc;
        this.speed = speed;
    }

    public double getProfitability_above_ltc() {
        return profitability_above_ltc;
    }

    public double getPrice() {
        return price;
    }

    public double getProfitability_ltc() {
        return profitability_ltc;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiGlobalCurrentStats)) return false;
        ApiGlobalCurrentStats that = (ApiGlobalCurrentStats) o;
        return Double.compare(that.profitability_above_ltc, profitability_above_ltc) == 0
                && Double.compare(that.price, price) == 0
                && Double.compare(that.profitability_ltc, profitability_ltc) == 0
                && Double.compare(that.speed, speed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(profitability_above_ltc, price, profitability_ltc, speed);
    }

    @Override
    public String toString() {
        return "ApiGlobalCurrentStats(profitability_above_ltc=" + profitability_above_ltc
                + ", price=" + price + ", profitability_ltc=" + profitability_ltc
                + ", speed=" + speed + ")";
    }
}
