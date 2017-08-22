package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 8/22/2017.
 */

public class AgencyInfo {
    @SerializedName("code")
    private String code ;
    @SerializedName("name")
    private String name ;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id ;

    @SerializedName("lat")
    private double lat ;

    @SerializedName("lng")
    private double lng ;

    @SerializedName("address")
    private String address ;

    @SerializedName("phone")
    private String phone ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
