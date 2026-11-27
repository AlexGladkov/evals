package com.dev.nicehash.data.room.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dev.nicehash.contracts.RoomContract

/**
 * Room cache for worker hub data (list of ApiWorker) per miner.
 * Used by offline-first cache-then-network flow.
 */
@Entity(tableName = RoomContract.TABLE_CACHED_WORKERS)
data class CachedWorkersEntity(
        @PrimaryKey val miner: String,
        val data: String,  // JSON serialized worker list
        val cachedAt: Long
)
