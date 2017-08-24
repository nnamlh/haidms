package com.congtyhai.model.app;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarCreateReviewInfo {

    private int day;
    private int type;
    // 1: hien ngay
    // 2: không check in
    // 3: hien khach hang
    private String deputy;
    private String name;
    private String status;
    private String notes;

    public CalendarCreateReviewInfo(int day, int type, String status, String notes) {
        this.day = day;
        this.type = type;
        this.status = status;
        this.notes = notes;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
