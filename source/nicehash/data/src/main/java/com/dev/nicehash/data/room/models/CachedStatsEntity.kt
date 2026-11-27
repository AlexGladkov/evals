package com.dev.nicehash.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.nicehash.contracts.RoomContract

/**
 * Room cache for NiceHash detailed stats (ApiProviderEx) per miner.
 * Used by offline-first cache-then-network flow: shown when network fails,
 * refreshed whenever network succeeds.
 */
@Entity(tableName = RoomContract.TABLE_CACHED_STATS)
data class CachedStatsEntity(
        @PrimaryKey val miner: String,
        val data: String,  // JSON serialized ApiProviderEx
        val cachedAt: Long  // timestamp for freshness
)
