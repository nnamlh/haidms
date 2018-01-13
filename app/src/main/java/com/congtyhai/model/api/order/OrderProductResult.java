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

}
