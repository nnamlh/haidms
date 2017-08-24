package com.congtyhai.model.api;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarDayCreate {

    private int day;

    private List<String> agencies;

    private String status;

    private String notes;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) {
        this.agencies = agencies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
