package com.dev.nicehash.domain.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Model containing workers for worker tab
 */
public class WorkerHub {
    private final String title;
    private final String countryCode;
    private final List<Worker> workerTitles;

    public WorkerHub(String title, String countryCode, List<Worker> workerTitles) {
        this.title = title;
        this.countryCode = countryCode;
        this.workerTitles = workerTitles;
    }

    public String getTitle() {
        return title;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public List<Worker> getWorkerTitles() {
        return workerTitles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkerHub)) return false;
        WorkerHub workerHub = (WorkerHub) o;
        return Objects.equals(title, workerHub.title)
                && Objects.equals(countryCode, workerHub.countryCode)
                && Objects.equals(workerTitles, workerHub.workerTitles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, countryCode, workerTitles);
    }

    @Override
    public String toString() {
        return "WorkerHub(title=" + title + ", countryCode=" + countryCode
                + ", workerTitles=" + workerTitles + ")";
    }

    /**
     * Created by Alex Gladkov on 15.07.18.
     * Model for Worker Hub
     */
    public static class Worker {
        private final String title;
        private float difference;
        private final int min;
        private float upDynamic;
        private float downDynamic;

        public Worker(String title, float difference, int min, float upDynamic, float downDynamic) {
            this.title = title;
            this.difference = difference;
            this.min = min;
            this.upDynamic = upDynamic;
            this.downDynamic = downDynamic;
        }

        public String getTitle() {
            return title;
        }

        public float getDifference() {
            return difference;
        }

        public void setDifference(float difference) {
            this.difference = difference;
        }

        public int getMin() {
            return min;
        }

        public float getUpDynamic() {
            return upDynamic;
        }

        public void setUpDynamic(float upDynamic) {
            this.upDynamic = upDynamic;
        }

        public float getDownDynamic() {
            return downDynamic;
        }

        public void setDownDynamic(float downDynamic) {
            this.downDynamic = downDynamic;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Worker)) return false;
            Worker worker = (Worker) o;
            return Float.compare(worker.difference, difference) == 0
                    && min == worker.min
                    && Float.compare(worker.upDynamic, upDynamic) == 0
                    && Float.compare(worker.downDynamic, downDynamic) == 0
                    && Objects.equals(title, worker.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, difference, min, upDynamic, downDynamic);
        }

        @Override
        public String toString() {
            return "Worker(title=" + title + ", difference=" + difference + ", min=" + min
                    + ", upDynamic=" + upDynamic + ", downDynamic=" + downDynamic + ")";
        }
    }
}
