package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarShowResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("hasApprove")
    private int hasApprove ;
    @SerializedName("notes")
    private String notes;
    @SerializedName("month")
    private int month ;
    @SerializedName("year")
    private int year;
    @SerializedName("items")
    private List<CalendarDayShow> items ;

    @SerializedName("typeDetail")
    private List<CalendarShowStatusDetail> statusDetails;

    public int getHasApprove() {
        return hasApprove;
    }

    public void setHasApprove(int hasApprove) {
        this.hasApprove = hasApprove;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<CalendarDayShow> getItems() {
        return items;
    }

    public void setItems(List<CalendarDayShow> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CalendarShowStatusDetail> getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(List<CalendarShowStatusDetail> statusDetails) {
        this.statusDetails = statusDetails;
    }
}
