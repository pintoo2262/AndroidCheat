package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 16/1/18.
 */

public class NeedViewResponse {

    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    String data;


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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
