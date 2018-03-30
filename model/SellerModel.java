
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SellerModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("birth_date")
    @Expose
    private String birthDate;
    @SerializedName("address1")
    @Expose
    private String address1;

    @SerializedName("address2")
    @Expose
    private String address2;

    @SerializedName("terms_agree")
    @Expose
    private String termsAgree;
    @SerializedName("seller_info")
    @Expose
    private String sellerInfo;
    @SerializedName("facebook_link")
    @Expose
    private String fbLink;
    @SerializedName("twitter_link")
    @Expose
    private String twLink;
    @SerializedName("insta_link")
    @Expose
    private String inLink;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("created")
    @Expose
    private String created;


    @SerializedName("verified_saller")
    private String verifiedSaller;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getTermsAgree() {
        return termsAgree;
    }

    public void setTermsAgree(String termsAgree) {
        this.termsAgree = termsAgree;
    }

    public String getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(String sellerInfo) {
        this.sellerInfo = sellerInfo;
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

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getVerifiedSaller() {
        return verifiedSaller;
    }

    public void setVerifiedSaller(String verifiedSaller) {
        this.verifiedSaller = verifiedSaller;
    }


    public String getFbLink() {
        return fbLink;
    }

    public void setFbLink(String fbLink) {
        this.fbLink = fbLink;
    }

    public String getTwLink() {
        return twLink;
    }

    public void setTwLink(String twLink) {
        this.twLink = twLink;
    }

    public String getInLink() {
        return inLink;
    }

    public void setInLink(String inLink) {
        this.inLink = inLink;
    }

}
