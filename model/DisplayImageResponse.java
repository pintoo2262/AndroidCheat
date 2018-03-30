
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DisplayImageResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("imagedata")
    @Expose
    private List<Imagedatum> imagedata = null;
    @SerializedName("boximagedata")
    @Expose
    private List<Boximagedatum> boximagedata = null;

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

    public List<Imagedatum> getImagedata() {
        return imagedata;
    }

    public void setImagedata(List<Imagedatum> imagedata) {
        this.imagedata = imagedata;
    }

    public List<Boximagedatum> getBoximagedata() {
        return boximagedata;
    }

    public void setBoximagedata(List<Boximagedatum> boximagedata) {
        this.boximagedata = boximagedata;
    }
}
