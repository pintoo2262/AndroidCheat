package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by smn on 30/1/18.
 */

public class ProductCollection {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("collection_id")
    @Expose
    private String collectionId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("final_price")
    @Expose
    private String finalprice;

    @SerializedName("final_stock")
    @Expose
    private String finalstock;

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("sku")
    @Expose
    private String sku;

    @SerializedName("image")
    @Expose
    private String image;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getFinalprice() {
        return finalprice;
    }

    public void setFinalprice(String finalprice) {
        this.finalprice = finalprice;
    }

    public String getFinalstock() {
        return finalstock;
    }

    public void setFinalstock(String finalstock) {
        this.finalstock = finalstock;
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


}
