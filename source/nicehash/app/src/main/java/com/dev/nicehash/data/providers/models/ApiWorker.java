package com.dev.nicehash.data.providers.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 06.08.18.
 * Data model class for backend workers
 */
public class ApiWorker {
    private final String title;
    private final int time;

    public ApiWorker(String title, int time) {
        this.title = title;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApiWorker)) return false;
        ApiWorker apiWorker = (ApiWorker) o;
        return time == apiWorker.time && Objects.equals(title, apiWorker.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, time);
    }

    @Override
    public String toString() {
        return "ApiWorker(title=" + title + ", time=" + time + ")";
    }
}
