package com.dev.nicehash.data.implementations;

import android.util.Log;

import com.dev.nicehash.data.converters.ConfigurationConverter;
import com.dev.nicehash.data.room.RoomAppDataSource;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.domain.repositories.ConfigurationRepository;

import io.reactivex.Single;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Implementation for ConfigurationRepository
 */
public class ConfigurationRepositoryImpl implements ConfigurationRepository {
    private final String TAG = ConfigurationRepositoryImpl.class.getSimpleName();

    private final RoomAppDataSource appDataSource;
    private final ConfigurationConverter configurationConverter;

    public ConfigurationRepositoryImpl(RoomAppDataSource appDataSource,
                                       ConfigurationConverter configurationConverter) {
        this.appDataSource = appDataSource;
        this.configurationConverter = configurationConverter;
    }

    @Override
    public Single<Configuration> fetchConfiguration() {
        return Single.create(subscriber -> {
            try {
                java.util.List<com.dev.nicehash.data.room.models.ConfigurationEntity> configs =
                        appDataSource.getConfigurationDao().getConfigurations();
                if (configs.isEmpty()) {
                    Configuration newConfig = Configuration.defaultInstance();
                    appDataSource.getConfigurationDao().addConfiguration(configurationConverter
                            .modelToDB(newConfig));
                    subscriber.onSuccess(newConfig);
                } else {
                    subscriber.onSuccess(configurationConverter.dbToModel(configs.get(0)));
                }
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    @Override
    public Single<Boolean> updateConfiguration(Configuration configuration) {
        return Single.create(subscriber -> {
            try {
                appDataSource.getConfigurationDao().addConfiguration(configurationConverter
                        .modelToDB(configuration));
                subscriber.onSuccess(true);
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }
}
