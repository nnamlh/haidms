package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 11/5/2017.
 */

public class YourOrderInfo {
    @SerializedName("code")
    private String code ;
    @SerializedName("c2Code")
    private String c2Code ;
    @SerializedName("c2Name")
    private String c2Name ;
    @SerializedName("date")
    private String date ;
    @SerializedName("dateSuggest")
    private String dateSuggest ;
    @SerializedName("productCount")
    private int productCount ;
    @SerializedName("address")
    private String address ;
    @SerializedName("phone")
    private String phone ;
    @SerializedName("orderId")
    private String orderId;
    @SerializedName("status")
    private String status;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }

    public String getC2Name() {
        return c2Name;
    }

    public void setC2Name(String c2Name) {
        this.c2Name = c2Name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateSuggest() {
        return dateSuggest;
    }

    public void setDateSuggest(String dateSuggest) {
        this.dateSuggest = dateSuggest;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
