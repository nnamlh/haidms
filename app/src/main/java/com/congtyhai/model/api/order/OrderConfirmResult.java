package com.congtyhai.model.api.order;

import com.congtyhai.model.api.AgencyC2C1;
import com.congtyhai.model.api.TypeCommon;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 10/23/2017.
 */

public class OrderConfirmResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("events")
    private List<OrderEventInfo> events;
    @SerializedName("payType")
    private List<TypeCommon> payType;
    @SerializedName("shipType")
    private List<TypeCommon> shipType;
    @SerializedName("store")
    private String store ;
    @SerializedName("deputy")
    private String deputy ;
    @SerializedName("agencyCode")
    private String agencyCode ;
    @SerializedName("agencyId")
    private String agencyId;
    @SerializedName("phone")
    private String phone ;
    @SerializedName("address")
    private String address ;
    @SerializedName("c1")
    private List<AgencyC2C1> c1;

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

    public List<OrderEventInfo> getEvents() {
        return events;
    }

    public void setEvents(List<OrderEventInfo> events) {
        this.events = events;
    }

    public List<TypeCommon> getPayType() {
        return payType;
    }

    public void setPayType(List<TypeCommon> payType) {
        this.payType = payType;
    }

    public List<TypeCommon> getShipType() {
        return shipType;
    }

    public void setShipType(List<TypeCommon> shipType) {
        this.shipType = shipType;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
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

    public List<AgencyC2C1> getC1() {
        return c1;
    }

    public void setC1(List<AgencyC2C1> c1) {
        this.c1 = c1;
    }
}
