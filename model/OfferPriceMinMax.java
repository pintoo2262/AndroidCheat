
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferPriceMinMax {

    @SerializedName("low_offer_price")
    @Expose
    private String lowOfferPrice;
    @SerializedName("top_offer_price")
    @Expose
    private String topOfferPrice;
    @SerializedName("seller_min_price")
    @Expose
    private String seller_min_price;

    @SerializedName("product_data")
    @Expose
    private OfferProduct productData;
    @SerializedName("order_data")
    @Expose
    private List<OrderLastSold> orderData = null;

    public String getLowOfferPrice() {
        return lowOfferPrice;
    }

    public void setLowOfferPrice(String lowOfferPrice) {
        this.lowOfferPrice = lowOfferPrice;
    }

    public String getTopOfferPrice() {
        return topOfferPrice;
    }

    public void setTopOfferPrice(String topOfferPrice) {
        this.topOfferPrice = topOfferPrice;
    }

    public OfferProduct getProductData() {
        return productData;
    }

    public void setProductData(OfferProduct productData) {
        this.productData = productData;
    }

    public List<OrderLastSold> getOrderData() {
        return orderData;
    }

    public void setOrderData(List<OrderLastSold> orderData) {
        this.orderData = orderData;
    }


    public String getSeller_min_price() {
        return seller_min_price;
    }

    public void setSeller_min_price(String seller_min_price) {
        this.seller_min_price = seller_min_price;
    }

}
