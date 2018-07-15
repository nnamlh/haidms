package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/5/2017.
 */

public class CheckInTaskResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;
    @SerializedName("timeRemain")
    private int timeRemain;
    @SerializedName("tasks")
    private List<TaskInfoResult> tasks;
    @SerializedName("agencyCode")
    private String agencyCode;
    @SerializedName("agencyName")
    private String agencyName;
    @SerializedName("agencyDeputy")
    private String agencyDeputy;
    @SerializedName("inPlan")
    private int inPlan;

    @SerializedName("flexible")
    private int flexible;

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

    public int getTimeRemain() {
        return timeRemain;
    }

    public void setTimeRemain(int timeRemain) {
        this.timeRemain = timeRemain;
    }

    public List<TaskInfoResult> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskInfoResult> tasks) {
        this.tasks = tasks;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public void setAgencyName(String agencyName) {
        this.agencyName = agencyName;
    }

    public String getAgencyDeputy() {
        return agencyDeputy;
    }

    public void setAgencyDeputy(String agencyDeputy) {
        this.agencyDeputy = agencyDeputy;
    }

    public int getInPlan() {
        return inPlan;
    }

    public void setInPlan(int inPlan) {
        this.inPlan = inPlan;
    }

    public int getFlexible() {
        return flexible;
    }

    public void setFlexible(int flexible) {
        this.flexible = flexible;
    }
}
