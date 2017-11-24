package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 8/22/2017.
 */

public class ProductCodeInfo {
    
    @SerializedName("id")
    private String id ;
    @SerializedName("code")
    private String code ;
    @SerializedName("barcode")
    private String barcode ;
    @SerializedName("name")
    private String name ;
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

    @SerializedName("short_describe")
    private String short_describe;
    @SerializedName("price")
    private double price;

    @SerializedName("unit")
    private String unit ;
    @SerializedName("quantity_box")
    private int quantity_box;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("vat")
    private double vat;


    public String getShort_describe() {
        return short_describe;
    }

    public void setShort_describe(String short_describe) {
        this.short_describe = short_describe;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantity_box() {
        return quantity_box;
    }

    public void setQuantity_box(int quantity_box) {
        this.quantity_box = quantity_box;
    }

    public double getVat() {
        return vat;
    }

    public void setVat(double vat) {
        this.vat = vat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
