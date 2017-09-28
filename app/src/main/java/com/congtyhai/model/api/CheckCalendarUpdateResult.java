package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/28/2017.
 */

public class CheckCalendarUpdateResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

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

    public List<CalendarStatus> getStatus() {
        return status;
    }

    public void setStatus(List<CalendarStatus> status) {
        this.status = status;
    }
}
