package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/15/2017.
 */

public class NotificationInfoResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;
    @SerializedName("data")
    private List<NotificationInfo> data;
    @SerializedName("page")
    private int page;


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

    public List<NotificationInfo> getData() {
        return data;
    }

    public void setData(List<NotificationInfo> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}