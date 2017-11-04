package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 11/5/2017.
 */

public class YourOrderShowResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("orders")
    private List<YourOrderInfo> orders;

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

    public List<YourOrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<YourOrderInfo> orders) {
        this.orders = orders;
    }
}
