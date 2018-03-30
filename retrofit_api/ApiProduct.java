package com.app.noan.retrofit_api;

import com.app.noan.model.BrandImageResponse;
import com.app.noan.model.BuyNowSIzeResponse;
import com.app.noan.model.CategoryResponse;
import com.app.noan.model.ColorResponseModel;
import com.app.noan.model.DefaultAPIResponse;
import com.app.noan.model.DefaultSizeResponse;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.NeedViewResponse;
import com.app.noan.model.Offer;
import com.app.noan.model.OrderPurchase;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.ProductDetailResponse;
import com.app.noan.model.ProductResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by smn on 27/9/17.
 */

public interface ApiProduct {

    @FormUrlEncoded
    @POST("product/view_all_product")
    Call<ProductResponse> viewAllPrdouct(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/view_single_product")
    Call<ProductDetailResponse> viewProductDetails(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/view_all_size_single_product")
    Call<BuyNowSIzeResponse> viewProductSize(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("order/order")
    Call<OrderResponse> PrdouctOrder(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/my_product")
    Call<ProductDetailResponse> myProduct(@FieldMap Map<String, String> credentialMap);

    @FormUrlEncoded
    @POST("product/need_product")
    Call<LoginResponse> needProduct(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/need_product_by_size")
    Call<NeedViewResponse> viewNeedSizeId(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/my_product_list_by_user")
    Call<ProductResponse> myProductList(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/need_product_list_by_user")
    Call<ProductResponse> needProductList(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/my_product_delete_by_user")
    Call<ProductResponse> deletemyProductList(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/need_product_delete_by_user")
    Call<ProductResponse> deleteneedProductList(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("user/view_order_by_user")
    Call<OrderPurchase> OrderPurchase(@FieldMap Map<String, String> credentialMap);


    @POST("other/get_default_data")
    Call<DefaultAPIResponse> getDefaultAPIByServer();

    @POST("other/size")
    Call<DefaultSizeResponse> sizeDefaultApplication();


    @POST("other/view_all_brand")
    Call<BrandImageResponse> brandDefaultApplication();

    @POST("other/color")
    Call<ColorResponseModel> colorDefaultApplication();


    @POST("product/get_all_category")
    Call<CategoryResponse> getAllcategory();


    @FormUrlEncoded
    @POST("product/get_all_product_by_filter")
    Call<ProductResponse> filterByServer(@FieldMap Map<String, String> credentialMap);


    //Offer
    @FormUrlEncoded
    @POST("offer/product_detail_for_offer")
    Call<Offer> offerPriceDetails(@FieldMap Map<String, String> credentialMap);


}
