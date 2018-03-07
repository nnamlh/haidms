package com.congtyhai.model.api.order;

/**
 * Created by HAI on 11/2/2017.
 */

public class StaffOrderShowSend {
    private String user;
    private String token;
    private String c1Code ;
    private String fdate;
    private String tdate;
    private String place;
    private String status;
    private String processId;



    public String getC1Code() {
        return c1Code;
    }

    public void setC1Code(String c1Code) {
        this.c1Code = c1Code;
    }

    public String getFdate() {
        return fdate;
    }

    public void setFdate(String fdate) {
        this.fdate = fdate;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    private int page;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
