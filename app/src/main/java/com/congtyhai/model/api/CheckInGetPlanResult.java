package com.congtyhai.model.api;

import android.print.PageRange;

import com.congtyhai.model.api.checkin.AgencyCheckinInfo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 8/25/2017.
 */

public class CheckInGetPlanResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("checkin")
    private List<AgencyCheckinInfo> checkin ;
    @SerializedName("status")
    private List<CalendarStatus> status;

    @SerializedName("checkFlexible")
    private boolean checkFlexible;

    @SerializedName("notes")
    private String notes;

    @SerializedName("title")
    private String title;

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

    public List<CalendarStatus> getStatus() {
        return status;
    }

    public void setStatus(List<CalendarStatus> status) {
        this.status = status;
    }

    public List<AgencyCheckinInfo> getCheckin() {
        return checkin;
    }

    public void setCheckin(List<AgencyCheckinInfo> checkin) {
        this.checkin = checkin;
    }

    public boolean isCheckFlexible() {
        return checkFlexible;
    }

    public void setCheckFlexible(boolean checkFlexible) {
        this.checkFlexible = checkFlexible;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
