package com.dev.nicehash.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dev.nicehash.contracts.RoomContract
import com.dev.nicehash.data.room.models.CachedPayoutsEntity

/**
 * Dao interface for Room cached payouts entity (offline-first cache-then-network)
 */
@Dao
interface CachedPayoutsDao {

    @Query("SELECT * FROM ${RoomContract.TABLE_CACHED_PAYOUTS} WHERE miner = :miner")
    fun getByMiner(miner: String): CachedPayoutsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(entity: CachedPayoutsEntity)

    @Query("DELETE FROM ${RoomContract.TABLE_CACHED_PAYOUTS}")
    fun clear()
}
