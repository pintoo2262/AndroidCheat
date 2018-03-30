package com.app.noan.retrofit_api;

import com.app.noan.model.CheckEmailResponse;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.NotificationResponse;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.PaymentHistoryResponse;
import com.app.noan.model.ShareModel;
import com.app.noan.model.VendorResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;


/**
 * Created by smn on 27/9/17.
 */

public interface APILogin {
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> userLogIn(@FieldMap Map<String, String> credentialMap);

    @Multipart
    @POST("user/register")
    Call<LoginResponse> registerUser(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part("first_name") RequestBody fname
            , @Part("middle_name") RequestBody mname,
            @Part("last_name") RequestBody lname,
            @Part("mobile") RequestBody mobile,
            @Part("default_size") RequestBody defaultsize,
            @Part("mobile_type") RequestBody mobileType,
            @Part("device_token") RequestBody deviceTocken,
            @Part MultipartBody.Part image
    );


    @FormUrlEncoded
    @POST("user/check_unique_email")
    Call<CheckEmailResponse> userCheckEmail(@Field("email") String email);


    @FormUrlEncoded
    @POST("user/logout")
    Call<LoginResponse> userlogOut(@Field("user_id") String userId, @Field("mobile_type") String email);


    // seller
    @FormUrlEncoded
    @POST("user/user_seller")
    Call<LoginResponse> userProfile(@FieldMap Map<String, String> credentialMap);


    @Multipart
    @POST("user/edit_user_detail")
    Call<LoginResponse> edtitProfile(@Part("user_id") RequestBody userId,
                                     @Part("mobile") RequestBody mobile,
                                     @Part("email") RequestBody email,
                                     @Part("first_name") RequestBody firstname,
                                     @Part("middle_name") RequestBody middlename,
                                     @Part("last_name") RequestBody lastname,
                                     @Part MultipartBody.Part image);


    // vendorseller

    @FormUrlEncoded
    @POST("seller/view_seller_detail")
    Call<VendorResponse> vendorProfile(@FieldMap Map<String, String> credentialMap);


    @Multipart
    @POST("seller/edit_seller_detail")
    Call<VendorResponse> vendoreProfileEdit(@Part("user_id") RequestBody userId,
                                            @Part("name") RequestBody username,
                                            @Part("email") RequestBody name,
                                            @Part("username") RequestBody email,
                                            @Part MultipartBody.Part image);

    //

    @GET
    Call<ResponseBody> downlload(@Url String fileUrl);

    @FormUrlEncoded
    @POST("user/forgot_password")
    Call<LoginResponse> forgotpasswordOtpgenerate(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/check_otp")
    Call<LoginResponse> checkOtp(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/change_password")
    Call<LoginResponse> addNewPassword(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/change_old_password")
    Call<LoginResponse> changePassword(@FieldMap Map<String, String> credentialMap);


    // vendor
    @FormUrlEncoded
    @POST("seller/change_password")
    Call<LoginResponse> vendorChangePassword(@FieldMap Map<String, String> credentialMap);

    //
    @FormUrlEncoded
    @POST("seller/change_paypal_email")
    Call<VendorResponse> changeVendorPayPalEmail(@FieldMap Map<String, String> credentialMap);

    //
    @FormUrlEncoded
    @POST("user/change_default_shoes_size")
    Call<LoginResponse> changesDefaultSize(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("user/notification")
    Call<NotificationResponse> notificaitonSetting(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("user/userNotification")
    Call<OrderResponse> userChangesNotification(@FieldMap Map<String, String> credentialMap);


    @POST("user/sharesettings")
    Call<ShareModel> shareAPI();

    @FormUrlEncoded
    @POST("user/user_wallet_history")
    Call<PaymentHistoryResponse> userPaymentHistory(@FieldMap Map<String, String> credentialMap);


}
