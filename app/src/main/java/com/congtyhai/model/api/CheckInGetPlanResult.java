package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CheckInGetPlanResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("inplan")
    private List<String> inplan ;
    @SerializedName("outplan")
    private List<String> outplan ;

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

    public List<String> getInplan() {
        return inplan;
    }

    public void setInplan(List<String> inplan) {
        this.inplan = inplan;
    }

    public List<String> getOutplan() {
        return outplan;
    }

    public void setOutplan(List<String> outplan) {
        this.outplan = outplan;
    }
}
