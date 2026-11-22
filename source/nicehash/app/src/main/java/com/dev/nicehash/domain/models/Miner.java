package com.dev.nicehash.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 23.06.18.
 * Data model for Miner entity in domain layer
 */
public class Miner implements Parcelable {
    private final int id;
    private final String name;
    private String hash;
    private boolean isSelected;

    public Miner(int id, String name, String hash, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.hash = hash;
        this.isSelected = isSelected;
    }

    protected Miner(Parcel parcel) {
        this.id = parcel.readInt();
        String parcelName = parcel.readString();
        this.name = parcelName != null ? parcelName : "";
        String parcelHash = parcel.readString();
        this.hash = parcelHash != null ? parcelHash : "";
        this.isSelected = parcel.readByte() != (byte) 0;
    }

    public static Miner getDefaultInstance() {
        return new Miner(-10, "Demo miner", "Demo miner", true);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Miner)) return false;
        Miner miner = (Miner) o;
        return id == miner.id
                && isSelected == miner.isSelected
                && Objects.equals(name, miner.name)
                && Objects.equals(hash, miner.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hash, isSelected);
    }

    @Override
    public String toString() {
        return "Miner(id=" + id + ", name=" + name + ", hash=" + hash + ", isSelected=" + isSelected + ")";
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(hash);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Miner> CREATOR = new Parcelable.Creator<Miner>() {
        @Override
        public Miner createFromParcel(Parcel parcel) {
            return new Miner(parcel);
        }

        @Override
        public Miner[] newArray(int size) {
            return new Miner[size];
        }
    };
}
