package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 3/11/17.
 */

public class CheckEmailResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;


    @SerializedName("data")
    User data;


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

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }

}
