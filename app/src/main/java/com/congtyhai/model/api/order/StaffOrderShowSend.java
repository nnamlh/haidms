package com.congtyhai.model.api.order;

/**
 * Created by HAI on 11/2/2017.
 */

public class StaffOrderShowSend {
    private String user;
    private String token;
    private String c2Code ;

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

    public String getC2Code() {
        return c2Code;
    }

    public void setC2Code(String c2Code) {
        this.c2Code = c2Code;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
