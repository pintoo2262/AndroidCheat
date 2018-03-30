package com.app.noan.retrofit_api;

import com.app.noan.model.CollectionDetailsResponse;
import com.app.noan.model.CollectionResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by smn on 30/1/18.
 */

public interface APICollection {


    @POST("collection/view_all_collection")
    Call<CollectionResponse> collectionListAPI();


    //
    @FormUrlEncoded
    @POST("collection/collection_detail")
    Call<CollectionDetailsResponse> collectionDetails(@FieldMap Map<String, String> credentialMap);


}
