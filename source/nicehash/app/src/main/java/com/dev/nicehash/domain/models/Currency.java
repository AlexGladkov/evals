package com.dev.nicehash.domain.models;

import com.dev.nicehash.helpers.EnumCollections;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 25.07.18.
 * Model for currency (domain level)
 */
public class Currency {
    private final int id;
    private final int title;
    private final String btc;
    private final int header;
    private final int imgRes;
    private final EnumCollections.Currency prototype;

    public Currency(int id, int title, String btc, int header, int imgRes,
                    EnumCollections.Currency prototype) {
        this.id = id;
        this.title = title;
        this.btc = btc;
        this.header = header;
        this.imgRes = imgRes;
        this.prototype = prototype;
    }

    public int getId() {
        return id;
    }

    public int getTitle() {
        return title;
    }

    public String getBtc() {
        return btc;
    }

    public int getHeader() {
        return header;
    }

    public int getImgRes() {
        return imgRes;
    }

    public EnumCollections.Currency getPrototype() {
        return prototype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency that = (Currency) o;
        return id == that.id
                && title == that.title
                && header == that.header
                && imgRes == that.imgRes
                && Objects.equals(btc, that.btc)
                && prototype == that.prototype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, btc, header, imgRes, prototype);
    }

    @Override
    public String toString() {
        return "Currency(id=" + id + ", title=" + title + ", btc=" + btc + ", header=" + header
                + ", imgRes=" + imgRes + ", prototype=" + prototype + ")";
    }
}
