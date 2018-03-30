package com.app.noan.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 2/11/17.
 */

public class ProductResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    @Expose
    private List<Product> productList = new ArrayList<>();
    @SerializedName("metadata")
    public MetaData metaData;

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


    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }


}
