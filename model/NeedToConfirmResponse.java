package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by smn on 22/11/17.
 */

public class NeedToConfirmResponse {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    @Expose
    private List<NeedToConfirmModel> needToConfirmModelList = null;
    @SerializedName("metadata")
    @Expose
    private MetaData metadata;

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

    public List<NeedToConfirmModel> getNeedToConfirmModelList() {
        return needToConfirmModelList;
    }

    public void setNeedToConfirmModelList(List<NeedToConfirmModel> needToConfirmModelList) {
        this.needToConfirmModelList = needToConfirmModelList;
    }

    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }


}
