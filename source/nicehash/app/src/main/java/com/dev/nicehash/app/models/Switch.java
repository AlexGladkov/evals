package com.dev.nicehash.app.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 25.06.18.
 * Data class for switchable cells
 */
public class Switch {
    private final int id;
    private final String title;
    private boolean isChecked;

    public Switch(int id, String title, boolean isChecked) {
        this.id = id;
        this.title = title;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Switch)) return false;
        Switch aSwitch = (Switch) o;
        return id == aSwitch.id
                && isChecked == aSwitch.isChecked
                && Objects.equals(title, aSwitch.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, isChecked);
    }

    @Override
    public String toString() {
        return "Switch(id=" + id + ", title=" + title + ", isChecked=" + isChecked + ")";
    }
}
