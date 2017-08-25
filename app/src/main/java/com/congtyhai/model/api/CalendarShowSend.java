package com.congtyhai.model.api;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarShowSend {

    private String user;

    private String token;

    private int month ;

    private int year ;

    public CalendarShowSend(int month, int year, String user, String token) {
        this.month = month;
        this.year = year;
        this.user = user;
        this.token = token;
    }

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
}
