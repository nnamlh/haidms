package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 10/2/2017.
 */

public class ResultEvent {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("events")
    private ResultEventInfo[] events;

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


    public ResultEventInfo[] getEvents() {
        return events;
    }

    public void setEvents(ResultEventInfo[] events) {
        this.events = events;
    }
}
