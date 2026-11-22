package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11/10/2018.
 */
public class ApiProviderPastData {
    private final long time;
    private final float a;
    private final double unpaid;

    public ApiProviderPastData(long time, float a, double unpaid) {
        this.time = time;
        this.a = a;
        this.unpaid = unpaid;
    }

    public long getTime() {
        return time;
    }

    public float getA() {
        return a;
    }

    public double getUnpaid() {
        return unpaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiProviderPastData)) return false;
        ApiProviderPastData that = (ApiProviderPastData) o;
        return time == that.time
                && Float.compare(that.a, a) == 0
                && Double.compare(that.unpaid, unpaid) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, a, unpaid);
    }

    @Override
    public String toString() {
        return "ApiProviderPastData(time=" + time + ", a=" + a + ", unpaid=" + unpaid + ")";
    }
}
