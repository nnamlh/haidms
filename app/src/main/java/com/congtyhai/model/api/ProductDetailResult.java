package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HAI on 9/9/2017.
 */

public class ProductDetailResult {
    @SerializedName("id")
    private String id ;
    @SerializedName("code")
    private String code ;
    @SerializedName("barcode")
    private String barcode ;
    @SerializedName("name")
    private String name ;
    @SerializedName("producer")
    private String producer ;
    @SerializedName("commerceName")
    private String commerceName ;
    @SerializedName("activce")
    private String activce ;
    @SerializedName("isNew")
    private int isNew ;
    @SerializedName("isForcus")
    private int isForcus ;
    @SerializedName("groupId")
    private String groupId ;
    @SerializedName("groupName")
    private String groupName ;
    @SerializedName("image")
    private String image ;

    @SerializedName("unit")
    private String unit ;
    @SerializedName("describe")
    private String describe ;
    @SerializedName("uses")
    private String uses;
    @SerializedName("introduce")
    private String introduce ;
    @SerializedName("register")
    private String register ;
    @SerializedName("notes")
    private String notes;
    @SerializedName("other")
    private String other ;
    @SerializedName("images")
    private List<String> images ;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    private String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getCommerceName() {
        return commerceName;
    }

    public void setCommerceName(String commerceName) {
        this.commerceName = commerceName;
    }

    public String getActivce() {
        return activce;
    }

    public void setActivce(String activce) {
        this.activce = activce;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsForcus() {
        return isForcus;
    }

    public void setIsForcus(int isForcus) {
        this.isForcus = isForcus;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
