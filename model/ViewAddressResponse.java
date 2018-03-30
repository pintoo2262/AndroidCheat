package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 6/11/17.
 */

public class ViewAddressResponse {

    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    private List<StoreAddress> storeAddressList = new ArrayList<>();
    @SerializedName("wallet")
    String wallet;
    @SerializedName("paypal_email")
    @Expose
    private String paypalEmail;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoreAddress> getStoreAddressList() {
        return storeAddressList;
    }

    public void setStoreAddressList(List<StoreAddress> storeAddressList) {
        this.storeAddressList = storeAddressList;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }
}
