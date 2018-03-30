package com.app.noan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.app.noan.R;
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {


    private ImageView ivSplashGiF;
    private String sessionUser;
    long gifanimatiomTime = 2030;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // initilzation of xml componet
        initialization();


        if (getIntent().getData() != null) {
            //do here
            String productId = getIntent().getData().getQueryParameter("product_id");

            if (productId != null) {
                Intent intent1 = new Intent(SplashActivity.this, ProductDetailsActivity.class);
                intent1.putExtra("product_id", productId);
                startActivity(intent1);
                finish();
            } else {
                Intent intent1 = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent1);
                finish();
            }
        }


        //set GIF in Splash Screen

        Glide.with(this).

                load(R.drawable.splashscreen).

                listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer
                            model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {


                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sessionUser = MyUtility.getSavedPreferences(SplashActivity.this, "id");
                                if (!sessionUser.equals("") && !sessionUser.isEmpty() && !sessionUser.equals("null")) {
                                    if (getIntent().getExtras() != null) {
                                        Gson gson = new Gson();
                                        String pushType = null, valueString = null;
                                        JSONObject mJsonObject = null;
                                        for (String key : getIntent().getExtras().keySet()) {
                                            Object value = getIntent().getExtras().get(key);
                                            if (key.equals("push_type")) {
                                                pushType = String.valueOf(value);
                                            }
                                            if (key.equals("data")) {
                                                try {
                                                    valueString = String.valueOf(value);
                                                    mJsonObject = new JSONObject(String.valueOf(value));
                                                } catch (Throwable t) {
                                                    Log.e("My App", "Could not parse malformed JSON: \"" + value + "\"");
                                                }
                                            }
                                        }
                                        if (pushType != null) {
                                            if (pushType.equals("SANDAL SIZE AVAILABLE")) {
                                                Intent intent = new Intent(SplashActivity.this, ProductDetailsActivity.class);
                                                try {
                                                    intent.putExtra("product_id", mJsonObject.getString("product_id"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("SANDAL SHIPPED TO YOU")) {
                                                NeedToConfirmModel needToConfirmModel = gson.fromJson(valueString, NeedToConfirmModel.class);
                                                Intent intent = new Intent(SplashActivity.this, OrderDetailActivity.class);
                                                intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("SANDAL DELIVERED TO YOU")) {
                                                NeedToConfirmModel needToConfirmModel = gson.fromJson(valueString, NeedToConfirmModel.class);
                                                Intent intent = new Intent(SplashActivity.this, OrderDetailActivity.class);
                                                intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("UPDATE ON YOUR OFFERS")) {
                                                NeedToConfirmModel needToConfirmModel = gson.fromJson(valueString, NeedToConfirmModel.class);

                                                Intent intent = new Intent(SplashActivity.this, OrderDetailActivity.class);
                                                intent.putExtra("NeedConfirmModel", (Serializable) needToConfirmModel);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("YOUR SANDAL SOLD")) {
                                                NeedToConfirmModel needToConfirmModel = gson.fromJson(valueString, NeedToConfirmModel.class);
                                                Intent intent = new Intent(SplashActivity.this, ConfirmSoldeActivity.class);
                                                intent.putExtra("NeedToConfirmModel", (Serializable) needToConfirmModel);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("UPDATE ON YOUR SELLER ACCOUNT STATUS")) {
                                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                                intent.putExtra("Notification", "isNotification");
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            } else if (pushType.equals("UPDATE ON YOUR LISTED PRODUCT FOR SELL")) {
                                                String status = null;
                                                Intent intent;
                                                try {
                                                    status = mJsonObject.getString("status");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if (status.equals("approved")) {
                                                    intent = new Intent(SplashActivity.this, ListingActivity.class);
                                                } else {
                                                    intent = new Intent(SplashActivity.this, IncompleteListActivity.class);
                                                }
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                                // Adds the back stack
                                                stackBuilder.addParentStack(FAQActivity.class);
                                                // Adds the Intent to the top of the stack
                                                stackBuilder.addNextIntent(intent);
                                                startActivity(intent);
                                                finish();
                                            }


                                        } else {
                                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(SplashActivity.this);
                                            // Adds the back stack
                                            stackBuilder.addParentStack(HomeActivity.class);
                                            // Adds the Intent to the top of the stack
                                            stackBuilder.addNextIntent(intent);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                } else

                                {
                                    Intent i = new Intent(SplashActivity.this, SplashActivity2.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }, gifanimatiomTime);
                        return false;
                    }
                }).

                into(ivSplashGiF);


    }

    private void initialization() {
        ivSplashGiF = (ImageView) findViewById(R.id.iv_splashImage);
    }


}
