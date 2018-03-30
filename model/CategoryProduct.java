package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by smn on 17/11/17.
 */

public class CategoryProduct implements Serializable {

    @SerializedName("id")
    String category_Id;
    @SerializedName("name")
    String category_Name;
    @SerializedName("parent_id")
    String category_ParentId;
    @SerializedName("description")
    String category_Descrption;
    @SerializedName("image")
    String category_image;
    private boolean isSelected = false;

    public String getCategory_Id() {
        return category_Id;
    }

    public void setCategory_Id(String category_Id) {
        this.category_Id = category_Id;
    }


    public String getCategory_Name() {
        return category_Name;
    }

    public void setCategory_Name(String category_Name) {
        this.category_Name = category_Name;
    }

    public String getCategory_ParentId() {
        return category_ParentId;
    }

    public void setCategory_ParentId(String category_ParentId) {
        this.category_ParentId = category_ParentId;
    }

    public String getCategory_Descrption() {
        return category_Descrption;
    }

    public void setCategory_Descrption(String category_Descrption) {
        this.category_Descrption = category_Descrption;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
