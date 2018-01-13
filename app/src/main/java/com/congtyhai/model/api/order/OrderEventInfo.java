package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 10/23/2017.
 */

public class OrderEventInfo {

    @SerializedName("id")
    private String id ;

    @SerializedName("name")
    private String name ;

    @SerializedName("point")
    private String point ;

    @SerializedName("hasPoint")
    private String hasPoint;

    @SerializedName("time")
    private String time ;

    @SerializedName("describe")
    private String describe ;

    @SerializedName("gift")
    private OrderEventGift gift ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(String hasPoint) {
        this.hasPoint = hasPoint;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public OrderEventGift getGift() {
        return gift;
    }

    public void setGift(OrderEventGift gift) {
        this.gift = gift;
    }
}
