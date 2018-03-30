package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.InstagramAdapter;
import com.app.noan.adapters.MoreProductAdapter;
import com.app.noan.fragment.LoginDialogFragment;
import com.app.noan.helper.App;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Product;
import com.app.noan.model.ProductDetailResponse;
import com.app.noan.model.instagram.Edge__;
import com.app.noan.model.instagram.InstagramResponse;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.retrofit_api.IMediaService;
import com.app.noan.retrofit_api.ServiceGenerator;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.steelkiwi.instagramhelper.InstagramHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtBuybtn, txtProductName, txt_pd_Productsize, txtProductDescrption, txtAddInstagramPhotos;
    private ImageView ivPrdouctImage;
    private ImageButton ibtnShare, ibtnMy, ibtnNeed;

    // More ImageRelated -1,2,3
    private int TypeRecycleview;
    private RecyclerView rvMoreProductImage, rvMoreProductImage2, rvInstagram;

    List<Edge__> mInstagramTagPhotoList;
    private InstagramAdapter instagramAdapter;


    //    List<Product> productsImageList;
    private List<Product> productsImageList;
    private MoreProductAdapter productDetailsAdapter;

    // More Image Product
    private List<Product> moreproductsImageList;
    private MoreProductAdapter mMoreProductAdapter;


    // tempArrayList
    private List<String> tempProductImageList;


    Dialog pDialog;
    int product_Id, finalStock;
    ApiProduct service;
    String userId, productHasTag;
    Product mProduct;
    GridLayoutManager gridLayoutManager;


    InstagramHelper instagramHelper;
    IMediaService iMediaService;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_product_details);

        toolBarAndDrawerInitilization();

        service = ApiClient.getClient().create(ApiProduct.class);
        iMediaService = ServiceGenerator.createService(IMediaService.class);
        instagramHelper = App.getInstagramHelper();

        userId = MyUtility.getSavedPreferences(ProductDetailsActivity.this, "id");
        //Intent
        product_Id = Integer.parseInt(getIntent().getStringExtra("product_id"));


        // initilzation of xml componet
        initialization();


        rvMoreProductImage.addOnItemTouchListener(new RecyclerTouchListener(this, rvMoreProductImage, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ProductDetailsActivity.this, LargeView.class);
                intent.putExtra("tempImageList", (Serializable) tempProductImageList);
                int pos1 = position + 1;
                intent.putExtra("index", pos1);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        rvInstagram.addOnItemTouchListener(new RecyclerTouchListener(this, rvInstagram, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ProductDetailsActivity.this, LargeView.class);
                intent.putExtra("tempImageList", (Serializable) tempProductImageList);
                if (productsImageList != null) {
                    int size = productsImageList.size() + 1;
                    int pos1 = position + size;
                    intent.putExtra("index", pos1);
                    startActivity(intent);
                } else {
                    int size = 1;
                    int pos1 = position + size;
                    intent.putExtra("index", pos1);
                    startActivity(intent);
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void initialization() {
        mInstagramTagPhotoList = new ArrayList<>();
        ivPrdouctImage = (ImageView) findViewById(R.id.iv_pd_ProductImage);
        txtBuybtn = (TextView) findViewById(R.id.btnnew);
        txtProductName = (TextView) findViewById(R.id.txt_pd_ProductTitle);
        txt_pd_Productsize = (TextView) findViewById(R.id.txt_pd_ProductSize);
        txtProductDescrption = (TextView) findViewById(R.id.txt_pd_ProductDescrption);
        txtAddInstagramPhotos = (TextView) findViewById(R.id.iv_ad_yours);

        rvMoreProductImage = (RecyclerView) findViewById(R.id.rv_moreProductImagelist);
        rvInstagram = (RecyclerView) findViewById(R.id.rv_moreProductImagelist1);
        rvMoreProductImage2 = (RecyclerView) findViewById(R.id.rv_moreProductImagelist2);


        txtBuybtn.setOnClickListener(this);
        ivPrdouctImage.setOnClickListener(this);
        ivPrdouctImage.setOnClickListener(this);
        txtAddInstagramPhotos.setOnClickListener(this);
        gridLayoutManager = new GridLayoutManager(ProductDetailsActivity.this, 2);
        rvMoreProductImage.setLayoutManager(gridLayoutManager);
        rvMoreProductImage.addItemDecoration(new SpacesItemDecoration(12, 2));


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar_myneed);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText("");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.transpareBlack));

        ibtnShare = mToolbar.findViewById(R.id.ibtn_share);
        ibtnMy = mToolbar.findViewById(R.id.ibtn_my);
        ibtnNeed = mToolbar.findViewById(R.id.ibtn_need);
        ibtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtility.getSavedPreferences(ProductDetailsActivity.this, "id").equals("")) {
                    if (mProduct != null) {
                        Intent intent;
                        int applicationNameId = ProductDetailsActivity.this.getApplicationInfo().labelRes;
                        final String appPackageName = ProductDetailsActivity.this.getPackageName();
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        String text = "" + mProduct.getProduct_name();
                        String link = "http://projects-beta.com/noan_new/webservices/user/appshare?product_id=" + product_Id;
                        Uri appLinkData = Uri.parse(link);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out on Noan");
                        intent.putExtra(Intent.EXTRA_TEXT, text + " " + appLinkData);
                        startActivity(Intent.createChooser(intent, "Share link:"));
                    }

                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.setArguments(ProductDetailsActivity.this);
                    dialogFragment.show(fm, "Dialog Fragment");
                }
            }
        });
        ibtnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtility.getSavedPreferences(ProductDetailsActivity.this, "id").equals("")) {
                    if (mProduct != null) {
                        myProductByServer();
                    }


                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.setArguments(ProductDetailsActivity.this);
                    dialogFragment.show(fm, "Dialog Fragment");
                }


            }
        });
        ibtnNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyUtility.getSavedPreferences(ProductDetailsActivity.this, "id").equals("")) {
                    if (mProduct != null) {
                        Intent mIntent = new Intent(ProductDetailsActivity.this, NeedActivity.class);
                        mIntent.putExtra("product", mProduct);
                        startActivity(mIntent);
                    }


                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.setArguments(ProductDetailsActivity.this);
                    dialogFragment.show(fm, "Dialog Fragment");
                }

            }
        });

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
        return (super.onOptionsItemSelected(item));
    }


    private void setProductImage() {
        rvMoreProductImage.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(ProductDetailsActivity.this, 2);
        TypeRecycleview = 0;
        productDetailsAdapter = new MoreProductAdapter(ProductDetailsActivity.this, productsImageList, TypeRecycleview);
        rvMoreProductImage.setLayoutManager(layoutManager);
        rvMoreProductImage.setAdapter(productDetailsAdapter);


    }

    private void setMoreProductImage() {
        rvMoreProductImage2.setHasFixedSize(true);
        TypeRecycleview = 2;
        mMoreProductAdapter = new MoreProductAdapter(ProductDetailsActivity.this, moreproductsImageList, TypeRecycleview);
        rvMoreProductImage2.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvMoreProductImage2.setAdapter(mMoreProductAdapter);

        rvMoreProductImage2.addOnItemTouchListener(new RecyclerTouchListener(this, rvMoreProductImage2, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("product_id", mMoreProductAdapter.moreProductList.get(position).getProduct_id());
                startActivity(intent);


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    private void setInstagramPhotos() {
        rvInstagram.setHasFixedSize(true);
        instagramAdapter = new InstagramAdapter(ProductDetailsActivity.this, mInstagramTagPhotoList);
        rvInstagram.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvInstagram.setAdapter(instagramAdapter);

    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_ad_yours:
                if (!MyUtility.getSavedPreferences(this, "id").equals("")) {
                    instagramHelper.loginFromActivity1(ProductDetailsActivity.this);
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.setArguments(ProductDetailsActivity.this);
                    dialogFragment.show(fm, "Dialog Fragment");
                }
                break;
            case R.id.btnnew:
                if (!MyUtility.getSavedPreferences(this, "id").equals("")) {
                    intent = new Intent(ProductDetailsActivity.this, SizeSelectionActiviy.class);
                    intent.putExtra("product_id", String.valueOf(product_Id));
                    intent.putExtra("ProductObjct", mProduct);
                    if (finalStock > 0) {
                        intent.putExtra("istypeOrder", "regularOrder");
                    } else {
                        intent.putExtra("istypeOrder", "offer");
                    }
                    startActivity(intent);
                } else {
                    FragmentManager fm = getSupportFragmentManager();
                    LoginDialogFragment dialogFragment = new LoginDialogFragment();
                    dialogFragment.setArguments(ProductDetailsActivity.this);
                    dialogFragment.show(fm, "Dialog Fragment");
                }


                break;
            case R.id.iv_pd_ProductImage:
                Intent mintent = new Intent(ProductDetailsActivity.this, LargeView.class);
                mintent.putExtra("tempImageList", (Serializable) tempProductImageList);
                startActivity(mintent);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ProductDetailsByServer(product_Id);
    }


    private void ProductDetailsByServer(int product_Id) {
        pDialog = new Dialog(ProductDetailsActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("product_id", String.valueOf(product_Id));
        Call<ProductDetailResponse> detailResponseCall = service.viewProductDetails(credMap);
        detailResponseCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {

                if (response.isSuccessful()) {

                    ProductDetailResponse productDetailResponse = ((ProductDetailResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (productDetailResponse.getStatus() == 1) {
                        setProductDetails(productDetailResponse);
                        mProduct = productDetailResponse.getProduct();
                        productHasTag = productDetailResponse.getProduct().getProduct_hasTag();
                        fetchPhotosByTag();
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "" + productDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(ProductDetailsActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }

    private void setProductDetails(ProductDetailResponse productDetailResponse) {

        tempProductImageList = new ArrayList<>();
        tempProductImageList.clear();
        tempProductImageList.add(productDetailResponse.getProduct().getProduct_image());
        Glide.with(this).load(productDetailResponse.getProduct().getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivPrdouctImage);
        if (Integer.parseInt(productDetailResponse.getProduct().getProduct_finalStock()) > 0) {
            finalStock = Integer.parseInt(productDetailResponse.getProduct().getProduct_finalStock());
        } else {
            txtBuybtn.setText(getString(R.string.makeOffer));
        }
        txtProductName.setText(productDetailResponse.getProduct().getProduct_name());
        txt_pd_Productsize.setText(productDetailResponse.getProduct().getProduct_type() + "**" + productDetailResponse.getProduct().getProduct_skuno());
        txtProductDescrption.setText(productDetailResponse.getProduct().getProduct_description());

        if (productDetailResponse.getProductMoreImageList().size() > 0) {
            productsImageList = new ArrayList<>();
            productsImageList.clear();
            productsImageList = productDetailResponse.getProductMoreImageList();
            for (int i = 0; i < productsImageList.size(); i++) {
                tempProductImageList.add(productsImageList.get(i).getProduct_image());
            }
            setProductImage();
        }
        if (productDetailResponse.getMoreProductImageList().size() > 0) {
            moreproductsImageList = new ArrayList<>();
            moreproductsImageList.clear();
            moreproductsImageList = productDetailResponse.getMoreProductImageList();
            setMoreProductImage();
        }


    }

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }


    private void myProductByServer() {
        pDialog = new Dialog(ProductDetailsActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("admin_product_id", String.valueOf(product_Id));


        Call<ProductDetailResponse> detailResponseCall = service.myProduct(credMap);
        detailResponseCall.enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    ProductDetailResponse productDetailResponse = ((ProductDetailResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (productDetailResponse.getStatus() == 1) {
                        Toast.makeText(ProductDetailsActivity.this, "" + productDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, "" + productDetailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(ProductDetailsActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }


    private void fetchPhotosByTag() {
        Call<InstagramResponse> call = iMediaService.getMediaByTag(productHasTag);
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    InstagramResponse body = response.body();
                    mInstagramTagPhotoList.clear();
                    mInstagramTagPhotoList.addAll(body.getGraphql().getHashtag().getEdgeHashtagToTopPosts().getEdges());
                    for (int p = 0; p < mInstagramTagPhotoList.size(); p++) {
                        tempProductImageList.add(mInstagramTagPhotoList.get(p).getNode().getDisplayUrl());
                    }
                    setInstagramPhotos();
                } else {
                    hideDialog();
//                    Toast.makeText(ProductDetailsActivity.this, "Couldn't refresh feed",
//                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {

                Toast.makeText(ProductDetailsActivity.this, "Couldn't refresh feed",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }


}
