package com.congtyhai.model.api.order;

import java.util.List;

/**
 * Created by HAI on 10/24/2017.
 */

public class OrderCompleteSend {

    private String user;
    private String token;

    private String code ;

    private String shipType;

    private String payType ;

    private String phone ;

    private String address ;

    private List<OrderProductSend> product ;

    private String timeSuggest ;

    private String notes;

    private String orderType ;

    private int inCheckIn ;

    private String c1;

    private int debtTime;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderProductSend> getProduct() {
        return product;
    }

    public void setProduct(List<OrderProductSend> product) {
        this.product = product;
    }

    public String getTimeSuggest() {
        return timeSuggest;
    }

    public void setTimeSuggest(String timeSuggest) {
        this.timeSuggest = timeSuggest;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getInCheckIn() {
        return inCheckIn;
    }

    public void setInCheckIn(int inCheckIn) {
        this.inCheckIn = inCheckIn;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }


    public int getDebtTime() {
        return debtTime;
    }

    public void setDebtTime(int debtTime) {
        this.debtTime = debtTime;
    }
}
