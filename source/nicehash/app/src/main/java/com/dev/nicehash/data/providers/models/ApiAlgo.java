package com.dev.nicehash.data.providers.models;

import java.util.Objects;

public class ApiAlgo {
    private final double speed;
    private final double price;

    public ApiAlgo(double speed, double price) {
        this.speed = speed;
        this.price = price;
    }

    public double getSpeed() {
        return speed;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiAlgo)) return false;
        ApiAlgo apiAlgo = (ApiAlgo) o;
        return Double.compare(apiAlgo.speed, speed) == 0 && Double.compare(apiAlgo.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(speed, price);
    }

    @Override
    public String toString() {
        return "ApiAlgo(speed=" + speed + ", price=" + price + ")";
    }
}
