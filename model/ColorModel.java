package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by smn on 5/12/17.
 */

public class ColorModel implements Serializable {
    @SerializedName("id")
    String colorId;
    @SerializedName("color_code")
    String colorCode;
    private boolean isSelected = false;


    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}


