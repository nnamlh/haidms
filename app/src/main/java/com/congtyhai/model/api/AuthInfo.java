package com.congtyhai.model.api;

/**
 * Created by HAI on 10/2/2017.
 */

public class AuthInfo {
    private String user;

    private String token;


    public AuthInfo(String user, String token) {
        this.user = user;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
