package com.dev.nicehash.helpers;

/**
 * Created by neura on 11/10/2018.
 */
public class MathUtils {

    public static double getAverage(java.util.Collection<Double> collection) {
        double sum = 0.0;
        for (Double item : collection) {
            sum += item;
        }
        return sum / collection.size();
    }
}
