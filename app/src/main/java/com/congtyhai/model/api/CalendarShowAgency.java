package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 8/25/2017.
 */

public class CalendarShowAgency {

    @SerializedName("deputy")
    private String deputy ;
    @SerializedName("code")
    private String code ;
    @SerializedName("name")
    private String name ;
    @SerializedName("ctype")
    private String ctype ;
    @SerializedName("inPlan")
    private int inPlan ;
    @SerializedName("perform")
    private int perform ;

    @SerializedName("ctypename")
    private String ctypename;


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

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public int getInPlan() {
        return inPlan;
    }

    public void setInPlan(int inPlan) {
        this.inPlan = inPlan;
    }

    public int getPerform() {
        return perform;
    }

    public void setPerform(int perform) {
        this.perform = perform;
    }

    public String getCtypename() {
        return ctypename;
    }

    public void setCtypename(String ctypename) {
        this.ctypename = ctypename;
    }
}
