package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 10/31/2017.
 */

public class OrderProductResult {

    @SerializedName("orderId")
    private String orderId;
    @SerializedName("productId")
    private String productId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("quantityFinish")
    private int quantityFinish;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("c1Id")
    private String c1Id;
    @SerializedName("c1Code")
    private String c1Code;
    @SerializedName("c1Store")
    private String c1Store;
    @SerializedName("c1Address")
    private String c1Address;
    @SerializedName("c1Phone")
    private String c1Phone;
    @SerializedName("price")
    private double price ;
    @SerializedName("perPrice")
    private double perPrice ;
    @SerializedName("unit")
    private String unit ;
    @SerializedName("quantityBox")
    private int quantityBox ;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(double perPrice) {
        this.perPrice = perPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getQuantityBox() {
        return quantityBox;
    }

    public void setQuantityBox(int quantityBox) {
        this.quantityBox = quantityBox;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantityFinish() {
        return quantityFinish;
    }

    public void setQuantityFinish(int quantityFinish) {
        this.quantityFinish = quantityFinish;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getC1Id() {
        return c1Id;
    }

    public void setC1Id(String c1Id) {
        this.c1Id = c1Id;
    }

    public String getC1Code() {
        return c1Code;
    }

    public void setC1Code(String c1Code) {
        this.c1Code = c1Code;
    }

    public String getC1Store() {
        return c1Store;
    }

    public void setC1Store(String c1Store) {
        this.c1Store = c1Store;
    }

    public String getC1Address() {
        return c1Address;
    }

    public void setC1Address(String c1Address) {
        this.c1Address = c1Address;
    }

    public String getC1Phone() {
        return c1Phone;
    }

    public void setC1Phone(String c1Phone) {
        this.c1Phone = c1Phone;
    }
}
