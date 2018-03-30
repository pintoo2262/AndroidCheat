package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by smn on 20/11/17.
 */

public class SizeModel implements Serializable {

    @SerializedName("id")
    String SizeId;
    @SerializedName("size")
    String SizeValue;
    private boolean isSelected = false;

    public String getSizeId() {
        return SizeId;
    }

    public void setSizeId(String sizeId) {
        SizeId = sizeId;
    }

    public String getSizeValue() {
        return SizeValue;
    }

    public void setSizeValue(String sizeValue) {
        SizeValue = sizeValue;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }


}
