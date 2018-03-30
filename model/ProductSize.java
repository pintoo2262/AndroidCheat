package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by smn on 8/11/17.
 */

public class ProductSize {
    @SerializedName("size")
    private String size;
    @SerializedName("size_id")
    private String sizeId;
    @SerializedName("seller_data")
    @Expose
    private List<SelllerProduct> sellerData = null;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public List<SelllerProduct> getSellerData() {
        return sellerData;
    }

    public void setSellerData(List<SelllerProduct> sellerData) {

        this.sellerData = sellerData;
    }


}
