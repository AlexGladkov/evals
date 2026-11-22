package com.dev.nicehash.domain.models;

import java.util.Objects;

public class ChartItem {
    private long x;
    private double y;

    public ChartItem(long x, double y) {
        this.x = x;
        this.y = y;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChartItem)) return false;
        ChartItem that = (ChartItem) o;
        return x == that.x && Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "ChartItem(x=" + x + ", y=" + y + ")";
    }
}
