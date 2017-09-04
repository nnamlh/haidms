package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 9/4/2017.
 */

public class CalendarStatus {

    @SerializedName("Id")
    public String id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Compel")
    public int compel;

    @SerializedName("Number")
    public int number;

    @SerializedName("TGroup")
    public String tGroup;

    @SerializedName("Notes")
    public String notes;

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
}
