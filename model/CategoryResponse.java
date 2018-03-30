package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 17/11/17.
 */

public class CategoryResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    @Expose
    private List<CategoryProduct> categoryProductList = new ArrayList<>();

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

    public List<CategoryProduct> getCategoryProductList() {
        return categoryProductList;
    }

    public void setCategoryProductList(List<CategoryProduct> categoryProductList) {
        this.categoryProductList = categoryProductList;
    }

}
