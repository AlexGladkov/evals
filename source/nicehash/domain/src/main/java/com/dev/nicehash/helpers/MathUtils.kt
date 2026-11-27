package com.dev.nicehash.helpers

/**
 * Created by neura on 11/10/2018.
 */
class MathUtils {

    companion object {
        fun getAverage(collection: Collection<Double>): Double {
            if (collection.isEmpty()) return 0.0
            return collection.sum() / collection.size
        }
    }
}