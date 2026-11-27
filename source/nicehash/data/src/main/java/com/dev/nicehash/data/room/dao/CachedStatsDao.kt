package com.dev.nicehash.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.nicehash.contracts.RoomContract
import com.dev.nicehash.data.room.models.CachedStatsEntity

/**
 * Dao interface for Room cached stats entity (offline-first cache-then-network)
 */
@Dao
interface CachedStatsDao {

    @Query("SELECT * FROM ${RoomContract.TABLE_CACHED_STATS} WHERE miner = :miner")
    fun getByMiner(miner: String): CachedStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: CachedStatsEntity)

    @Query("DELETE FROM ${RoomContract.TABLE_CACHED_STATS}")
    fun clear()
}
