package com.app.noan.retrofit_api;

import com.app.noan.model.APIError;
import com.app.noan.model.DisplayImageResponse;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.NeedToConfirmResponse;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.ProductForSellResponse;
import com.app.noan.model.ReceiveOfferForSellResponse;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.model.SubmitResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by smn on 16/11/17.
 */

public interface APIRequestSell {

    // New
    @FormUrlEncoded
    @POST("userproduct/personal_info_request_to_sell")
    Call<ReuestSellerModelResponse> pInfoRequestToSell(@FieldMap Map<String, String> credentialMap);




/*

    @FormUrlEncoded
    @POST("userproduct/personal_info_request_to_sell")
    Call<APIError> pInfoRequestToSell(@FieldMap Map<String, String> credentialMap);
*/


    @FormUrlEncoded
    @POST("user/user_seller")
    Call<LoginResponse> checkUserSellerRequest(@FieldMap Map<String, String> credentialMap);


    /*@Multipart
    @POST("userproduct/product_detail_request_to_sell")
    Call<SubmitResponse> prdouctDetail_request_to_sell(
            @Part("user_id") RequestBody userId,
            @Part("product_id") RequestBody productId,
            @Part("product_sku") RequestBody productSku,
            @Part("size") RequestBody produtSize,
            @Part("box_condition") RequestBody productBoxcondtion,
            @Part("issues") RequestBody productIssue,
            @Part("other_issues") RequestBody productOtherissue,
            @Part("price") RequestBody productPrice,
            @Part MultipartBody.Part image1,
            @Part MultipartBody.Part image2,
            @Part MultipartBody.Part image3,
            @Part MultipartBody.Part image4,
            @Part MultipartBody.Part image5,
            @Part MultipartBody.Part image6,
            @Part MultipartBody.Part image7,
            @Part List<MultipartBody.Part> boxImageList
    );*/


    @Multipart
    @POST("userproduct/product_detail_request_to_sell")
    Call<SubmitResponse> prdouctDetail_request_to_sell(
            @Part("user_id") RequestBody userId,
            @Part("product_id") RequestBody productId,
            @Part("product_sku") RequestBody productSku,
            @Part("size") RequestBody produtSize,
            @Part("box_condition") RequestBody productBoxcondtion,
            @Part("issues") RequestBody productIssue,
            @Part("other_issues") RequestBody productOtherissue,
            @Part("price") RequestBody productPrice,
            @Part List<MultipartBody.Part> productimageList,
            @Part List<MultipartBody.Part> boxImageList
    );


    // List for Sell product list

    @FormUrlEncoded
    @POST("userproduct/display_all_product_for_sell")
    Call<ProductForSellResponse> displayProductForSell(@FieldMap Map<String, String> credentialMap);


    // Display Image

    @FormUrlEncoded
    @POST("userproduct/display_all_image_product_for_sell")
    Call<DisplayImageResponse> displayAllImageProduct(@FieldMap Map<String, String> credentialMap);


    // List for Incompelte Listing product list

    @FormUrlEncoded
    @POST("userproduct/display_incomplete_list_user_sell_product")
    Call<ProductForSellResponse> InCompleteLisingSell(@FieldMap Map<String, String> credentialMap);


    // List for Need to Confirm ,ship,shipped
    @FormUrlEncoded
    @POST("order/get_user_sell_order_by_status")
    Call<NeedToConfirmResponse> needToConfirmList(@FieldMap Map<String, String> credentialMap);


    // Receivee offerfor sell

    @FormUrlEncoded
    @POST("offer/received_offer_for_sell")
    Call<ReceiveOfferForSellResponse> receivedOfferForSell(@FieldMap Map<String, String> credentialMap);


    // Recevie confirm
    @FormUrlEncoded
    @POST("offer/confirm_offer")
    Call<OrderResponse> receivedConfirmedOrder(@FieldMap Map<String, String> credentialMap);


    // Orderstatus Chagnes
    @FormUrlEncoded
    @POST("order/change_order_status")
    Call<OrderResponse> orderStatusChanges(@FieldMap Map<String, String> credentialMap);


    // Need to to Ship,

    @Multipart
    @POST("userproduct/need_to_ship")
    Call<APIError> needToShipProduct(
            @Part("order_id") RequestBody orderId,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part image
    );


    // Main Order Screen

    @FormUrlEncoded
    @POST("user/view_order_by_user")
    Call<NeedToConfirmResponse> userOrderPurchase(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("userproduct/display_order_for_user_sell_product")
    Call<NeedToConfirmResponse> userSoldDetail(@FieldMap Map<String, String> credentialMap);


    // InOreder      projects-beta.com/noan/webservices/order/get_user_purchase_order_by_status


}
