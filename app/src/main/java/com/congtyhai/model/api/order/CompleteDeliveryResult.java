package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 2/22/2018.
 */

public class CompleteDeliveryResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;


    @SerializedName("products")
    private List<OrderProductResult> products;

    @SerializedName("deliveryStatus")
    private String deliveryStatus;

    @SerializedName("deliveryStatusCode")
    private String deliveryStatusCode;


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

    public List<OrderProductResult> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductResult> products) {
        this.products = products;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getDeliveryStatusCode() {
        return deliveryStatusCode;
    }

    public void setDeliveryStatusCode(String deliveryStatusCode) {
        this.deliveryStatusCode = deliveryStatusCode;
    }
}
