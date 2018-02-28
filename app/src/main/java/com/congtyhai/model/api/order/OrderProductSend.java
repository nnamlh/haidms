package com.congtyhai.model.api.order;

/**
 * Created by HAI on 10/23/2017.
 */

public class OrderProductSend {
    private String code ;

    private int quantity ;

    private int hasBill;

//    private String c1 ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getHasBill() {
        return hasBill;
    }

    public void setHasBill(int hasBill) {
        this.hasBill = hasBill;
    }
}
