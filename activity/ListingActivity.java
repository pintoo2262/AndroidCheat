package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.adapters.ListingForSellAdapter;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.MetaData;
import com.app.noan.model.ProductForSellResponse;
import com.app.noan.model.Productdatum;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ListingActivity extends AppCompatActivity implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ListingActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;


    private Button btnAddNew;
    private SwipeRefreshLayout mSwipeRefreshLayoutListing;
    private RecyclerView rvForSellProduct;
    private PaginationScrollListener mPaginationScrollListener;

    ListingForSellAdapter mForSellAdapter;
    LinearLayoutManager linearLayoutManager;

    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since productPrice pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;


    MetaData metaDatal;
    APIRequestSell service;
    String userId, isUserSeller;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);

        toolBarAndDrawerInitilization();

        userId = MyUtility.getSavedPreferences(this, "id");
        isUserSeller = MyUtility.getSavedPreferences(ListingActivity.this, "sellerType");
        initialize();

        setAdapater();

        listing_firstPageByserver();

        mPaginationScrollListener = new PaginationScrollListener(linearLayoutManager, TOTAL_PAGES) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 50);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        };
        rvForSellProduct.addOnScrollListener(mPaginationScrollListener);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listing_firstPageByserver();
            }
        });

        rvForSellProduct.addOnItemTouchListener(new RecyclerTouchListener(this, rvForSellProduct, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (!isUserSeller.equals("vendorseller")) {
                    Intent intent = new Intent(ListingActivity.this, DisplaySellImageActivity.class);
                    intent.putExtra("Productdatum", (Serializable) mForSellAdapter.productdatumList.get(position));
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
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
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.black));
        mtoolbarTitle.setText(getResources().getString(R.string.sell_for));
    }

    private void initialize() {
        rvForSellProduct = (RecyclerView) findViewById(R.id.rv_active_sell_list);
        mSwipeRefreshLayoutListing = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshListing);
        mSwipeRefreshLayoutListing.setOnRefreshListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        // userId

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvForSellProduct.setLayoutManager(linearLayoutManager);
        rvForSellProduct.setHasFixedSize(true);
        btnAddNew = (Button) findViewById(R.id.btn_list_new);

        if (isUserSeller.equals("vendorseller")) {
            btnAddNew.setVisibility(View.GONE);
        }
        btnAddNew.setOnClickListener(this);
    }

    private void setAdapater() {
        mForSellAdapter = new ListingForSellAdapter(ListingActivity.this, "Listing", isUserSeller);
        rvForSellProduct.setAdapter(mForSellAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_list_new:
                startActivity(new Intent(ListingActivity.this, SearchSaleActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
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
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void listing_firstPageByserver() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<ProductForSellResponse> forSellResponseCall = service.displayProductForSell(credMap);
        forSellResponseCall.enqueue(new Callback<ProductForSellResponse>() {
            @Override
            public void onResponse(Call<ProductForSellResponse> call, Response<ProductForSellResponse> response) {

                hideErrorView();
                mSwipeRefreshLayoutListing.setRefreshing(false);
                if (response.isSuccessful()) {
                    ProductForSellResponse sellResponse = ((ProductForSellResponse) response.body());
                    if (sellResponse.getStatus() == 1) {
                        metaDatal = sellResponse.getMetadata();
                        getTotalPageDynamic(metaDatal);
                        mForSellAdapter.clear();
                        List<Productdatum> results = fetchResults(response);
                        progressBar.setVisibility(View.GONE);
                        mForSellAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mForSellAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductForSellResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<Productdatum> fetchResults(Response<ProductForSellResponse> response) {
        ProductForSellResponse forSellResponse = response.body();
        return forSellResponse.getProductdata();
    }

    private void getTotalPageDynamic(MetaData metaDatal) {

        int modulos = Integer.parseInt(metaDatal.getTableTotalRows()) % Integer.parseInt(metaDatal.getRecordPerPage());
        int total1 = Integer.parseInt(metaDatal.getTableTotalRows()) / Integer.parseInt(metaDatal.getRecordPerPage());
        if (modulos != 0) {
            TOTAL_PAGES = total1 + 1;
        } else if (modulos == 0) {
            TOTAL_PAGES = total1;
        }
        Log.d(TAG, "TotalPage--" + TOTAL_PAGES);
    }


    private void loadNextPage() {

        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<ProductForSellResponse> productResponseCall = service.displayProductForSell(credMap);
        productResponseCall.enqueue(new Callback<ProductForSellResponse>() {
            @Override
            public void onResponse(Call<ProductForSellResponse> call, Response<ProductForSellResponse> response) {
                if (response.isSuccessful()) {
                    ProductForSellResponse sellResponse = ((ProductForSellResponse) response.body());
                    if (sellResponse.getStatus() == 1) {
                        mForSellAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Productdatum> results = response.body().getProductdata();
                        mForSellAdapter.addAll(results);
                        if (TOTAL_PAGES != currentPage)
                            mForSellAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductForSellResponse> call, Throwable t) {
                t.printStackTrace();
                mForSellAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mForSellAdapter.clear();
        mForSellAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        mForSellAdapter.notifyDataSetChanged();
        listing_firstPageByserver();
    }
}
