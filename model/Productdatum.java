
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Productdatum implements Serializable {

    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("user_product_detail_id")
    @Expose
    private String userProductDetailId;
    @SerializedName("admin_product_id")
    @Expose
    private String adminProductId;
    @SerializedName("product_sku")
    @Expose
    private String productSku;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("box_condition")
    @Expose
    private String boxCondition;
    @SerializedName("issues")
    @Expose
    private String issues;
    @SerializedName("other_issues")
    @Expose
    private String otherIssues;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("product_main_image")
    @Expose
    private String productMainImage;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("stock")
    @Expose
    private String stock;


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserProductDetailId() {
        return userProductDetailId;
    }

    public void setUserProductDetailId(String userProductDetailId) {
        this.userProductDetailId = userProductDetailId;
    }

    public String getAdminProductId() {
        return adminProductId;
    }

    public void setAdminProductId(String adminProductId) {
        this.adminProductId = adminProductId;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBoxCondition() {
        return boxCondition;
    }

    public void setBoxCondition(String boxCondition) {
        this.boxCondition = boxCondition;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductMainImage() {
        return productMainImage;
    }

    public void setProductMainImage(String productMainImage) {
        this.productMainImage = productMainImage;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
