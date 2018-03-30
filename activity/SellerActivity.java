package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.SellerImageViewpagerAdapter;
import com.app.noan.model.CommonImage;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.app.noan.R.string.sellerSwipeValidaiton;

public class SellerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private TextView txtOK, txtIAgree, txtAcceptPolicy;
    private LinearLayout llseller1, llseller2, llseller3, llIndaicator;

    //  Viewpager
    private EnchantedViewPager mViewpagerSell;
    private SellerImageViewpagerAdapter viewpagerAdapter;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private int imagelist[] = {R.drawable.b1, R.drawable.b2, R.drawable.b3, R.drawable.b4, R.drawable.b5};
    private List<CommonImage> imagesLIst;
    private int dotsCount;
    private ImageView[] dots;

    private Handler mHandler;
    private Timer mTimerTask;
    private Runnable mRunnable;

    private String shfillvalue = "false", shPreferenceGetsell, userId;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_seller);
        MyUtility.onResumeSellerScreenType=2;
        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();
        userId = MyUtility.getSavedPreferences(SellerActivity.this, "id");
        // set Data inViewpagaer
        setViewpagerData();


    }


    private void initialization() {
        txtOK = (TextView) findViewById(R.id.btnSellerOk);
        txtIAgree = (TextView) findViewById(R.id.txt_i_agree);
        txtAcceptPolicy = (TextView) findViewById(R.id.txtAcceptPolicy);
        llseller1 = (LinearLayout) findViewById(R.id.llsellr1);
        llseller2 = (LinearLayout) findViewById(R.id.llsellr2);
        llseller3 = (LinearLayout) findViewById(R.id.llsellr3);
        mViewpagerSell = (EnchantedViewPager) findViewById(R.id.viewpagejustinfo);
        llIndaicator = (LinearLayout) findViewById(R.id.ll_Indicator_Dots);
        txtIAgree.setVisibility(View.VISIBLE);

        shPreferenceGetsell = getIntent().getStringExtra("IntetSeller");


        if (shPreferenceGetsell != null) {
            shfillvalue = shPreferenceGetsell;
        }

        txtOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llseller1.setVisibility(View.GONE);
                llseller2.setVisibility(View.VISIBLE);

            }

        });
        txtIAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shfillvalue.equals("true")) {
                    MyUtility.savePreferences(SellerActivity.this, "shifillValue", "1");
                    MyUtility.savePreferences(SellerActivity.this, "isFillSell", "true");
                    llseller2.setVisibility(View.GONE);
                    llseller3.setVisibility(View.VISIBLE);
                } else {
                    dialog(getResources().getString(sellerSwipeValidaiton));
                }

            }
        });
        txtAcceptPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalReturnaddressInfoByserver(shfillvalue);


            }
        });

    }

    @SuppressWarnings("deprecation")
    private void setViewpagerData() {
        imagesLIst = new ArrayList<>();
        for (int i = 0; i < imagelist.length; i++) {
            CommonImage commonImage = new CommonImage();
            commonImage.setImage(imagelist[i]);
            imagesLIst.add(commonImage);
        }
        viewpagerAdapter = new SellerImageViewpagerAdapter(SellerActivity.this, imagesLIst);
        mViewpagerSell.setAdapter(viewpagerAdapter);
        mViewpagerSell.setCurrentItem(0);
        mViewpagerSell.setOnPageChangeListener(this);
        setUIPageController();
        mViewpagerSell.useScale();
        mViewpagerSell.useAlpha();

        NUM_PAGES = imagelist.length;
    }

    public void dialog(String msg) {
        final TextView dialogMsg;
        final Button btnOk;
        final Dialog validationDialog = new Dialog(SellerActivity.this);
        validationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        validationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        validationDialog.setContentView(R.layout.custom_dialog_validation);
        validationDialog.setCanceledOnTouchOutside(true);
        validationDialog.setCancelable(false);
        dialogMsg = validationDialog.findViewById(R.id.txtMessage);
        btnOk = validationDialog.findViewById(R.id.btn);
        dialogMsg.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationDialog.dismiss();
            }
        });
        validationDialog.show();
    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.seller101));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.requestSell));
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.btn_bgwhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }

    @SuppressWarnings("deprecation")
    private void setUIPageController() {
        dotsCount = viewpagerAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            llIndaicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_dots));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dots));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_dots));

        if (imagelist.length - 1 == position) {
            shfillvalue = "true";

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void PersonalReturnaddressInfoByserver(String shfillvalue) {

        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("termsagree", "");
        credMap.put("full_name", "");
        credMap.put("phone_number", "");
        credMap.put("birth_date", "");
        credMap.put("address1", "");
        credMap.put("address2", "");
        credMap.put("seller_info", shfillvalue);
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "seller_101");
        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    if (sellerModelResponse.getStatus() == 1) {
                        Intent intentMessage = new Intent();
                        // put the message in Intent
                        // Set The Result in Intent
                        setResult(3, intentMessage);
                        // finish The activity
                        finish();
                    } else {
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(SellerActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
