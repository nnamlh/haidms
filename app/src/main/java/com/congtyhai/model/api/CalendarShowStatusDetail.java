package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 9/5/2017.
 */

public class CalendarShowStatusDetail {

    @SerializedName("typeId")
    private String status;
    @SerializedName("typeName")
    private String statusName;
    @SerializedName("number")
    private String number;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
