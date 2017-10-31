package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 10/30/2017.
 */

public class C1OrderShowResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("orders")
    private List<C1OrderInfo> orders;

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

    public List<C1OrderInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<C1OrderInfo> orders) {
        this.orders = orders;
    }
}
