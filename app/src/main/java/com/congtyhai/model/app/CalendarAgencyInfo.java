package com.congtyhai.model.app;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarAgencyInfo {

    private String deputy;
    private String code;
    private String name;
    private int check;

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

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public CalendarAgencyInfo(String deputy, String code, String name, int check) {
        this.deputy = deputy;
        this.code = code;
        this.name = name;
        this.check = check;
    }

}
