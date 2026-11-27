package com.dev.nicehash.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.nicehash.contracts.RoomContract
import com.dev.nicehash.data.room.models.CachedWorkersEntity

/**
 * Dao interface for Room cached workers entity (offline-first cache-then-network)
 */
@Dao
interface CachedWorkersDao {

    @Query("SELECT * FROM ${RoomContract.TABLE_CACHED_WORKERS} WHERE miner = :miner")
    fun getByMiner(miner: String): CachedWorkersEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: CachedWorkersEntity)

    @Query("DELETE FROM ${RoomContract.TABLE_CACHED_WORKERS}")
    fun clear()
}
