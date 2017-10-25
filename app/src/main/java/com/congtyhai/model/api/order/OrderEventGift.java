package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 10/23/2017.
 */

public class OrderEventGift {

    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private String image;
    @SerializedName("point")
    private String point;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

}
