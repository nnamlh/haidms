package com.congtyhai.model.api;

/**
 * Created by HAI on 8/26/2017.
 */

public class CheckInSend {
    private String user;
    private String token;

    private String agency ;

    private double lat ;

    private double lng ;

    private double distance;

    private String notes;

    private String noteCode;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNoteCode() {
        return noteCode;
    }

    public void setNoteCode(String noteCode) {
        this.noteCode = noteCode;
    }
}
