package com.congtyhai.model.api.order;

/**
 * Created by HAI on 11/5/2017.
 */

public class C2OrderShowSend {

    private String user;
    private String token;

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
}
