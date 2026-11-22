package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiProviderData {
    private final float a;
    private final float rs;
    private final double unpaid;

    public ApiProviderData(float a, float rs, double unpaid) {
        this.a = a;
        this.rs = rs;
        this.unpaid = unpaid;
    }

    public float getA() {
        return a;
    }

    public float getRs() {
        return rs;
    }

    public double getUnpaid() {
        return unpaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderData)) return false;
        ApiProviderData that = (ApiProviderData) o;
        return Float.compare(that.a, a) == 0
                && Float.compare(that.rs, rs) == 0
                && Double.compare(that.unpaid, unpaid) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, rs, unpaid);
    }

    @Override
    public String toString() {
        return "ApiProviderData(a=" + a + ", rs=" + rs + ", unpaid=" + unpaid + ")";
    }
}
