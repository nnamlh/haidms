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

    @SerializedName("time")
    private String time ;

    @SerializedName("notes")
    private String notes ;

    @SerializedName("gifts")
    private List<OrderEventGift> gifts ;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<OrderEventGift> getGifts() {
        return gifts;
    }

    public void setGifts(List<OrderEventGift> gifts) {
        this.gifts = gifts;
    }
}
