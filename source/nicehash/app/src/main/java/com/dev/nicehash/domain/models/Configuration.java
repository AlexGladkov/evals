package com.dev.nicehash.domain.models;

import com.dev.nicehash.enums.Theme;
import com.dev.nicehash.helpers.EnumCollections;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Model for app configuration (domain level)
 */
public class Configuration implements Serializable {
    private final int id;
    private Theme theme;
    private int defaultTab;
    private EnumCollections.Currency currency;
    private EnumCollections.Language defaultLanguage;
    private boolean isSound;
    private boolean isVibro;
    private boolean isPayments;
    private boolean isMaintenance;
    private boolean isTop;
    private boolean isBottom;
    private boolean isStrix;
    private int minerId;

    public Configuration(int id, Theme theme, int defaultTab, EnumCollections.Currency currency,
                         EnumCollections.Language defaultLanguage, boolean isSound, boolean isVibro,
                         boolean isPayments, boolean isMaintenance, boolean isTop, boolean isBottom,
                         boolean isStrix, int minerId) {
        this.id = id;
        this.theme = theme;
        this.defaultTab = defaultTab;
        this.currency = currency;
        this.defaultLanguage = defaultLanguage;
        this.isSound = isSound;
        this.isVibro = isVibro;
        this.isPayments = isPayments;
        this.isMaintenance = isMaintenance;
        this.isTop = isTop;
        this.isBottom = isBottom;
        this.isStrix = isStrix;
        this.minerId = minerId;
    }

    public static Configuration defaultInstance() {
        return new Configuration(-1, Theme.LightBlue, 0,
                EnumCollections.Currency.USD, EnumCollections.Language.English, true, true,
                true, true, true, true, true, 0);
    }

    public int getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public int getDefaultTab() {
        return defaultTab;
    }

    public void setDefaultTab(int defaultTab) {
        this.defaultTab = defaultTab;
    }

    public EnumCollections.Currency getCurrency() {
        return currency;
    }

    public void setCurrency(EnumCollections.Currency currency) {
        this.currency = currency;
    }

    public EnumCollections.Language getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(EnumCollections.Language defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public boolean isSound() {
        return isSound;
    }

    public void setSound(boolean sound) {
        isSound = sound;
    }

    public boolean isVibro() {
        return isVibro;
    }

    public void setVibro(boolean vibro) {
        isVibro = vibro;
    }

    public boolean isPayments() {
        return isPayments;
    }

    public void setPayments(boolean payments) {
        isPayments = payments;
    }

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public boolean isBottom() {
        return isBottom;
    }

    public void setBottom(boolean bottom) {
        isBottom = bottom;
    }

    public boolean isStrix() {
        return isStrix;
    }

    public void setStrix(boolean strix) {
        isStrix = strix;
    }

    public int getMinerId() {
        return minerId;
    }

    public void setMinerId(int minerId) {
        this.minerId = minerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuration)) return false;
        Configuration that = (Configuration) o;
        return id == that.id
                && defaultTab == that.defaultTab
                && isSound == that.isSound
                && isVibro == that.isVibro
                && isPayments == that.isPayments
                && isMaintenance == that.isMaintenance
                && isTop == that.isTop
                && isBottom == that.isBottom
                && isStrix == that.isStrix
                && minerId == that.minerId
                && theme == that.theme
                && currency == that.currency
                && defaultLanguage == that.defaultLanguage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, theme, defaultTab, currency, defaultLanguage, isSound, isVibro,
                isPayments, isMaintenance, isTop, isBottom, isStrix, minerId);
    }

    @Override
    public String toString() {
        return "Configuration(id=" + id + ", theme=" + theme + ", defaultTab=" + defaultTab
                + ", currency=" + currency + ", defaultLanguage=" + defaultLanguage
                + ", isSound=" + isSound + ", isVibro=" + isVibro + ", isPayments=" + isPayments
                + ", isMaintenance=" + isMaintenance + ", isTop=" + isTop + ", isBottom=" + isBottom
                + ", isStrix=" + isStrix + ", minerId=" + minerId + ")";
    }
}
