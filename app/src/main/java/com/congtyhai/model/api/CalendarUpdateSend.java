package com.congtyhai.model.api;

import java.util.List;

/**
 * Created by HAI on 9/28/2017.
 */

public class CalendarUpdateSend {
    private String user;

    private String token;

    private int month;

    private int year;

    private CalendarDayCreate item ;

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

    public CalendarDayCreate getItem() {
        return item;
    }

    public void setItem(CalendarDayCreate item) {
        this.item = item;
    }
}
