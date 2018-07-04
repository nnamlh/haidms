package com.congtyhai.model.api;

/**
 * Created by HAI on 9/9/2017.
 */

public class DecorImageSend {
    private String user;
    private String token;
    private String checkInId;

    private String group ;

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


    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getCheckInId() {
        return checkInId;
    }

    public void setCheckInId(String checkInId) {
        this.checkInId = checkInId;
    }
}
