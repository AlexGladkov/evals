package com.dev.nicehash.data.room.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dev.nicehash.contracts.RoomContract;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 11.08.18.
 * Data model for db currency
 */
@Entity(tableName = RoomContract.TABLE_CURRENCY)
public class ExchangeRateEntity {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Long id;
    private final double value;
    private final String code;

    public ExchangeRateEntity(@Nullable Long id, double value, String code) {
        this.id = id;
        this.value = value;
        this.code = code;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExchangeRateEntity)) return false;
        ExchangeRateEntity that = (ExchangeRateEntity) o;
        return Double.compare(that.value, value) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, code);
    }

    @Override
    public String toString() {
        return "ExchangeRateEntity(id=" + id + ", value=" + value + ", code=" + code + ")";
    }
}
