package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.room.models.ConfigurationEntity;
import com.dev.nicehash.domain.models.Configuration;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Converter for configurations entities
 */
public interface ConfigurationConverter {
    ConfigurationEntity modelToDB(Configuration configuration);
    Configuration dbToModel(ConfigurationEntity configurationEntity);
}
