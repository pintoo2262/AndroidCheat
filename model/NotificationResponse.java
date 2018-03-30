package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 20/12/17.
 */

public class NotificationResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    @Expose
    List<NotificationModel> mNotificationResponseList;

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

    public List<NotificationModel> getmNotificationResponseList() {
        return mNotificationResponseList;
    }

    public void setmNotificationResponseList(List<NotificationModel> mNotificationResponseList) {
        this.mNotificationResponseList = mNotificationResponseList;
    }


}
