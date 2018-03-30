package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 19/1/18.
 */

public class SubmitResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;

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


}
