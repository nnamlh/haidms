package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 10/2/2017.
 */

public class CodeSendResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("codes")
    private List<GeneralInfo> codes;

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


    public List<GeneralInfo> getCodes() {
        return codes;
    }

    public void setCodes(List<GeneralInfo> codes) {
        this.codes = codes;
    }
}
