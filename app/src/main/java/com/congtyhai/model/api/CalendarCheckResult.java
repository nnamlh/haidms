package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/4/2017.
 */

public class CalendarCheckResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("month")
    private List<String> month;

    @SerializedName("status")
    private List<CalendarStatus> status;


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

    public List<String> getMonth() {
        return month;
    }

    public void setMonth(List<String> month) {
        this.month = month;
    }

    public List<CalendarStatus> getStatus() {
        return status;
    }

    public void setStatus(List<CalendarStatus> status) {
        this.status = status;
    }
}
