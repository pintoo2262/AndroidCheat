package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 2/1/18.
 */

public class ColorResponseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    List<ColorModel> colorModelList;

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


    public List<ColorModel> getColorModelList() {
        return colorModelList;
    }

    public void setColorModelList(List<ColorModel> colorModelList) {
        this.colorModelList = colorModelList;
    }

}
