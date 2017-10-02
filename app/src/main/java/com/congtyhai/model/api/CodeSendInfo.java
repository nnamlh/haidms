package com.congtyhai.model.api;

/**
 * Created by HAI on 10/2/2017.
 */

public class CodeSendInfo {
    private String[] codes;

    private String user;

    private String token;


    public CodeSendInfo(String user, String token, String[] codes) {
        this.user = user;
        this.token = token;
        this.codes = codes;
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

    public String[] getCodes() {
        return codes;
    }

    public void setCodes(String[] codes) {
        this.codes = codes;
    }
}
