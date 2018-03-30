package com.app.noan.retrofit_api;

import com.app.noan.model.instagram.InstagramResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hinaka on 3/10/2016.
 */
public interface IMediaService {

  /*  @GET("media/popular")
    Call<MediaPopularResponse> getPopularMedia(@Query("client_id") String clientId);

    @GET("media/{media_id}/comments")
    Call<CommentResponse> getComment(@Path("media_id") String mediaId,
                                     @Query("client_id") String clientId);

    @GET("users/{user_id}/media/recent")
    Call<MediaUserResponse> getMediaByUser(@Path("user_id") String userId,
                                           @Query("client_id") String clientId)*/;


    @GET("explore/tags/{tag}/?__a=1")
    Call<InstagramResponse> getMediaByTag(@Path("tag") String tag);

   /* @GET("users/search")
    Call<UserResponse> getUserByUsername(@Query("q") String username,
                                         @Query("client_id") String clientId);*/
}
