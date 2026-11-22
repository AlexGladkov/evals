package com.dev.nicehash.app.di;

import android.content.Context;

import com.dev.nicehash.app.App;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Alex Gladkov on 21.06.18.
 * App module provides context from App
 */
@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }
}
