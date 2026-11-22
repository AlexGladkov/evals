package com.dev.nicehash.domain.models;

import java.util.Objects;

/**
 * Created by Alex Gladkov on 15.07.18.
 * Model for income tab (domain)
 */
public class Income {
    private double hourValue;
    private final double hourBtc;
    private double dayValue;
    private final double dayBtc;
    private double weekValue;
    private final double weekBtc;
    private double monthValue;
    private final double monthBtc;

    public Income(double hourValue, double hourBtc, double dayValue, double dayBtc,
                  double weekValue, double weekBtc, double monthValue, double monthBtc) {
        this.hourValue = hourValue;
        this.hourBtc = hourBtc;
        this.dayValue = dayValue;
        this.dayBtc = dayBtc;
        this.weekValue = weekValue;
        this.weekBtc = weekBtc;
        this.monthValue = monthValue;
        this.monthBtc = monthBtc;
    }

    public double getHourValue() {
        return hourValue;
    }

    public void setHourValue(double hourValue) {
        this.hourValue = hourValue;
    }

    public double getHourBtc() {
        return hourBtc;
    }

    public double getDayValue() {
        return dayValue;
    }

    public void setDayValue(double dayValue) {
        this.dayValue = dayValue;
    }

    public double getDayBtc() {
        return dayBtc;
    }

    public double getWeekValue() {
        return weekValue;
    }

    public void setWeekValue(double weekValue) {
        this.weekValue = weekValue;
    }

    public double getWeekBtc() {
        return weekBtc;
    }

    public double getMonthValue() {
        return monthValue;
    }

    public void setMonthValue(double monthValue) {
        this.monthValue = monthValue;
    }

    public double getMonthBtc() {
        return monthBtc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Income)) return false;
        Income income = (Income) o;
        return Double.compare(income.hourValue, hourValue) == 0
                && Double.compare(income.hourBtc, hourBtc) == 0
                && Double.compare(income.dayValue, dayValue) == 0
                && Double.compare(income.dayBtc, dayBtc) == 0
                && Double.compare(income.weekValue, weekValue) == 0
                && Double.compare(income.weekBtc, weekBtc) == 0
                && Double.compare(income.monthValue, monthValue) == 0
                && Double.compare(income.monthBtc, monthBtc) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hourValue, hourBtc, dayValue, dayBtc, weekValue, weekBtc, monthValue, monthBtc);
    }

    @Override
    public String toString() {
        return "Income(hourValue=" + hourValue + ", hourBtc=" + hourBtc + ", dayValue=" + dayValue
                + ", dayBtc=" + dayBtc + ", weekValue=" + weekValue + ", weekBtc=" + weekBtc
                + ", monthValue=" + monthValue + ", monthBtc=" + monthBtc + ")";
    }
}
