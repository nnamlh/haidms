package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.PUT;

/**
 * Created by HAI on 9/4/2017.
 */

public class CalendarStatus {

    @SerializedName("Id")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Compel")
    private int compel;

    @SerializedName("Number")
    private int number;

    @SerializedName("TGroup")
    private String tGroup;

    @SerializedName("Notes")
    private String notes;

    @SerializedName("GroupType")
    private String GroupType;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCompel() {
        return compel;
    }

    public void setCompel(int compel) {
        this.compel = compel;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String gettGroup() {
        return tGroup;
    }

    public void settGroup(String tGroup) {
        this.tGroup = tGroup;
    }

    public String getGroupType() {
        return GroupType;
    }

    public void setGroupType(String groupType) {
        GroupType = groupType;
    }
}
