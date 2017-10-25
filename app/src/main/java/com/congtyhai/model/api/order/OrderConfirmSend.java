package com.congtyhai.model.api.order;

import java.util.List;

/**
 * Created by HAI on 10/23/2017.
 */

public class OrderConfirmSend {
    private String user;
    private String token;
    private String agency;
    private List<OrderProductSend> product;

    public String getAgency() {
        return agency;
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

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public List<OrderProductSend> getProduct() {
        return product;
    }

    public void setProduct(List<OrderProductSend> product) {
        this.product = product;
    }
}
