package com.app.noan.model;

/**
 * Created by smn on 21/9/17.
 */

public class Payment {
    String paymentDescription;
    String paymentTransactionNo;
    String paymentAmount;
    String paymentdate;

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public String getPaymentTransactionNo() {
        return paymentTransactionNo;
    }

    public void setPaymentTransactionNo(String paymentTransactionNo) {
        this.paymentTransactionNo = paymentTransactionNo;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }


}
