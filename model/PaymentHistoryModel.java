
package com.app.noan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentHistoryModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    @SerializedName("offer_id")
    @Expose
    private String offer_id;


    @SerializedName("transaction_no")
    @Expose
    private String transaction_no;


    @SerializedName("user_code")
    @Expose
    private String userCode;
    @SerializedName("wallet_balance")
    @Expose
    private String walletBalance;
    @SerializedName("transfer_type")
    @Expose
    private String transferType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("created")
    @Expose
    private String created;

    @SerializedName("status")
    @Expose
    private String Status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTransaction_no() {
        return transaction_no;
    }

    public void setTransaction_no(String transaction_no) {
        this.transaction_no = transaction_no;
    }


}
