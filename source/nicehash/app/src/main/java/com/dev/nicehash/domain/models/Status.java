package com.dev.nicehash.domain.models;

import com.dev.nicehash.helpers.EnumCollections;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 25.07.18.
 * Model for server status (domain layer)
 */
public class Status {
    private final int id;
    private final String header;
    private final String title;
    private final EnumCollections.Status status;

    public Status(int id, String header, String title, EnumCollections.Status status) {
        this.id = id;
        this.header = header;
        this.title = title;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getTitle() {
        return title;
    }

    public EnumCollections.Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Status)) return false;
        Status that = (Status) o;
        return id == that.id
                && Objects.equals(header, that.header)
                && Objects.equals(title, that.title)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, header, title, status);
    }

    @Override
    public String toString() {
        return "Status(id=" + id + ", header=" + header + ", title=" + title + ", status=" + status + ")";
    }
}
