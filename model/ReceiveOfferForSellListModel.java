
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReceiveOfferForSellListModel implements Serializable {
    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("offer_user_code")
    @Expose
    private String offerUserCode;
    @SerializedName("offer_status")
    @Expose
    private String offerStatus;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("size_id")
    @Expose
    private String sizeId;
    @SerializedName("shipping_rate")
    @Expose
    private String shippingRate;
    @SerializedName("grand_total")
    @Expose
    private String grandTotal;
    @SerializedName("used_wallet_balance")
    @Expose
    private String usedWalletBalance;
    @SerializedName("buyer_user_code")
    @Expose
    private String buyerUserCode;
    @SerializedName("offer_date")
    @Expose
    private String offerDate;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("exp_in")
    @Expose
    private String expIn;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;


    @SerializedName("expiry_date")
    @Expose
    private String offerExpiryDate;


    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("original_price")
    @Expose
    private String originalPrice;

    @SerializedName("buyer_user_name")
    @Expose
    private String buyerUserName;

    @SerializedName("current_stock")
    @Expose
    private String currentStock;


    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferUserCode() {
        return offerUserCode;
    }

    public void setOfferUserCode(String offerUserCode) {
        this.offerUserCode = offerUserCode;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
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

    public String getUsedWalletBalance() {
        return usedWalletBalance;
    }

    public void setUsedWalletBalance(String usedWalletBalance) {
        this.usedWalletBalance = usedWalletBalance;
    }

    public String getBuyerUserCode() {
        return buyerUserCode;
    }

    public void setBuyerUserCode(String buyerUserCode) {
        this.buyerUserCode = buyerUserCode;
    }

    public String getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(String offerDate) {
        this.offerDate = offerDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getExpIn() {
        return expIn;
    }

    public void setExpIn(String expIn) {
        this.expIn = expIn;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public void setBuyerUserName(String buyerUserName) {
        this.buyerUserName = buyerUserName;
    }

    public String getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(String currentStock) {
        this.currentStock = currentStock;
    }

    public String getOfferExpiryDate() {
        return offerExpiryDate;
    }

    public void setOfferExpiryDate(String offerExpiryDate) {
        this.offerExpiryDate = offerExpiryDate;
    }
}
