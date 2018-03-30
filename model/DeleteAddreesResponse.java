package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 30/11/17.
 */

public class DeleteAddreesResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;

    @SerializedName("data")
    private List<StoreAddress> storeAddressList = new ArrayList<>();

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
}
