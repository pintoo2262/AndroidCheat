package com.app.noan.model;

/**
 * Created by smn on 9/2/18.
 */

public class FingurePrintAuthentication {
    String email;
    String password;
    Boolean isEnableFignure;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnableFignure() {
        return isEnableFignure;
    }

    public void setEnableFignure(Boolean enableFignure) {
        isEnableFignure = enableFignure;
    }


}
