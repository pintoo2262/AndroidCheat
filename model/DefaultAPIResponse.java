package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 3/1/18.
 */

public class DefaultAPIResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("AllSize")
    List<SizeModel> sizeModelList = new ArrayList<>();
    @SerializedName("AllBrand")
    List<BrandModel> brandModelList = new ArrayList<>();
    @SerializedName("AllColor")
    List<ColorModel> colorModelList = new ArrayList<>();
    @SerializedName("Allcategories")
    List<CategoryProduct> categoryProductList = new ArrayList<>();





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

    public List<SizeModel> getSizeModelList() {
        return sizeModelList;
    }

    public void setSizeModelList(List<SizeModel> sizeModelList) {
        this.sizeModelList = sizeModelList;
    }

    public List<BrandModel> getBrandModelList() {
        return brandModelList;
    }

    public void setBrandModelList(List<BrandModel> brandModelList) {
        this.brandModelList = brandModelList;
    }

    public List<ColorModel> getColorModelList() {
        return colorModelList;
    }

    public void setColorModelList(List<ColorModel> colorModelList) {
        this.colorModelList = colorModelList;
    }

    public List<CategoryProduct> getCategoryProductList() {
        return categoryProductList;
    }

    public void setCategoryProductList(List<CategoryProduct> categoryProductList) {
        this.categoryProductList = categoryProductList;
    }




}
