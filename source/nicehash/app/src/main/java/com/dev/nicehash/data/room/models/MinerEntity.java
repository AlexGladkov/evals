package com.dev.nicehash.data.room.models;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.dev.nicehash.contracts.RoomContract;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 21.06.18.
 * Entity describing CryptoMiner
 */
@Entity(tableName = RoomContract.TABLE_MINERS)
public class MinerEntity {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Integer id;
    private String name;
    private String hash;
    private final String payouts;

    public MinerEntity(@Nullable Integer id, String name, String hash, String payouts) {
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.payouts = payouts;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPayouts() {
        return payouts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MinerEntity)) return false;
        MinerEntity that = (MinerEntity) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(hash, that.hash)
                && Objects.equals(payouts, that.payouts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hash, payouts);
    }

    @Override
    public String toString() {
        return "MinerEntity(id=" + id + ", name=" + name + ", hash=" + hash + ", payouts=" + payouts + ")";
    }
}
