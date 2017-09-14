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

    @SerializedName("c2")
    private AgencyInfo[] agencies;

    @SerializedName("c1")
    private AgencyC1Info[] agencyc1;

    @SerializedName("products")
    private ProductCodeInfo[] products;

    @SerializedName("productGroups")
    private  GroupResultInfo[] productGroups;

    @SerializedName("name")
    private String name ;
    @SerializedName("code")
    private String code ;
    @SerializedName("type")
    private String type ;

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

    public AgencyInfo[] getAgencies() {
        return agencies;
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

    public GroupResultInfo[] getProductGroups() {
        return productGroups;
    }

    public void setProductGroups(GroupResultInfo[] productGroups) {
        this.productGroups = productGroups;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
