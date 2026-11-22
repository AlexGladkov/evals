package com.dev.nicehash.app.di;

import android.content.Context;

import com.dev.nicehash.data.room.RoomAppDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alex Gladkov on 21.06.18.
 * Room injections
 */
@Module
public class RoomModule {

    @Provides
    @Singleton
    RoomAppDataSource provideRoomCurrencyDataSource(Context context) {
        return RoomAppDataSource.buildDataSource(context);
    }
}
