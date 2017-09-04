package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by HAI on 8/22/2017.
 */

public class MainInfoResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("ecount")
    private int ecount;

    @SerializedName("topics")
    private String[] topics;

    @SerializedName("function")
    private String[] function;

    @SerializedName("agencies")
    private AgencyInfo[] agencies;

    @SerializedName("agencyc1")
    private AgencyC1Info[] agencyc1;

    @SerializedName("recivers")
    private ReceiveInfo[] recivers;

    @SerializedName("products")
    private ProductCodeInfo[] products;


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

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public int getEcount() {
        return ecount;
    }

    public void setEcount(int ecount) {
        this.ecount = ecount;
    }

    public String getFunction() {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(function));
        return  mJSONArray.toString();
    }

    public void setFunction(String[] function) {
        this.function = function;
    }


    public void setAgencies(AgencyInfo[] agencies) {
        this.agencies = agencies;
    }

    public void setRecivers(ReceiveInfo[] recivers) {
        this.recivers = recivers;
    }

    public AgencyInfo[] getAgencies() {
        return agencies;
    }

    public ReceiveInfo[] getRecivers() {
        return recivers;
    }

    public ProductCodeInfo[] getProducts() {
        return products;
    }

    public void setProducts(ProductCodeInfo[] products) {
        this.products = products;
    }

    public AgencyC1Info[] getAgencyc1() {
        return agencyc1;
    }

    public void setAgencyc1(AgencyC1Info[] agencyc1) {
        this.agencyc1 = agencyc1;
    }
}