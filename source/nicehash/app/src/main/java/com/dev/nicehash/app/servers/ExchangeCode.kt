package com.dev.nicehash.app.servers

/**
 * Supported fiat currencies for conversion.
 * Extracted from ExchangeServerImpl so that UI/presenters don't depend
 * on the concrete implementation class (DIP).
 */
enum class ExchangeCode(val value: String) {
    Ru("RUB"), US("USD")
}
