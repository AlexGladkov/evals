package com.dev.nicehash.data.converters

import com.dev.nicehash.data.providers.models.ApiOrder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for marketplace converter (api -> domain)
 */
class MarketConverterTest {

    private val converter = MarketConverterImpl()

    @Test
    fun parseValidResponseCorrectly() {
        val result = listOf(ApiOrder(id = "abc123", algo = 24, price = 0.12, speed = 0.5, alive = true),
                ApiOrder(id = "def456", algo = 20, price = 0.35, speed = 12.4, alive = false))

        val orders = converter.apiToModels(result = result)

        assertEquals(2, orders.size)

        val first = orders[0]
        assertEquals("abc123", first.id)
        assertEquals(24, first.algo)
        assertEquals(0.12, first.price, 0.000001)
        assertEquals(0.5, first.speed, 0.000001)
        assertTrue(first.alive)

        val second = orders[1]
        assertEquals("def456", second.id)
        assertEquals(20, second.algo)
        assertEquals(0.35, second.price, 0.000001)
        assertEquals(12.4, second.speed, 0.000001)
        assertTrue(second.alive.not())
    }

    @Test
    fun handlesEmptyOrdersList() {
        val orders = converter.apiToModels(result = emptyList())

        assertTrue(orders.isEmpty())
    }

    @Test
    fun handlesMalformedDataGracefully() {
        val orders = converter.apiToModels(result = listOf(
                ApiOrder(id = null, algo = null, price = null, speed = null, alive = null),
                ApiOrder(id = "abc123", algo = 24, price = 0.12, speed = 0.5, alive = true)))

        assertEquals(2, orders.size)

        val malformed = orders[0]
        assertEquals("", malformed.id)
        assertEquals(0, malformed.algo)
        assertEquals(0.0, malformed.price, 0.000001)
        assertEquals(0.0, malformed.speed, 0.000001)
        assertTrue(malformed.alive.not())

        val valid = orders[1]
        assertEquals("abc123", valid.id)
        assertTrue(valid.alive)
    }
}
