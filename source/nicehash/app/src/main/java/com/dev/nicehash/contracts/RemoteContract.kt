package com.dev.nicehash.contracts

/**
 * Created by Alex Gladkov on 21.06.18.
 * Contract values for remote operations (endpoints, params, etc)
 */
class RemoteContract {

    companion object {
        val baseUrl = "https://api2.nicehash.com/main/api/v2/"
        val baseUrlCurrency = "https://blockchain.info/"

        /** Base url for the support chat service (configurable) */
        val chatBaseUrl = baseUrl

        const val METHOD = "method"
        const val MINER = "addr"
        const val FROM = "from"

        const val METHOD_STATS_PROVIDER = "stats.provider"
        const val METHOD_STATS_PROVIDER_EX = "stats.provider.ex"
        const val METHOD_STATS_PROVIDER_WORKERS = "stats.provider.workers"
        const val METHOD_STATS_PROVIDER_PAYMENTS = "stats.provider.payments"
    }
}
