package com.dev.nicehash.app.di;

import com.github.terrakok.cicerone.Cicerone;
import com.github.terrakok.cicerone.NavigatorHolder;
import com.github.terrakok.cicerone.Router;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by agladkov on 22.12.17.
 * Dagger module provides navigator and router for Cicerone
 */
@Module
public class NavigationModule {
    private Cicerone<Router> cicerone = Cicerone.create();

    @Provides
    @Singleton
    Router provideRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    NavigatorHolder provideNavigationHolder() {
        return cicerone.getNavigatorHolder();
    }
}
