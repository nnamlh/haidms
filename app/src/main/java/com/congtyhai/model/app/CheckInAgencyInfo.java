package com.congtyhai.model.app;

import com.congtyhai.model.api.AgencyC2C1;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CheckInAgencyInfo {

    private String code;
    private String name;
    private String deputy;
    private double distance;
    private List<AgencyC2C1> c1;
    private String checkInType;
    private String checkInName;
    private int inPlan;
    private int isShowType;

    public String getCheckInType() {
        return checkInType;
    }

    public void setCheckInType(String checkInType) {
        this.checkInType = checkInType;
    }

    public String getCheckInName() {
        return checkInName;
    }

    public void setCheckInName(String checkInName) {
        this.checkInName = checkInName;
    }

    public int getInPlan() {
        return inPlan;
    }

    public void setInPlan(int inPlan) {
        this.inPlan = inPlan;
    }

    public int getIsShowType() {
        return isShowType;
    }

    public void setIsShowType(int isShowType) {
        this.isShowType = isShowType;
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

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public CheckInAgencyInfo() {

    }

   public List<AgencyC2C1> getC1() {
        return c1;
    }

    public void setC1(List<AgencyC2C1> c1) {
        this.c1 = c1;
    }
}
