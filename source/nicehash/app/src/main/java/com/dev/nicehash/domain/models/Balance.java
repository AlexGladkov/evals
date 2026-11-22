package com.dev.nicehash.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Model for balance cell (domain)
 */
public class Balance implements Parcelable {
    private final int id;
    private final String title;
    private float value;
    private final double btc;
    private float incomeValue;
    private final double incomeBtc;
    private final String suffix;

    public Balance(int id, String title, float value, double btc,
                   float incomeValue, double incomeBtc, String suffix) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.btc = btc;
        this.incomeValue = incomeValue;
        this.incomeBtc = incomeBtc;
        this.suffix = suffix;
    }

    protected Balance(Parcel parcel) {
        this.id = parcel.readInt();
        String parcelTitle = parcel.readString();
        this.title = parcelTitle != null ? parcelTitle : "";
        this.value = parcel.readFloat();
        this.btc = parcel.readDouble();
        this.incomeValue = parcel.readFloat();
        this.incomeBtc = parcel.readDouble();
        String parcelSuffix = parcel.readString();
        this.suffix = parcelSuffix != null ? parcelSuffix : "";
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public double getBtc() {
        return btc;
    }

    public float getIncomeValue() {
        return incomeValue;
    }

    public void setIncomeValue(float incomeValue) {
        this.incomeValue = incomeValue;
    }

    public double getIncomeBtc() {
        return incomeBtc;
    }

    public String getSuffix() {
        return suffix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Balance)) return false;
        Balance that = (Balance) o;
        return id == that.id
                && Float.compare(that.value, value) == 0
                && Double.compare(that.btc, btc) == 0
                && Float.compare(that.incomeValue, incomeValue) == 0
                && Double.compare(that.incomeBtc, incomeBtc) == 0
                && Objects.equals(title, that.title)
                && Objects.equals(suffix, that.suffix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, value, btc, incomeValue, incomeBtc, suffix);
    }

    @Override
    public String toString() {
        return "Balance(id=" + id + ", title=" + title + ", value=" + value + ", btc=" + btc
                + ", incomeValue=" + incomeValue + ", incomeBtc=" + incomeBtc + ", suffix=" + suffix + ")";
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeFloat(value);
        parcel.writeDouble(btc);
        parcel.writeFloat(incomeValue);
        parcel.writeDouble(incomeBtc);
        parcel.writeString(suffix);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Balance> CREATOR = new Parcelable.Creator<Balance>() {
        @Override
        public Balance createFromParcel(Parcel parcel) {
            return new Balance(parcel);
        }

        @Override
        public Balance[] newArray(int size) {
            return new Balance[size];
        }
    };
}
