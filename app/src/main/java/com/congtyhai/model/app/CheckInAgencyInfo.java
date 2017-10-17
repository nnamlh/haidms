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
    private float distance;
    private List<AgencyC2C1> c1;

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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
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
