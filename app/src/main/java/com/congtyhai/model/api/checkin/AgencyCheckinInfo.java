package com.congtyhai.model.api.checkin;

import com.congtyhai.model.api.SubOwner;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 11/23/2017.
 */

public class AgencyCheckinInfo {

    @SerializedName("deputy")
    private String deputy;
    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;
    @SerializedName("inPlan")
    public int inPlan;
    @SerializedName("lng")
    public double lng;
    @SerializedName("lat")
    public double lat;
    @SerializedName("ctype")
    private String ctype;
    @SerializedName("cname")
    private String cname;

    @SerializedName("agencyType")
    private String agencyType;

    @SerializedName("content")
    private String content;

    @SerializedName("checkInId")
    private  String checkInId;

    @SerializedName("c1")
    private List<SubOwner> c1;
    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

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

    public int getInPlan() {
        return inPlan;
    }

    public void setInPlan(int inPlan) {
        this.inPlan = inPlan;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public List<SubOwner> getC1() {
        return c1;
    }

    public void setC1(List<SubOwner> c1) {
        this.c1 = c1;
    }

    public String getAgencyType() {
        return agencyType;
    }

    public void setAgencyType(String agencyType) {
        this.agencyType = agencyType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(String checkInId) {
        this.checkInId = checkInId;
    }
}
