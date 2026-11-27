package com.dev.nicehash.helpers

import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MathUtilsTest {

    @Test
    fun averageIsCorrect() {
        val collection = listOf(6.0, 6.0, 6.0, 6.0)
        assertEquals(6f, com.dev.nicehash.helpers.MathUtils.getAverage(collection = collection).toFloat())
    }

    @Test
    fun averageOfDifferentValues() {
        val collection = listOf(1.0, 2.0, 3.0, 4.0)
        assertEquals(2.5, MathUtils.getAverage(collection = collection), 1e-9)
    }

    @Test
    fun averageOfSingleElement() {
        assertEquals(42.5, MathUtils.getAverage(collection = listOf(42.5)), 1e-9)
    }

    @Test
    fun averageOfLargeNumbers() {
        val collection = listOf(1.0E12, 3.0E12)
        assertEquals(2.0E12, MathUtils.getAverage(collection = collection), 1.0)
    }

    @Test
    fun averageOfNegativeValues() {
        assertEquals(0.0, MathUtils.getAverage(collection = listOf(-2.0, 2.0)), 1e-9)
        assertEquals(-5.0, MathUtils.getAverage(collection = listOf(-4.0, -6.0)), 1e-9)
    }

    /**
     * Known bug (fixed later on task/c2): an empty collection makes getAverage divide
     * 0.0 / 0 which yields NaN instead of a well-defined average of 0.0.
     * Expected behavior once fixed:
     */
    @Ignore("Known bug: getAverage(emptyList()) returns NaN on the current code (0.0 / 0.0)")
    @Test
    fun averageOfEmptyCollectionShouldBeZero() {
        assertEquals(0.0, MathUtils.getAverage(collection = emptyList()), 0.0)
    }

    /**
     * Characterization test pinning the CURRENT (buggy) behavior for empty input:
     * NaN. Flip it together with [averageOfEmptyCollectionShouldBeZero] once fixed.
     */
    @Test
    fun averageOfEmptyCollectionReturnsNaNOnCurrentCode() {
        assertTrue(MathUtils.getAverage(collection = emptyList()).isNaN())
    }
}
