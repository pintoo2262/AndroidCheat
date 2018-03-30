package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 21/12/17.
 */

public class ShareModel {

    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public String data;

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

