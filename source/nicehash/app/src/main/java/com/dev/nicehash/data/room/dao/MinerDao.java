package com.dev.nicehash.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dev.nicehash.contracts.RoomContract;
import com.dev.nicehash.data.room.models.MinerEntity;

import java.util.List;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Dao interface for Room Miner Entity
 */
@Dao
public interface MinerDao {

    @Query("SELECT * FROM " + RoomContract.TABLE_MINERS)
    List<MinerEntity> getMiners();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addMiner(MinerEntity minerEntity);

    @Query("DELETE FROM " + RoomContract.TABLE_MINERS)
    void clearMiners();
}
