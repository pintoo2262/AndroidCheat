package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 30/1/18.
 */

public class CollectionResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Collection> data = null;
    @SerializedName("AllproductsTag")
    @Expose
    private List<TagModel> collectionTagModelList = null;


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

    public List<Collection> getData() {
        return data;
    }

    public void setData(List<Collection> data) {
        this.data = data;
    }

    public List<TagModel> getCollectionTagModelList() {
        return collectionTagModelList;
    }

    public void setCollectionTagModelList(List<TagModel> collectionTagModelList) {
        this.collectionTagModelList = collectionTagModelList;
    }

}
