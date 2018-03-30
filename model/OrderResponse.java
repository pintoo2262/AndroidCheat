package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 13/11/17.
 */

public class OrderResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("wallet")
    public String userWallletBalance;
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

    public String getUserWallletBalance() {
        return userWallletBalance;
    }

    public void setUserWallletBalance(String userWallletBalance) {
        this.userWallletBalance = userWallletBalance;
    }
}
