package com.app.noan.retrofit_api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hinaka on 3/10/2016.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://www.instagram.com/";

    private static OkHttpClient.Builder sHttpClient = new OkHttpClient.Builder();

    private static HttpLoggingInterceptor sLogging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = sBuilder
                .client(sHttpClient
                        .addInterceptor(sLogging)
                        .build())
                .build();
        return retrofit.create(serviceClass);
    }
}
