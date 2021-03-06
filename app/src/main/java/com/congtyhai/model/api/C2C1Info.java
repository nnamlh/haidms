package com.congtyhai.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 10/30/2017.
 */

public class C2C1Info {

    @SerializedName("code")
    private String code;
    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("deputy")
    private String deputy;

    @SerializedName("taxCode")
    private String taxCode;

    @SerializedName("province")
    private String province ;

    @SerializedName("district")
    private String district ;

    @SerializedName("identityCard")
    private String identityCard ;

    @SerializedName("businessLicense")
    private String businessLicense ;
    @SerializedName("ward")
    private String ward;

    @SerializedName("country")
    private String country;

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeputy() {
        return deputy;
    }

    public void setDeputy(String deputy) {
        this.deputy = deputy;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
