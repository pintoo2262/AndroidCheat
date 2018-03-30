package com.app.noan.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.noan.R;
import com.app.noan.model.BrandModel;
import com.app.noan.model.CategoryProduct;
import com.app.noan.model.ColorModel;
import com.app.noan.model.DefaultAPIResponse;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by smn on 13/9/17.
 */

public class App extends Application {
    private static final String TAG = "App";
    private static final String TWITTER_KEY = "obXgL2QPcu6bRgk1qZa0X3URS";
    private static final String TWITTER_SECRET = "4OI1ttLmSKPrAXMaorHmcjnCc6iCVNM7AgFFdTgVVZ4Y2bTFi9";


    ApiProduct apiProduct;
    public List<BrandModel> brandModelList;
    public List<SizeModel> sizeModelList;
    public List<ColorModel> colorModelList;
    public List<CategoryProduct> categoryProductList;
    SharedPreferences prefs;

    public static final String CLIENT_ID = "e802c14b2f3c4790aaa704ad4020f1ad";
    public static final String CLIENT_SECRET = "42f87c04998b437781c3340bbb6a1549";
    public static final String REDIRECT_URL = "http://localhost:3000";

    private static InstagramHelper instagramHelper;
    String scope;

    private static TwitterAuthConfig authConfig;

    @Override
    public void onCreate() {
        super.onCreate();
        authConfig =
                new TwitterAuthConfig(TWITTER_KEY,
                        TWITTER_SECRET);
        scope = "basic+public_content+follower_list+comments+relationships+likes";
        instagramHelper = new InstagramHelper.Builder()
                .withClientId(CLIENT_ID)
                .withRedirectUrl(REDIRECT_URL)
                .withScope(scope)
                .build();


        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new CrashlyticsNdk());
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Dubai-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        apiProduct = ApiClient.getClient().create(ApiProduct.class);
        prefs = getApplicationContext().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);

        defaultApplicationByServer();

    }

    public static InstagramHelper getInstagramHelper() {
        return instagramHelper;
    }

    public static TwitterAuthConfig getTiwtterHelper() {
        return authConfig;
    }


    public void defaultApplicationByServer() {
        Call<DefaultAPIResponse> defaultAPIResponseCall = apiProduct.getDefaultAPIByServer();
        defaultAPIResponseCall.enqueue(new Callback<DefaultAPIResponse>() {
            @Override
            public void onResponse(Call<DefaultAPIResponse> call, Response<DefaultAPIResponse> response) {
                DefaultAPIResponse defaultAPIResponse = response.body();
                if (defaultAPIResponse != null) {
                    if (defaultAPIResponse.getStatus() == 1) {
                        brandModelList = defaultAPIResponse.getBrandModelList();
                        categoryProductList = defaultAPIResponse.getCategoryProductList();
                        sizeModelList = defaultAPIResponse.getSizeModelList();
                        colorModelList = defaultAPIResponse.getColorModelList();
                        setDataInSharedpreferences(brandModelList, categoryProductList, sizeModelList, colorModelList);
                    }
                }

            }

            @Override
            public void onFailure(Call<DefaultAPIResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }


    private void setDataInSharedpreferences(List<BrandModel> brandModelList, List<CategoryProduct> categoryProductList, List<SizeModel> sizeModelList, List<ColorModel> colorModelList) {
        SharedPreferences.Editor edit = prefs.edit();
        if (brandModelList != null) {
            try {
                edit.putString("BrandList", ObjectSerializer.serialize((Serializable) brandModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (categoryProductList != null) {
            try {
                edit.putString("CategoryList", ObjectSerializer.serialize((Serializable) categoryProductList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (sizeModelList != null) {
            try {
                edit.putString("SizeList", ObjectSerializer.serialize((Serializable) sizeModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (colorModelList != null) {
            try {
                edit.putString("ColorList", ObjectSerializer.serialize((Serializable) colorModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        edit.commit();


    }


}
