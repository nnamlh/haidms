package com.congtyhai.model.api;

import java.util.List;

/**
 * Created by HAI on 9/25/2017.
 */

public class ProductOrderSend {
    private List<ProductOrder> orders;
    private String token;
    private String user;
    private String agency;

    public List<ProductOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<ProductOrder> orders) {
        this.orders = orders;
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

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
