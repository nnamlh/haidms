package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 2/28/2018.
 */

public class UpdateDeliveryResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("finish")
    private int finish;

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

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
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
