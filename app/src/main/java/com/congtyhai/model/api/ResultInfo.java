package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 8/14/2017.
 */

public class ResultInfo {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;


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

}
