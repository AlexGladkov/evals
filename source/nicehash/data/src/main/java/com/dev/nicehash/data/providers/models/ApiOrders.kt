package com.dev.nicehash.data.providers.models

/**
 * Models for orders.get api request
 * @see <a href="https://api2.nicehash.com/main/api/v2/">NiceHash api</a>
 */
data class ApiOrdersResult(val method: String, val result: List<ApiOrder>)
data class ApiOrder(val id: String?, val algo: Int?, val price: Double?, val speed: Double?, val alive: Boolean?)
