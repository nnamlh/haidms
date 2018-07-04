package com.congtyhai.model.api;

/**
 * Created by HAI on 9/5/2017.
 */

public class CheckInTaskSend {
    private String user;
    private String token;

    private String code ;

    private String checkInId;

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

    public String getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(String checkInId) {
        this.checkInId = checkInId;
    }
}
