package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 13/2/18.
 */

public class TagModel {
    @SerializedName("product_id")
    @Expose
    String product_id;
    @SerializedName("hashtag")
    @Expose
    String product_hasTag;


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_hasTag() {
        return product_hasTag;
    }

    public void setProduct_hasTag(String product_hasTag) {
        this.product_hasTag = product_hasTag;
    }
}
