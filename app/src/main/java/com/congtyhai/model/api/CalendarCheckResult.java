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

    @SerializedName("max")
    private int max;

    @SerializedName("requireCheck")
    private boolean requireCheck;


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

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public boolean isRequireCheck() {
        return requireCheck;
    }

    public void setRequireCheck(boolean requireCheck) {
        this.requireCheck = requireCheck;
    }
}
