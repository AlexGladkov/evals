package com.dev.nicehash.data.converters;

import com.dev.nicehash.data.room.models.ConfigurationEntity;
import com.dev.nicehash.domain.models.Configuration;
import com.dev.nicehash.enums.Theme;
import com.dev.nicehash.helpers.EnumCollections;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Implementation for configuration converter
 */
public class ConfigurationConverterImpl implements ConfigurationConverter {
    @Override
    public ConfigurationEntity modelToDB(Configuration configuration) {
        // Kotlin named args ported positionally to declared order:
        // (id, theme, defaultTab, defaultLanguage, isSound, isVibro, isPayments,
        //  isMaintenance, isTop, isBottom, isStrix, minerId, currency)
        return new ConfigurationEntity(configuration.getId(), configuration.getTheme().getValue(),
                configuration.getDefaultTab(), configuration.getDefaultLanguage().getIntCode(),
                configuration.isSound(), configuration.isVibro(), configuration.isPayments(),
                configuration.isMaintenance(), configuration.isTop(), configuration.isBottom(),
                configuration.isStrix(), configuration.getMinerId(), configuration.getCurrency().getId());
    }

    @Override
    public Configuration dbToModel(ConfigurationEntity configurationEntity) {
        Theme theme;
        if (configurationEntity.getTheme() == Theme.LightGreen.getValue()) {
            theme = Theme.LightGreen;
        } else if (configurationEntity.getTheme() == Theme.LightPurple.getValue()) {
            theme = Theme.LightPurple;
        } else if (configurationEntity.getTheme() == Theme.LightBlue.getValue()) {
            theme = Theme.LightBlue;
        } else if (configurationEntity.getTheme() == Theme.LightRed.getValue()) {
            theme = Theme.LightRed;
        } else if (configurationEntity.getTheme() == Theme.LightOrange.getValue()) {
            theme = Theme.LightOrange;
        } else if (configurationEntity.getTheme() == Theme.DarkPurple.getValue()) {
            theme = Theme.DarkPurple;
        } else if (configurationEntity.getTheme() == Theme.DarkGreen.getValue()) {
            theme = Theme.DarkGreen;
        } else if (configurationEntity.getTheme() == Theme.DarkOrange.getValue()) {
            theme = Theme.DarkOrange;
        } else if (configurationEntity.getTheme() == Theme.DarkRed.getValue()) {
            theme = Theme.DarkRed;
        } else if (configurationEntity.getTheme() == Theme.DarkBlue.getValue()) {
            theme = Theme.DarkPurple;
        } else {
            theme = Theme.LightBlue;
        }

        EnumCollections.Language defaultLanguage;
        if (configurationEntity.getDefaultLanguage() == EnumCollections.Language.Russian.getIntCode()) {
            defaultLanguage = EnumCollections.Language.Russian;
        } else if (configurationEntity.getDefaultLanguage() == EnumCollections.Language.English.getIntCode()) {
            defaultLanguage = EnumCollections.Language.English;
        } else if (configurationEntity.getDefaultLanguage() == EnumCollections.Language.Chinese.getIntCode()) {
            defaultLanguage = EnumCollections.Language.Chinese;
        } else {
            defaultLanguage = EnumCollections.Language.English;
        }

        EnumCollections.Currency currency;
        if (configurationEntity.getCurrency() == EnumCollections.Currency.Euro.getId()) {
            currency = EnumCollections.Currency.Euro;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.Dania.getId()) {
            currency = EnumCollections.Currency.Dania;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.Czech.getId()) {
            currency = EnumCollections.Currency.Czech;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.China.getId()) {
            currency = EnumCollections.Currency.China;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.CHF.getId()) {
            currency = EnumCollections.Currency.CHF;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.Canada.getId()) {
            currency = EnumCollections.Currency.Canada;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.Belgium.getId()) {
            currency = EnumCollections.Currency.Belgium;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.Australian.getId()) {
            currency = EnumCollections.Currency.Australian;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.USD.getId()) {
            currency = EnumCollections.Currency.USD;
        } else if (configurationEntity.getCurrency() == EnumCollections.Currency.BritainPound.getId()) {
            currency = EnumCollections.Currency.BritainPound;
        } else {
            currency = EnumCollections.Currency.Russian;
        }

        // Kotlin named args ported positionally to declared order:
        // (id, theme, defaultTab, currency, defaultLanguage, isSound, isVibro,
        //  isPayments, isMaintenance, isTop, isBottom, isStrix, minerId)
        // Note: original Kotlin passed isVibro for isBottom — ported as-is (bug preserved)
        return new Configuration(configurationEntity.getId(), theme, configurationEntity.getDefaultTab(),
                currency, defaultLanguage, configurationEntity.isSound(), configurationEntity.isVibro(),
                configurationEntity.isPayments(), configurationEntity.isMaintenance(), configurationEntity.isTop(),
                configurationEntity.isVibro(), configurationEntity.isStrix(), configurationEntity.getMinerId());
    }
}
