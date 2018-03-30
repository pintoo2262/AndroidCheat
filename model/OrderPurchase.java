package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 16/11/17.
 */

public class OrderPurchase {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;

    @SerializedName("data")
    @Expose
    private List<SelllerProduct> selllerProductList = new ArrayList<>();
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

    public List<SelllerProduct> getSelllerProductList() {
        return selllerProductList;
    }

    public void setSelllerProductList(List<SelllerProduct> selllerProductList) {
        this.selllerProductList = selllerProductList;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }


}
