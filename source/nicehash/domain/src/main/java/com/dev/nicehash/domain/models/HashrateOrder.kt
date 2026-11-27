package com.dev.nicehash.domain.models

/**
 * Hashrate order from NiceHash marketplace
 */
data class HashrateOrder(val id: String, val algo: Int, val price: Double, val speed: Double, val alive: Boolean)
