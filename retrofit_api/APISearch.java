package com.app.noan.retrofit_api;

import com.app.noan.model.CategoryResponse;
import com.app.noan.model.PriceModel;
import com.app.noan.model.ProductResponse;
import com.app.noan.model.SuggestedModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by smn on 17/11/17.
 */

public interface APISearch {


    @POST("product/get_all_category")
    Call<CategoryResponse> getAllcategory();

    @FormUrlEncoded
    @POST("product/search_product_by_sku")
    Call<ProductResponse> searchProductBySKU(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("userproduct/get_min_max_price_by_sku")
    Call<PriceModel> setMaxprice(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/get_all_product_by_category")
    Call<ProductResponse> getallProductbyCategory(@FieldMap Map<String, String> credentialMap);


    @FormUrlEncoded
    @POST("product/suggested_sku")
    Call<SuggestedModel> getSuggestedSku(@FieldMap Map<String, String> credentialMap);


}

