package com.congtyhai.model.api;

/**
 * Created by HAI on 9/5/2017.
 */

public class CheckInTaskSend {
    private String user;
    private String token;

    private String code ;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
