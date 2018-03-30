package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.DisplayAllBoxImageAdpater;
import com.app.noan.adapters.DisplayAllProductImageAdpater;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Boximagedatum;
import com.app.noan.model.DisplayImageResponse;
import com.app.noan.model.Imagedatum;
import com.app.noan.model.Productdatum;
import com.app.noan.model.SelllerProduct;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserSellerDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = UserSellerDetailActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtProductTitle, txtProductCondtion, txtProductPrice, txtProductSize, txtOk;
    private ImageView ivProductImage;

    private RecyclerView rvProductImage, rvBoxImage;
    private DisplayAllProductImageAdpater productImageAdapter;
    private DisplayAllBoxImageAdpater boxImageAdpater;

    private Productdatum mProduct;
    private List<Imagedatum> productImageList = new ArrayList<>();
    private List<Boximagedatum> boxImageList = new ArrayList<>();
    RelativeLayout mRelativeLayout;
    Dialog pDialog;

    APIRequestSell service;
    SelllerProduct mSelllerProduct;
    SharedPreferences prefs;
    public List<SizeModel> sizeModelList;
    String sizevalue;
    List<String> tempProductImageList;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_seller_detail);
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        mSelllerProduct = (SelllerProduct) getIntent().getSerializableExtra("SelllerProductList");
        toolBarAndDrawerInitilization();

        initialize();

        displyAllImage_byServer();
        rvProductImage.addOnItemTouchListener(new RecyclerTouchListener(this, rvProductImage, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(UserSellerDetailActivity.this, LargeView.class);
                intent.putExtra("tempImageList", (Serializable) tempProductImageList);
                int pos1 = position + 1;
                intent.putExtra("index", pos1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvBoxImage.addOnItemTouchListener(new RecyclerTouchListener(this, rvBoxImage, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(UserSellerDetailActivity.this, LargeView.class);
                intent.putExtra("tempImageList", (Serializable) tempProductImageList);
                int pos1 = position + 8;
                intent.putExtra("index", pos1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.toolbar_userSellerProduct));
    }

    private void initialize() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        rvProductImage = (RecyclerView) findViewById(R.id.rv_product_image);
        rvBoxImage = (RecyclerView) findViewById(R.id.rvBoxImae);
        txtProductTitle = (TextView) findViewById(R.id.txtSellerProductName);
        txtProductSize = (TextView) findViewById(R.id.txtSellerProductSize);
        txtProductCondtion = (TextView) findViewById(R.id.txtSellerCondition);
        txtProductPrice = (TextView) findViewById(R.id.txtSellerPrice);
        ivProductImage = (ImageView) findViewById(R.id.iv_SellerProductImage);
        txtOk = (TextView) findViewById(R.id.txtOk);
        txtOk.setOnClickListener(this);
        ivProductImage.setOnClickListener(this);

        txtProductTitle.setText(mSelllerProduct.getProductName());
        txtProductPrice.setText("$ " + mSelllerProduct.getPrice());
        txtProductCondtion.setText("Box :" + mSelllerProduct.getBoxCondition());
        try {
            sizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sizeModelList.size() > 0) {
            for (int i = 0; i < sizeModelList.size(); i++) {
                if (mSelllerProduct.getSize().equals(sizeModelList.get(i).getSizeId())) {
                    sizevalue = sizeModelList.get(i).getSizeValue();
                }
            }
        }


        txtProductSize.setText("Size  " + sizevalue + "  New in Box");
        Glide.with(this).load(mSelllerProduct.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProductImage);
    }

    private void set_ProductImageList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvProductImage.setLayoutManager(layoutManager);
        productImageAdapter = new DisplayAllProductImageAdpater(this, productImageList);
        rvProductImage.setAdapter(productImageAdapter);

    }

    private void set_BoxImageList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvBoxImage.setLayoutManager(layoutManager);
        boxImageAdpater = new DisplayAllBoxImageAdpater(this, boxImageList);
        rvBoxImage.setAdapter(boxImageAdpater);

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    private void displyAllImage_byServer() {

        pDialog = new Dialog(UserSellerDetailActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);

        showDialog();
        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_product_detail_id", mSelllerProduct.getId());

        Call<DisplayImageResponse> call = service.displayAllImageProduct(credMap);
        call.enqueue(new Callback<DisplayImageResponse>() {
            @Override
            public void onResponse(Call<DisplayImageResponse> call, Response<DisplayImageResponse> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    DisplayImageResponse displayImageResponse = ((DisplayImageResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    tempProductImageList = new ArrayList<>();
                    tempProductImageList.clear();
                    tempProductImageList.add(mSelllerProduct.getImage());
                    if (displayImageResponse.getStatus() == 1) {

                        boxImageList = displayImageResponse.getBoximagedata();
                        productImageList = displayImageResponse.getImagedata();
                        set_BoxImageList();
                        set_ProductImageList();

                        for (int i = 0; i < productImageList.size(); i++) {
                            tempProductImageList.add(productImageList.get(i).getImage());
                        }
                        for (int i = 0; i < boxImageList.size(); i++) {
                            tempProductImageList.add(boxImageList.get(i).getImage());
                        }
                    } else {
//                        Toast.makeText(DisplaySellImageActivity.this, "" + displayImageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<DisplayImageResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();
                Toast.makeText(UserSellerDetailActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
                Log.d("onFailure", t.toString());
            }
        });
    }

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtOk:
                Intent intent = new Intent(this, ProductBuyActivity.class);
                intent.putExtra("screenType", "buynow");
                intent.putExtra("SelllerProductList", (Serializable) mSelllerProduct);
                startActivity(intent);
                break;
            case R.id.iv_SellerProductImage:
                Intent mintent = new Intent(UserSellerDetailActivity.this, LargeView.class);
                mintent.putExtra("tempImageList", (Serializable) tempProductImageList);
                startActivity(mintent);
                break;
        }
    }
}
