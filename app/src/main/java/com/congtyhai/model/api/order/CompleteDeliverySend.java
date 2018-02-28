package com.congtyhai.model.api.order;

/**
 * Created by HAI on 2/22/2018.
 */

public class CompleteDeliverySend {
    private String user;
    private String token;

    private String orderId;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
