package com.congtyhai.model.api;

/**
 * Created by HAI on 8/25/2017.
 */

public class SendBasicInfo {
    private String user;

    private String token;

    public SendBasicInfo(String user, String token) {
        this.token = token;
        this.user = user;
    }

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
}
