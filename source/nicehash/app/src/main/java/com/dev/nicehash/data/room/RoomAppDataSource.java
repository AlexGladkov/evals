package com.dev.nicehash.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dev.nicehash.contracts.RoomContract;
import com.dev.nicehash.data.room.dao.ConfigurationDao;
import com.dev.nicehash.data.room.dao.CurrencyDao;
import com.dev.nicehash.data.room.dao.MinerDao;
import com.dev.nicehash.data.room.models.ConfigurationEntity;
import com.dev.nicehash.data.room.models.ExchangeRateEntity;
import com.dev.nicehash.data.room.models.MinerEntity;

/**
 * Created by Alex Gladkov on 21.06.18.
 * Class for room implementation
 */
@Database(
    entities = {MinerEntity.class, ConfigurationEntity.class, ExchangeRateEntity.class},
    version = 10,
    exportSchema = false
)
public abstract class RoomAppDataSource extends RoomDatabase {
    public abstract MinerDao getMinerDao();
    public abstract ConfigurationDao getConfigurationDao();
    public abstract CurrencyDao getCurrencyDao();

    public static RoomAppDataSource buildDataSource(Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(), RoomAppDataSource.class, RoomContract.DATABASE_APP)
                .fallbackToDestructiveMigration()
                .build();
    }
}
