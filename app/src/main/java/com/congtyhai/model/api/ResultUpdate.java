package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 10/2/2017.
 */

public class ResultUpdate {
    @SerializedName("id")
    private String id ;
    @SerializedName("msg")
    private String msg ;
    @SerializedName("products")
    private List<GeneralInfo> products ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public List<GeneralInfo> getProducts() {
        return products;
    }

    public void setProducts(List<GeneralInfo> products) {
        this.products = products;
    }
}
