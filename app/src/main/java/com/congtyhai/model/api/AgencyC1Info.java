package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 8/23/2017.
 */

public class AgencyC1Info {

    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;
    @SerializedName("deputy")
    private String deputy;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }
}
