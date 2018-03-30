package com.app.noan.model;

/**
 * Created by smn on 19/9/17.
 */

public class OfferModel {

    int image;
    int updownlastsale;
    String name;
    String code;
    String topoffer;
    String lowprice;
    String lastsale;

    public OfferModel(int image, String name, String code, String topoffer, String lowprice, int updownlastsale, String lastsale) {
        this.image = image;
        this.name = name;
        this.code = code;
        this.topoffer = topoffer;
        this.lowprice = lowprice;
        this.updownlastsale = updownlastsale;
        this.lastsale = lastsale;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getUpdownlastsale() {
        return updownlastsale;
    }

    public void setUpdownlastsale(int updownlastsale) {
        this.updownlastsale = updownlastsale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTopoffer() {
        return topoffer;
    }

    public void setTopoffer(String topoffer) {
        this.topoffer = topoffer;
    }

    public String getLowprice() {
        return lowprice;
    }

    public void setLowprice(String lowprice) {
        this.lowprice = lowprice;
    }

    public String getLastsale() {
        return lastsale;
    }

    public void setLastsale(String lastsale) {
        this.lastsale = lastsale;
    }
}
