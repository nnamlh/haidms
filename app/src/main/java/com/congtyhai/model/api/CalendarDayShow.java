package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarDayShow {

    @SerializedName("day")
    private int day ;
    @SerializedName("status")
    private String status;
    @SerializedName("statusName")
    private String statusName ;
    @SerializedName("notes")
    private String notes ;
    @SerializedName("calendar")
    private List<CalendarShowAgency> calendar ;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<CalendarShowAgency> getCalendar() {
        return calendar;
    }

    public void setCalendar(List<CalendarShowAgency> calendar) {
        this.calendar = calendar;
    }
}
