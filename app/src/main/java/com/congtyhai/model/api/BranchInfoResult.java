package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 10/4/2017.
 */

public class BranchInfoResult {
    @SerializedName("code")
    private String code ;
    @SerializedName("phone")
    private String phone ;
    @SerializedName("name")
    private String name ;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lng")
    private double lng ;
    @SerializedName("address")
    private String address ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
