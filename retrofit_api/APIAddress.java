package com.app.noan.retrofit_api;

import com.app.noan.model.DeleteAddreesResponse;
import com.app.noan.model.EditAddressResponse;
import com.app.noan.model.SingleAddress;
import com.app.noan.model.StoreAddressResponse;
import com.app.noan.model.VendorResponse;
import com.app.noan.model.ViewAddressResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by smn on 6/11/17.
 */

public interface APIAddress {

    @FormUrlEncoded
    @POST("user/add_user_address")
    Call<StoreAddressResponse> storeAddress(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("user/list_address_by_user")
    Call<ViewAddressResponse> viewAddress(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/set_default_address")
    Call<ViewAddressResponse> setDefaultAddress(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("user/delete_user_address")
    Call<DeleteAddreesResponse> deletAddress(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("user/edit_user_address")
    Call<EditAddressResponse> editAddress(@FieldMap Map<String, String> credentialMap);


    // Second time

    @FormUrlEncoded
    @POST("seller/change_paypal_email")
    Call<VendorResponse> changeVendorPayPalEmail(@FieldMap Map<String, String> credentialMap);


    // Remove
    @FormUrlEncoded
    @POST("user/view_single_user_address")
    Call<SingleAddress> viewSingleAddress(@FieldMap Map<String, String> credentialMap);

}
