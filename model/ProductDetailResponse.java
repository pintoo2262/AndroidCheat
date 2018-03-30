package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 6/11/17.
 */

public class ProductDetailResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    @Expose
    Product product;
    @SerializedName("imagedata")
    @Expose
    private List<Product> productMoreImageList;
    @SerializedName("moreproductdata")
    @Expose
    private List<Product> moreProductImageList;


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


    public List<Product> getProductMoreImageList() {
        return productMoreImageList;
    }

    public void setProductMoreImageList(List<Product> productMoreImageList) {
        this.productMoreImageList = productMoreImageList;
    }


    public List<Product> getMoreProductImageList() {
        return moreProductImageList;
    }

    public void setMoreProductImageList(List<Product> moreProductImageList) {
        this.moreProductImageList = moreProductImageList;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}


