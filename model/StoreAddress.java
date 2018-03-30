package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 6/11/17.
 */

public class StoreAddress {


    @SerializedName("id")
    int id;
    @SerializedName("user_id")
    String userId;
    @SerializedName("full_name")
    String fullName;
    @SerializedName("phone")
    String phoneNo;
    @SerializedName("address")
    String address;
    @SerializedName("apt_unit")
    String apiUnit;
    @SerializedName("city")
    String city;
    @SerializedName("state")
    String state;
    @SerializedName("shipping_rate")
    String shippingRate;
    @SerializedName("country")
    String country;
    @SerializedName("postal_code")
    String postal_code;
    @SerializedName("default_address")
    String default_addres;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApiUnit() {
        return apiUnit;
    }

    public void setApiUnit(String apiUnit) {
        this.apiUnit = apiUnit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }


    public String getDefault_addres() {
        return default_addres;
    }

    public void setDefault_addres(String default_addres) {
        this.default_addres = default_addres;
    }

    public String getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(String shippingRate) {
        this.shippingRate = shippingRate;
    }

}
