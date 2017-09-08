package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/8/2017.
 */

public class GroupResultInfo {

    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("childs")
    private GroupResultInfo[] childs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public GroupResultInfo[] getChilds() {
        return childs;
    }

    public void setChilds(GroupResultInfo[] childs) {
        this.childs = childs;
    }
}
