package com.example.jay.myappbasic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by smn on 2/11/17.
 */

public class Product implements Serializable {


    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("product_id")
    @Expose
    String product_id;

    @SerializedName("hashtag")
    @Expose
    String product_hasTag;
    @SerializedName("mp_id")
    @Expose
    String mpId;
    @SerializedName("np_id")
    @Expose
    String npId;
    @SerializedName("product_name")
    @Expose
    String product_name;
    @SerializedName("description")
    String product_description;
    @SerializedName("image")
    String product_image;
    @SerializedName("sku")
    String product_skuno;
    @SerializedName("category_id")
    String product_category_id;
    @SerializedName("brand_id")
    String product_brand_id;
    @SerializedName("variant_value")
    String product_color;
    @SerializedName("price")
    String product_price;
    @SerializedName("size")
    String product_size;
    @SerializedName("s_value")
    String product_sizevalue;
    @SerializedName("type")
    String product_type;
    @SerializedName("stock")
    String product_stock;
    @SerializedName("seller_id")
    String product_seller_id;
    @SerializedName("buynow")
    String product_buynowTab;
    @SerializedName("tranding")
    String product_trandingTab;
    @SerializedName("exclusive")
    String product_exclusiveTab;
    @SerializedName("final_stock")
    String product_finalStock;
    @SerializedName("final_price")
    String product_finalprice;





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_skuno() {
        return product_skuno;
    }

    public void setProduct_skuno(String product_skuno) {
        this.product_skuno = product_skuno;
    }

    public String getProduct_category_id() {
        return product_category_id;
    }

    public void setProduct_category_id(String product_category_id) {
        this.product_category_id = product_category_id;
    }

    public String getProduct_brand_id() {
        return product_brand_id;
    }

    public void setProduct_brand_id(String product_brand_id) {
        this.product_brand_id = product_brand_id;
    }

    public String getProduct_color() {
        return product_color;
    }

    public void setProduct_color(String product_color) {
        this.product_color = product_color;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
    }

    public String getProduct_seller_id() {
        return product_seller_id;
    }

    public void setProduct_seller_id(String product_seller_id) {
        this.product_seller_id = product_seller_id;
    }

    public String getProduct_buynowTab() {
        return product_buynowTab;
    }

    public void setProduct_buynowTab(String product_buynowTab) {
        this.product_buynowTab = product_buynowTab;
    }

    public String getProduct_trandingTab() {
        return product_trandingTab;
    }

    public void setProduct_trandingTab(String product_trandingTab) {
        this.product_trandingTab = product_trandingTab;
    }

    public String getProduct_exclusiveTab() {
        return product_exclusiveTab;
    }

    public void setProduct_exclusiveTab(String product_exclusiveTab) {
        this.product_exclusiveTab = product_exclusiveTab;
    }

    public String getProduct_lowprice() {
        return product_lowprice;
    }

    public void setProduct_lowprice(String product_lowprice) {
        this.product_lowprice = product_lowprice;
    }

    public String getProduct_lastSaleprice() {
        return product_lastSaleprice;
    }

    public void setProduct_lastSaleprice(String product_lastSaleprice) {
        this.product_lastSaleprice = product_lastSaleprice;
    }

    public String getProduct_finalStock() {
        return product_finalStock;
    }

    public void setProduct_finalStock(String product_finalStock) {
        this.product_finalStock = product_finalStock;
    }

    public String getProduct_finalprice() {
        return product_finalprice;
    }

    public void setProduct_finalprice(String product_finalprice) {
        this.product_finalprice = product_finalprice;
    }

    public String getMpId() {
        return mpId;
    }

    public void setMpId(String mpId) {
        this.mpId = mpId;
    }

    public String getNpId() {
        return npId;
    }

    public void setNpId(String npId) {
        this.npId = npId;
    }


    public String getProduct_sizevalue() {
        return product_sizevalue;
    }

    public void setProduct_sizevalue(String product_sizevalue) {
        this.product_sizevalue = product_sizevalue;
    }

    public String getProduct_topOfferPrice() {
        return product_topOfferPrice;
    }

    public void setProduct_topOfferPrice(String product_topOfferPrice) {
        this.product_topOfferPrice = product_topOfferPrice;
    }

    public String getProduct_hasTag() {
        return product_hasTag;
    }

    public void setProduct_hasTag(String product_hasTag) {
        this.product_hasTag = product_hasTag;
    }


}
