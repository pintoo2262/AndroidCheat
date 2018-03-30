package com.app.noan.retrofit_api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by smn on 27/9/17.
 */

public class ApiClient {

//    public static final String BASE_URL = "http://projects-beta.com/noan/webservices/";

    public static final String BASE_URL = "http://projects-beta.com/noan_new1/webservices/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .build();


            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

         /* OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(120, TimeUnit.SECONDS)// 500 milsecond
                    .build();

                    final OkHttpClient okHttpClient = new OkHttpClient();
    okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
    okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
     .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)

                    */
        return retrofit;
    }
}
