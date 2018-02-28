package com.congtyhai.model.api.order;

import com.congtyhai.util.HAIRes;
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

    @SerializedName("statusCode")
    private String statusCode;

    @SerializedName("deliveryStatus")
    private String deliveryStatus;

    @SerializedName("deliveryStatusCode")
    private String deliveryStatusCode;

    @SerializedName("senderCode")
    private String senderCode;

    @SerializedName("senderName")
    private String senderName;

    @SerializedName("money")
    private double money;

    @SerializedName("countProduct")
    private int countProduct;

    @SerializedName("payInfo")
    private String payInfo;
    @SerializedName("shipInfo")
    private String shipInfo;
    @SerializedName("hasBill")
    private int hasBill;

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

    public String getSenderCode() {
        return senderCode;
    }

    public void setSenderCode(String senderCode) {
        this.senderCode = senderCode;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMoney() {
        return HAIRes.getInstance().formatMoneyToText(money);
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCountProduct() {
        return countProduct;
    }

    public void setCountProduct(int countProduct) {
        this.countProduct = countProduct;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getShipInfo() {
        return shipInfo;
    }

    public void setShipInfo(String shipInfo) {
        this.shipInfo = shipInfo;
    }

    public int getHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }
}
