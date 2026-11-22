package com.dev.nicehash.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dev.nicehash.contracts.RoomContract;
import com.dev.nicehash.data.room.models.ConfigurationEntity;

import java.util.List;

/**
 * Created by Alex Gladkov on 16.07.18
 * Dao for configuration entity
 */
@Dao
public interface ConfigurationDao {

    @Query("SELECT * FROM " + RoomContract.TABLE_CONFIGURATIONS)
    List<ConfigurationEntity> getConfigurations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addConfiguration(ConfigurationEntity minerEntity);
}
