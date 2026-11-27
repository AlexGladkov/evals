package com.dev.nicehash.app.di

import com.dev.nicehash.app.servers.ExchangeServer
import com.dev.nicehash.app.servers.ExchangeServerImpl
import com.dev.nicehash.data.converters.*
import com.dev.nicehash.data.implementations.*
import com.dev.nicehash.data.providers.CurrencyProvider
import com.dev.nicehash.data.providers.MarketProvider
import com.dev.nicehash.data.providers.MinerProvider
import com.dev.nicehash.data.providers.NicehashProvider
import com.dev.nicehash.data.room.RoomAppDataSource
import com.dev.nicehash.domain.IncomeCalculator
import com.dev.nicehash.domain.repositories.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Alex Gladkov on 23.06.18.
 * DI class for repositories
 */
@Module
class RepositoryModule {

    @Provides
    fun provideMinerRepository(appDataSource: RoomAppDataSource, minerProvider: MinerProvider,
                               minerConverter: MinerConverter, paymentsConverter: PaymentsConverter,
                               workerConverter: WorkerConverter, balanceConverter: BalanceConverter): MinerRepository {
        return MinerRepositoryImpl(appDataSource = appDataSource, minerConverter = minerConverter,
                minerProvider = minerProvider, paymentsConverter = paymentsConverter,
                workerConverter = workerConverter, balanceConverter = balanceConverter)
    }

    @Provides
    fun provideCurrencyRepository(appDataSource: RoomAppDataSource,
                                  exchangeRateConverter: ExchangeRateConverter,
                                  currencyProvider: CurrencyProvider): CurrencyRepository {
        return CurrencyRepositoryImpl(exchangeRateConverter = exchangeRateConverter,
                appDataSource = appDataSource, exchangeRateProvider = currencyProvider)
    }

    @Provides
    fun provideConfigurationRepository(appDataSource: RoomAppDataSource,
                                       configurationConverter: ConfigurationConverter): ConfigurationRepository {
        return ConfigurationRepositoryImpl(appDataSource = appDataSource, configurationConverter = configurationConverter)
    }

    @Provides
    @Singleton
    fun provideExchangeService(currencyRepository: CurrencyRepository): ExchangeServer {
        return ExchangeServerImpl(currencyRepository = currencyRepository)
    }

    @Provides
    fun provideIncomeCalculator(): IncomeCalculator {
        return IncomeCalculator()
    }

    @Provides
    @Singleton
    fun provideMoneyRepository(nicehashProvider: NicehashProvider,
                               incomeCalculator: IncomeCalculator): MoneyRepository {
        return MoneyRepositoryImpl(nicehashProvider = nicehashProvider,
                incomeCalculator = incomeCalculator)
    }

    @Provides
    fun provideMarketRepository(marketProvider: MarketProvider,
                                marketConverter: MarketConverter): MarketRepository {
        return MarketRepositoryImpl(marketProvider = marketProvider, marketConverter = marketConverter)
    }
}