package com.dev.nicehash.app.di;

import com.dev.nicehash.app.servers.ExchangeServer;
import com.dev.nicehash.app.servers.ExchangeServerImpl;
import com.dev.nicehash.data.converters.BalanceConverter;
import com.dev.nicehash.data.converters.ConfigurationConverter;
import com.dev.nicehash.data.converters.ExchangeRateConverter;
import com.dev.nicehash.data.converters.MinerConverter;
import com.dev.nicehash.data.converters.PaymentsConverter;
import com.dev.nicehash.data.converters.WorkerConverter;
import com.dev.nicehash.data.implementations.ConfigurationRepositoryImpl;
import com.dev.nicehash.data.implementations.CurrencyRepositoryImpl;
import com.dev.nicehash.data.implementations.GeneralRepositoryImpl;
import com.dev.nicehash.data.implementations.MinerRepositoryImpl;
import com.dev.nicehash.data.implementations.MoneyRepositoryImpl;
import com.dev.nicehash.data.providers.CurrencyProvider;
import com.dev.nicehash.data.providers.GeneralProvider;
import com.dev.nicehash.data.providers.MinerProvider;
import com.dev.nicehash.data.providers.NicehashProvider;
import com.dev.nicehash.data.room.RoomAppDataSource;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;
import com.dev.nicehash.domain.repositories.CurrencyRepository;
import com.dev.nicehash.domain.repositories.GeneralRepository;
import com.dev.nicehash.domain.repositories.MinerRepository;
import com.dev.nicehash.domain.repositories.MoneyRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alex Gladkov on 23.06.18.
 * DI class for repositories
 */
@Module
public class RepositoryModule {

    @Provides
    MinerRepository provideMinerRepository(RoomAppDataSource appDataSource, MinerProvider minerProvider,
                                           MinerConverter minerConverter, PaymentsConverter paymentsConverter,
                                           WorkerConverter workerConverter, BalanceConverter balanceConverter) {
        return new MinerRepositoryImpl(appDataSource, minerProvider, minerConverter,
                paymentsConverter, balanceConverter, workerConverter);
    }

    @Provides
    CurrencyRepository provideCurrencyRepository(RoomAppDataSource appDataSource,
                                                 ExchangeRateConverter exchangeRateConverter,
                                                 CurrencyProvider currencyProvider) {
        return new CurrencyRepositoryImpl(exchangeRateConverter, currencyProvider, appDataSource);
    }

    @Provides
    ConfigurationRepository provideConfigurationRepository(RoomAppDataSource appDataSource,
                                                           ConfigurationConverter configurationConverter) {
        return new ConfigurationRepositoryImpl(appDataSource, configurationConverter);
    }

    @Provides
    GeneralRepository provideGeneralRepository(GeneralProvider generalProvider) {
        return new GeneralRepositoryImpl(generalProvider);
    }

    @Provides
    @Singleton
    ExchangeServer provideExchangeService(CurrencyRepository currencyRepository) {
        return new ExchangeServerImpl(currencyRepository);
    }

    @Provides
    @Singleton
    MoneyRepository provideMoneyRepository(NicehashProvider nicehashProvider) {
        return new MoneyRepositoryImpl(nicehashProvider);
    }
}
