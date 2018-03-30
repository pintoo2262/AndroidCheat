package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by smn on 8/11/17.
 */

public class SelllerProduct implements Comparable<SelllerProduct>, Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("user_seller_id")
    @Expose
    private String usersellerId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("main_product")
    @Expose
    private String mainProduct;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("stock")
    @Expose
    private String stock;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("variant_value")
    @Expose
    private String variantValue;
    @SerializedName("buynow")
    @Expose
    private String buynow;
    @SerializedName("tranding")
    @Expose
    private String tranding;
    @SerializedName("exclusive")
    @Expose
    private String exclusive;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;
    @SerializedName("s_value")
    @Expose
    private String sValue;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("contact_name")
    @Expose
    private String contactName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("commision")
    @Expose
    private String commision;
    @SerializedName("bargaining")
    @Expose
    private String bargaining;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("box_condition")
    @Expose
    private String boxCondition;
    @SerializedName("issues")
    @Expose
    private String issues;

    @SerializedName("other_issues")
    @Expose
    private String otherIssues;

    @SerializedName("exp_in")
    @Expose
    private String expIn;


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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariantValue() {
        return variantValue;
    }

    public void setVariantValue(String variantValue) {
        this.variantValue = variantValue;
    }

    public String getBuynow() {
        return buynow;
    }

    public void setBuynow(String buynow) {
        this.buynow = buynow;
    }

    public String getTranding() {
        return tranding;
    }

    public void setTranding(String tranding) {
        this.tranding = tranding;
    }

    public String getExclusive() {
        return exclusive;
    }

    public void setExclusive(String exclusive) {
        this.exclusive = exclusive;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSValue() {
        return sValue;
    }

    public void setSValue(String sValue) {
        this.sValue = sValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getBargaining() {
        return bargaining;
    }

    public void setBargaining(String bargaining) {
        this.bargaining = bargaining;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUsersellerId() {
        return usersellerId;
    }

    public void setUsersellerId(String usersellerId) {
        this.usersellerId = usersellerId;
    }

    public String getsValue() {
        return sValue;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }

    public String getExpIn() {
        return expIn;
    }

    public void setExpIn(String expIn) {
        this.expIn = expIn;
    }

    @Override
    public int compareTo(SelllerProduct selllerProduct) {

        return (Integer.parseInt(this.getPrice()) < Integer.parseInt(selllerProduct.getPrice()) ? -1 :

                (this.getPrice() == selllerProduct.getPrice() ? 0 : 1));

    }


}
