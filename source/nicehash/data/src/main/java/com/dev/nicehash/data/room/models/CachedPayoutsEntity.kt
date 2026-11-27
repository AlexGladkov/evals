package com.dev.nicehash.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.nicehash.contracts.RoomContract

/**
 * Room cache for payment history (list of ApiPayment) per miner.
 * Used by offline-first cache-then-network flow.
 */
@Entity(tableName = RoomContract.TABLE_CACHED_PAYOUTS)
data class CachedPayoutsEntity(
        @PrimaryKey val miner: String,
        val data: String,  // JSON serialized payouts
        val cachedAt: Long
)
