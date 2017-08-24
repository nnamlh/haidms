package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 8/24/2017.
 */

public class CheckCalendarResult  {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("month")
    private List<String> month;

    @SerializedName("status")
    private List<StatusInfo> status;

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

    public List<StatusInfo> getStatus() {
        return status;
    }

    public void setStatus(List<StatusInfo> status) {
        this.status = status;
    }
}
