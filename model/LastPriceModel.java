package com.app.noan.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 20/11/17.
 */

public class LastPriceModel {
    @SerializedName("min_price")
    public String minPrice;
    @SerializedName("max_price")
    public String maxPrice;

    @SerializedName("user_max_offer_price")
    public String userMaxPrice;

    @SerializedName("order_data")
    private List<OrderDatum> orderDatumArrayList = new ArrayList<>();


    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getUserMaxPrice() {
        return userMaxPrice;
    }

    public void setUserMaxPrice(String userMaxPrice) {
        this.userMaxPrice = userMaxPrice;
    }

    public List<OrderDatum> getOrderDatumArrayList() {
        return orderDatumArrayList;
    }

    public void setOrderDatumArrayList(List<OrderDatum> orderDatumArrayList) {
        this.orderDatumArrayList = orderDatumArrayList;
    }


}
