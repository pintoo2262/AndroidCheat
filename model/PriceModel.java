package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 20/11/17.
 */

public class PriceModel {
    @SerializedName("status")
    public Integer status;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public LastPriceModel lastprice;

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

    public LastPriceModel getLastprice() {
        return lastprice;
    }

    public void setLastprice(LastPriceModel lastprice) {
        this.lastprice = lastprice;
    }


}
