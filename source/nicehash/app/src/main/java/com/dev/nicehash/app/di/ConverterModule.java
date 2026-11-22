package com.dev.nicehash.app.di;

import com.dev.nicehash.data.converters.BalanceConverter;
import com.dev.nicehash.data.converters.BalanceConverterImpl;
import com.dev.nicehash.data.converters.ConfigurationConverter;
import com.dev.nicehash.data.converters.ConfigurationConverterImpl;
import com.dev.nicehash.data.converters.ExchangeRateConverter;
import com.dev.nicehash.data.converters.ExchangeRateConverterImpl;
import com.dev.nicehash.data.converters.MinerConverter;
import com.dev.nicehash.data.converters.MinerConverterImpl;
import com.dev.nicehash.data.converters.PaymentsConverter;
import com.dev.nicehash.data.converters.PaymentsConverterImpl;
import com.dev.nicehash.data.converters.WorkerConverter;
import com.dev.nicehash.data.converters.WorkerConverterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Alex Gladkov on 23.06.18.
 * DI module for converters
 */
@Module
public class ConverterModule {

    @Provides
    MinerConverter provideMinerConverter() {
        return new MinerConverterImpl();
    }

    @Provides
    ConfigurationConverter provideConfigurationConverter() {
        return new ConfigurationConverterImpl();
    }

    @Provides
    PaymentsConverter providePaymentsConverter() {
        return new PaymentsConverterImpl();
    }

    @Provides
    WorkerConverter provideWorkerConverter() {
        return new WorkerConverterImpl();
    }

    @Provides
    BalanceConverter provideBalanceConverter() {
        return new BalanceConverterImpl();
    }

    @Provides
    ExchangeRateConverter provideExchangeRateConverter() {
        return new ExchangeRateConverterImpl();
    }
}
