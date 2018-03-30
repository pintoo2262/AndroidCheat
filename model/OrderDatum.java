
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_seller_id")
    @Expose
    private String userSellerId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("number_of_product")
    @Expose
    private String numberOfProduct;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("shipping_rate")
    @Expose
    private String shippingRate;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("mark_as_shipped")
    @Expose
    private String markAsShipped;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSellerId() {
        return userSellerId;
    }

    public void setUserSellerId(String userSellerId) {
        this.userSellerId = userSellerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNumberOfProduct() {
        return numberOfProduct;
    }

    public void setNumberOfProduct(String numberOfProduct) {
        this.numberOfProduct = numberOfProduct;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getShippingRate() {
        return shippingRate;
    }

    public void setShippingRate(String shippingRate) {
        this.shippingRate = shippingRate;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarkAsShipped() {
        return markAsShipped;
    }

    public void setMarkAsShipped(String markAsShipped) {
        this.markAsShipped = markAsShipped;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
