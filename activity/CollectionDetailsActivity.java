package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.CollectionAdpater;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Collection;
import com.app.noan.model.CollectionDetailsResponse;
import com.app.noan.model.ProductCollection;
import com.app.noan.retrofit_api.APICollection;
import com.app.noan.retrofit_api.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CollectionDetailsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle, txtCollectionTitle, txtCollectionshortDescrption, txt_collectionLongDescrpriton;
    String id;
    Dialog pDialog;
    APICollection mApiCollection;

    ImageView ivCollectionImage;
    RecyclerView mCollectionRecycleview;
    GridLayoutManager gridLayoutManager;
    CollectionAdpater mCollectionAdpater;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_collection_details);

        mApiCollection = ApiClient.getClient().create(APICollection.class);
        id = getIntent().getStringExtra("collection_id");
        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

        //
        collectionByServer(id);

        mCollectionAdpater = new CollectionAdpater(CollectionDetailsActivity.this, mCollectionRecycleview);
        mCollectionRecycleview.setAdapter(mCollectionAdpater);


        mCollectionRecycleview.addOnItemTouchListener(new RecyclerTouchListener(CollectionDetailsActivity.this, mCollectionRecycleview, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mCollectionAdpater.productModelList.get(position).getId() != null) {
                    Intent intent = new Intent(CollectionDetailsActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("product_id", mCollectionAdpater.productModelList.get(position).getProductId());
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }

    private void initialization() {
        ivCollectionImage = (ImageView) findViewById(R.id.iv_colllectionImage);
        txtCollectionTitle = (TextView) findViewById(R.id.txt_collectionTitle);
        txtCollectionshortDescrption = (TextView) findViewById(R.id.txt_collectionshortDescrpriton);
        txt_collectionLongDescrpriton = (TextView) findViewById(R.id.txt_collectionLongDescrpriton);
        mCollectionRecycleview = (RecyclerView) findViewById(R.id.rv_Collection);

        gridLayoutManager = new GridLayoutManager(CollectionDetailsActivity.this, 2);
        mCollectionRecycleview.setLayoutManager(gridLayoutManager);
        mCollectionRecycleview.addItemDecoration(new SpacesItemDecoration(12, 2));
        mCollectionRecycleview.setNestedScrollingEnabled(false);
        mCollectionRecycleview.setHasFixedSize(false);

    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbarCreate);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mToolbarTitle.setText(getResources().getString(R.string.ownercollectionDetails));
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

    private void collectionByServer(String collectiondId) {
        pDialog = new Dialog(CollectionDetailsActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("collection_id", collectiondId);
        Call<CollectionDetailsResponse> detailsResponseCall = mApiCollection.collectionDetails(credMap);
        detailsResponseCall.enqueue(new Callback<CollectionDetailsResponse>() {
            @Override
            public void onResponse(Call<CollectionDetailsResponse> call, Response<CollectionDetailsResponse> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    CollectionDetailsResponse detailsResponse = ((CollectionDetailsResponse) response.body());
                    if (detailsResponse.getStatus() == 1) {
                        Collection mCollection = detailsResponse.getCollectionl();
                        setCollectionData(mCollection);
                        List<ProductCollection> results = fetchResults(response);
                        mCollectionAdpater.addAll(results);
                    } else {
                        Toast.makeText(CollectionDetailsActivity.this, "" + detailsResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<CollectionDetailsResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(CollectionDetailsActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }

    private List<ProductCollection> fetchResults(Response<CollectionDetailsResponse> response) {
        CollectionDetailsResponse topRatedMovies = response.body();
        return topRatedMovies.getData();
    }

    private void setCollectionData(Collection mCollection) {
        Glide.with(this).load(mCollection.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivCollectionImage);
        txtCollectionTitle.setText(mCollection.getName());
        txtCollectionshortDescrption.setText(mCollection.getShortDescription());
        txt_collectionLongDescrpriton.setText(mCollection.getLongDescription());
    }

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }


}
