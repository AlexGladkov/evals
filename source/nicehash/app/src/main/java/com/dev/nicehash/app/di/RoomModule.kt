package com.dev.nicehash.app.di

import android.content.Context
import com.dev.nicehash.data.room.RoomAppDataSource
import com.dev.nicehash.data.room.dao.CachedPayoutsDao
import com.dev.nicehash.data.room.dao.CachedStatsDao
import com.dev.nicehash.data.room.dao.CachedWorkersDao
import com.dev.nicehash.data.room.dao.CurrencyDao
import com.dev.nicehash.data.room.dao.MinerDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Alex Gladkov on 21.06.18.
 * Room injections
 */
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideRoomCurrencyDataSource(context: Context) =
            RoomAppDataSource.buildDataSource(context)

    @Provides
    fun provideMinerDao(dataSource: RoomAppDataSource): MinerDao = dataSource.minerDao

    @Provides
    fun provideCachedStatsDao(dataSource: RoomAppDataSource): CachedStatsDao = dataSource.cachedStatsDao

    @Provides
    fun provideCachedWorkersDao(dataSource: RoomAppDataSource): CachedWorkersDao = dataSource.cachedWorkersDao

    @Provides
    fun provideCachedPayoutsDao(dataSource: RoomAppDataSource): CachedPayoutsDao = dataSource.cachedPayoutsDao

    @Provides
    fun provideCurrencyDao(dataSource: RoomAppDataSource): CurrencyDao = dataSource.currencyDao
}
