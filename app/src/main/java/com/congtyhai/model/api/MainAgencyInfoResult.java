package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by HAI on 10/3/2017.
 */

public class MainAgencyInfoResult {

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

    @SerializedName("products")
    private ProductCodeInfo[] products;

    @SerializedName("productGroups")
    private  GroupResultInfo[] productGroups;

    @SerializedName("c2")
    private C2C1Info[] c2;

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

    public ProductCodeInfo[] getProducts() {
        return products;
    }

    public void setProducts(ProductCodeInfo[] products) {
        this.products = products;
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

    public C2C1Info[] getC2() {
        return c2;
    }

    public void setC2(C2C1Info[] c2) {
        this.c2 = c2;
    }
}
