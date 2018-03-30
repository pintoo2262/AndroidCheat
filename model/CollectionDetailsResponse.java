package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 30/1/18.
 */

public class CollectionDetailsResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("collection_data")
    Collection collectionl;
    @SerializedName("data")
    @Expose
    private List<ProductCollection> data = null;

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

    public Collection getCollectionl() {
        return collectionl;
    }

    public void setCollectionl(Collection collectionl) {
        this.collectionl = collectionl;
    }

    public List<ProductCollection> getData() {
        return data;
    }

    public void setData(List<ProductCollection> data) {
        this.data = data;
    }
}
