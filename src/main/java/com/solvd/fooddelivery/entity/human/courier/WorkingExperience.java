package com.solvd.fooddelivery.entity.human.courier;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "workingExperience")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkingExperience {

    private int years;
    private int months;
    private int days;

    public void addDays(int days) {
        this.days += days;
        normalize();
    }

    public void addMonths(int months) {
        this.months += months;
        normalize();
    }

    public void addYears(int years) {
        this.years += years;
        normalize();
    }

    private void normalize() {
        if (days >= 30) {
            months += days / 30;
            days = days % 30;
        }
        if (months >= 12) {
            years += months / 12;
            months = months % 12;
        }
    }

    public String getWorkingPeriod() {
        return new String(years + " years "
                + months + " months " + days + " days.");
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
