package com.app.noan.model;

/**
 * Created by smn on 13/9/17.
 */

public class SandalModel {

    int sandalimage;
    String sandaldescription;
    String sandalprice;
    String sandalPerOff;

    public String getPerOff() {
        return sandalPerOff;
    }

    public void setPerOff(String perOff) {
        sandalPerOff = perOff;
    }

    public int getSandleimage() {
        return sandalimage;
    }

    public void setSandleimage(int sandalimage) {
        this.sandalimage = sandalimage;
    }

    public String getDescription() {
        return sandaldescription;
    }

    public void setDescription(String sandaldescription) {
        this.sandaldescription = sandaldescription;
    }

    public String getPrice() {
        return sandalprice;
    }

    public void setPrice(String sandalprice) {
        this.sandalprice = sandalprice;
    }

}
