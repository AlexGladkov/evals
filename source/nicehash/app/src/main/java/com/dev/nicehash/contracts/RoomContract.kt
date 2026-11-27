package com.dev.nicehash.contracts

/**
 * Created by Alex Gladkov on 21.06.18.
 * Contract for room database (basename, queries, etc)
 */
class RoomContract {

    companion object {
        const val DATABASE_APP = "nicehashmonitor.db"

        const val TABLE_MINERS = "miners"
        const val TABLE_CONFIGURATIONS = "configurations"
        const val TABLE_CURRENCY = "currency"

        // offline-first cache tables (cache-then-network)
        const val TABLE_CACHED_STATS = "cached_stats"
        const val TABLE_CACHED_WORKERS = "cached_workers"
        const val TABLE_CACHED_PAYOUTS = "cached_payouts"
    }
}