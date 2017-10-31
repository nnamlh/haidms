package com.congtyhai.model.api.order;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 11/1/2017.
 */

public class OrderProductHistory {
    @SerializedName("date")
    private String date ;
    @SerializedName("quantity")
    private int quantity ;
    @SerializedName("notes")
    private String notes ;
    @SerializedName("unit")
    private String unit ;
    @SerializedName("quantityBox")
    private int quantityBox ;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
