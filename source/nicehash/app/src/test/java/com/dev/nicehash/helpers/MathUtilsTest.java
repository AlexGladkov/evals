package com.dev.nicehash.helpers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MathUtilsTest {

    @Test
    public void averageIsCorrect() {
        List<Double> collection = Arrays.asList(6.0, 6.0, 6.0, 6.0);
        assertEquals(6f, (float) MathUtils.getAverage(collection), 0.0f);
    }
}
