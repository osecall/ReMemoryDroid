package com.rememorydroid.oriolsecall.rememorydroid;

/**
 * Created by Oriol on 08/04/2017.
 */

public class VersioList {
    String WeekDay, Versio;

    public VersioList(String weekDay, String versio) {
        WeekDay = weekDay;
        Versio = versio;
    }

    public String getWeekDay() {
        return WeekDay;
    }

    public void setWeekDay(String weekDay) {
        WeekDay = weekDay;
    }

    public String getVersio() {
        return Versio;
    }

    public void setVersio(String versio) {
        Versio = versio;
    }
}
