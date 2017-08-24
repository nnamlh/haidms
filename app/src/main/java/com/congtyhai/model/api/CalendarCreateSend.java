package com.congtyhai.model.api;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarCreateSend {

    private String user;

    private String token;

    private String notes ;

    private int month;

    private int year;

    private List<CalendarDayCreate> items ;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public List<CalendarDayCreate> getItems() {
        return items;
    }

    public void setItems(List<CalendarDayCreate> items) {
        this.items = items;
    }
}
