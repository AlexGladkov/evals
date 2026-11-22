package com.dev.nicehash.app.di;

import com.dev.nicehash.data.providers.CurrencyProvider;
import com.dev.nicehash.data.providers.GeneralProvider;
import com.dev.nicehash.data.providers.MinerProvider;
import com.dev.nicehash.data.providers.NicehashProvider;
import com.dev.nicehash.data.providers.implementations.CurrencyProviderImpl;
import com.dev.nicehash.data.providers.implementations.GeneralProviderImpl;
import com.dev.nicehash.data.providers.implementations.MinerProviderImpl;
import com.dev.nicehash.data.providers.implementations.NicehashProviderImpl;
import com.dev.nicehash.data.providers.services.RemoteCurrencyService;
import com.dev.nicehash.data.providers.services.RemoteGeneralService;
import com.dev.nicehash.data.providers.services.RemoteMinerService;
import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by Alex Gladkov on 05.08.18.
 * DI class for providers
 */
@Module
public class ProviderModule {

    @Provides
    @Singleton
    NicehashProvider createNicehashProvider(RemoteMinerService remoteMinerService) {
        return new NicehashProviderImpl(remoteMinerService);
    }

    @Provides
    GeneralProvider createGeneralProvider(RemoteGeneralService remoteGeneralService) {
        return new GeneralProviderImpl(remoteGeneralService);
    }

    @Provides
    MinerProvider createMinerProvider(RemoteMinerService remoteMinerService, Gson gson) {
        return new MinerProviderImpl(remoteMinerService, gson);
    }

    @Provides
    CurrencyProvider createCurrencyProvider(RemoteCurrencyService remoteCurrencyService) {
        return new CurrencyProviderImpl(remoteCurrencyService);
    }
}
