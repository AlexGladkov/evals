package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiGlobal24Stats {
    private final double price;
    private final int algo;
    private final double speed;

    public ApiGlobal24Stats(double price, int algo, double speed) {
        this.price = price;
        this.algo = algo;
        this.speed = speed;
    }

    public double getPrice() {
        return price;
    }

    public int getAlgo() {
        return algo;
    }

    public double getSpeed() {
        return speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiGlobal24Stats)) return false;
        ApiGlobal24Stats that = (ApiGlobal24Stats) o;
        return Double.compare(that.price, price) == 0
                && algo == that.algo
                && Double.compare(that.speed, speed) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, algo, speed);
    }

    @Override
    public String toString() {
        return "ApiGlobal24Stats(price=" + price + ", algo=" + algo + ", speed=" + speed + ")";
    }
}
