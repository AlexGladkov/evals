package com.dev.nicehash.data.room.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dev.nicehash.contracts.RoomContract;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 16.07.18.
 * Class for app configuration
 */
@Entity(tableName = RoomContract.TABLE_CONFIGURATIONS)
public class ConfigurationEntity {
    @PrimaryKey
    private final int id;
    private final int theme;
    private final int defaultTab;
    private final int defaultLanguage;
    private boolean isSound;
    private boolean isVibro;
    private boolean isPayments;
    private boolean isMaintenance;
    private boolean isTop;
    private boolean isBottom;
    private boolean isStrix;
    private int minerId;
    private int currency;

    public ConfigurationEntity(int id, int theme, int defaultTab, int defaultLanguage,
                               boolean isSound, boolean isVibro, boolean isPayments,
                               boolean isMaintenance, boolean isTop, boolean isBottom,
                               boolean isStrix, int minerId, int currency) {
        this.id = id;
        this.theme = theme;
        this.defaultTab = defaultTab;
        this.defaultLanguage = defaultLanguage;
        this.isSound = isSound;
        this.isVibro = isVibro;
        this.isPayments = isPayments;
        this.isMaintenance = isMaintenance;
        this.isTop = isTop;
        this.isBottom = isBottom;
        this.isStrix = isStrix;
        this.minerId = minerId;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public int getTheme() {
        return theme;
    }

    public int getDefaultTab() {
        return defaultTab;
    }

    public int getDefaultLanguage() {
        return defaultLanguage;
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

    public int getCurrency() {
        return currency;
    }

    public void setCurrency(int currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigurationEntity)) return false;
        ConfigurationEntity that = (ConfigurationEntity) o;
        return id == that.id
                && theme == that.theme
                && defaultTab == that.defaultTab
                && defaultLanguage == that.defaultLanguage
                && isSound == that.isSound
                && isVibro == that.isVibro
                && isPayments == that.isPayments
                && isMaintenance == that.isMaintenance
                && isTop == that.isTop
                && isBottom == that.isBottom
                && isStrix == that.isStrix
                && minerId == that.minerId
                && currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, theme, defaultTab, defaultLanguage, isSound, isVibro, isPayments,
                isMaintenance, isTop, isBottom, isStrix, minerId, currency);
    }

    @Override
    public String toString() {
        return "ConfigurationEntity(id=" + id + ", theme=" + theme + ", defaultTab=" + defaultTab
                + ", defaultLanguage=" + defaultLanguage + ", isSound=" + isSound + ", isVibro=" + isVibro
                + ", isPayments=" + isPayments + ", isMaintenance=" + isMaintenance + ", isTop=" + isTop
                + ", isBottom=" + isBottom + ", isStrix=" + isStrix + ", minerId=" + minerId
                + ", currency=" + currency + ")";
    }
}
