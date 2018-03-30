package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.ProductImageAdpater;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.model.Product;
import com.app.noan.model.SizeModel;
import com.app.noan.model.SubmitResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubmitListingActivity extends AppCompatActivity {

    private static final String TAG = SubmitListingActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtProductTitle, txtProductCondtion, txtProductPrice, txtProductSize;
    private ImageView ivProductImage;

    private Button btnSubmit;

    private RecyclerView rvProductImage, rvBoxImage;
    private ProductImageAdpater imageAdpater;

    private Product mProduct;
    private String strCondtion, strIssue, strOtherissue, strSize, strPrice;
    private ArrayList<String> productImageList = new ArrayList<>();
    private ArrayList<String> boxImageList = new ArrayList<>();
    RelativeLayout mRelativeLayout;
    SharedPreferences prefs;
    public List<SizeModel> sizeModelList;
    String sizevalue;
    Dialog pDialog;
    List<MultipartBody.Part> productListImage = new ArrayList<>();
    List<MultipartBody.Part> boxListImage = new ArrayList<>();


    //
    String productImage1, productImage2, productImage3, productImage4, productImage5, productImage6, productImage7, boxImage1, boxImage2;
    RequestBody userId, product_id, product_sku, size, issues, box_condition, other_issues, price;
    APIRequestSell service;

    @Override

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_listing);
        service = ApiClient.getClient().create(APIRequestSell.class);

        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        mProduct = (Product) getIntent().getSerializableExtra("searchProduct");
        strCondtion = getIntent().getStringExtra("box_condition");
        strIssue = getIntent().getStringExtra("issues");
        strOtherissue = getIntent().getStringExtra("other_issues");
        strSize = getIntent().getStringExtra("defaultSize");
        strPrice = getIntent().getStringExtra("productPrice");
        productImageList = getIntent().getStringArrayListExtra("productImage");
        boxImageList = getIntent().getStringArrayListExtra("boxlistImage");


        toolBarAndDrawerInitilization();

        initialize();

        set_ProductImageList();

        set_BoxImageList();

        pDialog = new Dialog(SubmitListingActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(5 * 1000);

                    // After 5 seconds redirect to another intent
                    for (int j = 0; j < productImageList.size(); j++) {
                        File file = new File(productImageList.get(j));
                        Bitmap bm = BitmapFactory.decodeFile(productImageList.get(j));
                        String someFilepath = productImageList.get(j);
                        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                        if (extension.equals(".png")) {
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos1); //bm is the bitmap object
                        } else {
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos1); //bm is the bitmap object
                        }
                        byte[] imageBytes1 = baos1.toByteArray();
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes1);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image[]", file.getName(), requestFile);
                        productListImage.add(filePart);
                    }

                    for (int i = 0; i < boxImageList.size(); i++) {
                        File file = new File(boxImageList.get(i));
                        Bitmap bm = BitmapFactory.decodeFile(boxImageList.get(i));
                        String someFilepath = boxImageList.get(i);
                        String extension = someFilepath.substring(someFilepath.lastIndexOf("."));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        if (extension.equals(".png")) {
                            bm.compress(Bitmap.CompressFormat.PNG, 10, baos); //bm is the bitmap object
                        } else {
                            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos); //bm is the bitmap object
                        }

                        byte[] imageBytes = baos.toByteArray();
                        Log.d("fileName", file.getName());
                        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData("box_image[]", file.getName(), requestFile);
                        boxListImage.add(filePart);
                    }

                   hideDialog();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request_To_Sell_Product();
            }
        });


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
        mtoolbarTitle.setText(getResources().getString(R.string.your_listing));
    }

    private void initialize() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        txtProductTitle = (TextView) findViewById(R.id.txtProductName);
        txtProductSize = (TextView) findViewById(R.id.txtProductSize);
        txtProductCondtion = (TextView) findViewById(R.id.txtProductCondtion);
        txtProductPrice = (TextView) findViewById(R.id.txt_product_price);
        ivProductImage = (ImageView) findViewById(R.id.iv_ListingImage);

        rvProductImage = (RecyclerView) findViewById(R.id.rv_product_image);
        rvBoxImage = (RecyclerView) findViewById(R.id.rv_boxImage);
        txtProductTitle.setText(mProduct.getProduct_name());
        txtProductPrice.setText("$ " + strPrice);
        txtProductCondtion.setText("Box :" + strCondtion);
        try {
            sizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sizeModelList.size() > 0) {
            for (int i = 0; i < sizeModelList.size(); i++) {
                if (strSize.equals(sizeModelList.get(i).getSizeId())) {
                    sizevalue = sizeModelList.get(i).getSizeValue();
                }
            }
        }

        txtProductSize.setText("Size  " + sizevalue);

        Glide.with(this).load(mProduct.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);
    }

    private void set_ProductImageList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvProductImage.setLayoutManager(layoutManager);
        imageAdpater = new ProductImageAdpater(this, productImageList);
        rvProductImage.setAdapter(imageAdpater);
        rvProductImage.addItemDecoration(new SpacesItemDecoration_horizontal(1));
    }

    private void set_BoxImageList() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvBoxImage.setLayoutManager(layoutManager);
        imageAdpater = new ProductImageAdpater(this, boxImageList);
        rvBoxImage.setAdapter(imageAdpater);
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

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }


    private void request_To_Sell_Product() {

        mRelativeLayout.setVisibility(View.VISIBLE);
        btnSubmit.setVisibility(View.INVISIBLE);


        userId = RequestBody.create(MediaType.parse("text/plain"), MyUtility.getSavedPreferences(this, "id"));
        product_id = RequestBody.create(MediaType.parse("text/plain"), mProduct.getProduct_id());
        product_sku = RequestBody.create(MediaType.parse("text/plain"), mProduct.getProduct_skuno());
        size = RequestBody.create(MediaType.parse("text/plain"), strSize);
        box_condition = RequestBody.create(MediaType.parse("text/plain"), strCondtion);
        issues = RequestBody.create(MediaType.parse("text/plain"), strIssue);
        other_issues = RequestBody.create(MediaType.parse("text/plain"), strOtherissue);
        price = RequestBody.create(MediaType.parse("text/plain"), strPrice);



        Call<SubmitResponse> call = service.prdouctDetail_request_to_sell(userId, product_id, product_sku, size, box_condition, issues, other_issues, price, productListImage, boxListImage);
        call.enqueue(new Callback<SubmitResponse>() {
            @Override
            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {

                if (response.isSuccessful()) {
//                    hideDialog();
                    SubmitResponse apiError = ((SubmitResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.VISIBLE);
                    if (apiError.getStatus() == 0) {
                        Toast.makeText(SubmitListingActivity.this, "" + apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SubmitListingActivity.this, "" + apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(SubmitListingActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<SubmitResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
//                hideDialog();
                mRelativeLayout.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(SubmitListingActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

}