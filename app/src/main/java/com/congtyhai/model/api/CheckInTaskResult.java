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
    public int timeRemain;
    @SerializedName("tasks")
    public List<TaskInfoResult> tasks;
    @SerializedName("agencyCode")
    public String agencyCode;
    @SerializedName("agencyName")
    public String agencyName;
    @SerializedName("agencyDeputy")
    public String agencyDeputy;
    @SerializedName("inPlan")
    public int inPlan;

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
}
