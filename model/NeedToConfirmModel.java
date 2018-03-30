
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NeedToConfirmModel implements Serializable {

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
    @SerializedName("transaction_id")

    @Expose
    private String transactionId;

    @SerializedName("product_id")
    @Expose
    private String productId;

    @SerializedName("size_id")
    @Expose
    private String sizeId;

    @SerializedName("number_of_product")
    @Expose
    private String numberOfProduct;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("used_wallet_balance")
    @Expose
    private String usedWalletBalance;

    @SerializedName("shipping_rate")
    @Expose
    private String shippingRate;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;


    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;


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
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("s_value")
    @Expose
    private String sValue;
    @SerializedName("box_condition")
    @Expose
    private String boxCondition;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("issues")
    @Expose
    private String issues;
    @SerializedName("other_issues")
    @Expose
    private String otherIssues;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("user_seller_name")
    @Expose
    private String usersellerName;

    @SerializedName("user_name")
    @Expose
    private String username;

    @SerializedName("shipped")
    @Expose
    private String shippedDate;

    @SerializedName("delivered")
    @Expose
    private String deliveredDate;

    @SerializedName("vendor_name")
    @Expose
    private String vendorName;

    @SerializedName("offer_id")
    @Expose
    private String offerId;


    @SerializedName("expiry_date")
    @Expose
    private String offer_expiryDate;

    @SerializedName("buyer_user_name")
    @Expose
    private String buyerUserName;


    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getUsedWalletBalance() {
        return usedWalletBalance;
    }

    public void setUsedWalletBalance(String usedWalletBalance) {
        this.usedWalletBalance = usedWalletBalance;
    }

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSValue() {
        return sValue;
    }

    public void setSValue(String sValue) {
        this.sValue = sValue;
    }

    public String getBoxCondition() {
        return boxCondition;
    }

    public void setBoxCondition(String boxCondition) {
        this.boxCondition = boxCondition;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getOtherIssues() {
        return otherIssues;
    }

    public void setOtherIssues(String otherIssues) {
        this.otherIssues = otherIssues;
    }


    public String getsValue() {
        return sValue;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getUsersellerName() {
        return usersellerName;
    }

    public void setUsersellerName(String usersellerName) {
        this.usersellerName = usersellerName;
    }

    public String getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(String shippedDate) {
        this.shippedDate = shippedDate;
    }

    public String getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(String deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOffer_expiryDate() {
        return offer_expiryDate;
    }

    public void setOffer_expiryDate(String offer_expiryDate) {
        this.offer_expiryDate = offer_expiryDate;
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public void setBuyerUserName(String buyerUserName) {
        this.buyerUserName = buyerUserName;
    }

}
