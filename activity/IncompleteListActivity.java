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

public class IncompleteListActivity extends AppCompatActivity implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = IncompleteListActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;

    private SwipeRefreshLayout mSwipeRefreshLayoutIncompleteListing;
    private RecyclerView rv_Incompletlist;
    private PaginationScrollListener mPaginationScrollListener;
    ListingForSellAdapter mIncompleteAdapter;
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


    private Button btnAddNew;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomplete_list);

        toolBarAndDrawerInitilization();
        isUserSeller = MyUtility.getSavedPreferences(IncompleteListActivity.this, "sellerType");
        initialize();

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
        rv_Incompletlist.addOnScrollListener(mPaginationScrollListener);


        rv_Incompletlist.addOnItemTouchListener(new RecyclerTouchListener(this, rv_Incompletlist, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (!isUserSeller.equals("vendorseller")) {
                    Intent intent = new Intent(IncompleteListActivity.this, DisplaySellImageActivity.class);
                    intent.putExtra("Productdatum", (Serializable) mIncompleteAdapter.productdatumList.get(position));
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incompletList_ByServer();
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
        mtoolbarTitle.setText(getResources().getString(R.string.incomplet_list));
    }

    private void initialize() {
        rv_Incompletlist = (RecyclerView) findViewById(R.id.rv_incomplete_list);
        mSwipeRefreshLayoutIncompleteListing = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshIncompleteListing);
        mSwipeRefreshLayoutIncompleteListing.setOnRefreshListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        // userId
        userId = MyUtility.getSavedPreferences(this, "id");
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_Incompletlist.setLayoutManager(linearLayoutManager);
        rv_Incompletlist.setHasFixedSize(true);

        setAdapater();

        btnAddNew = (Button) findViewById(R.id.btn_list_new);
        btnAddNew.setOnClickListener(this);
    }

    private void setAdapater() {
        mIncompleteAdapter = new ListingForSellAdapter(IncompleteListActivity.this, "incomplete", "");
        rv_Incompletlist.setAdapter(mIncompleteAdapter);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_list_new:
                startActivity(new Intent(IncompleteListActivity.this, SearchSaleActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapater();
        mIncompleteAdapter.clear();
        mIncompleteAdapter.isEmpty();
        mIncompleteAdapter.notifyDataSetChanged();
        mPaginationScrollListener.resetState();
        incompletList_ByServer();
    }

    private void incompletList_ByServer() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_seller_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<ProductForSellResponse> forSellResponseCall = service.InCompleteLisingSell(credMap);
        forSellResponseCall.enqueue(new Callback<ProductForSellResponse>() {
            @Override
            public void onResponse(Call<ProductForSellResponse> call, Response<ProductForSellResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    ProductForSellResponse sellResponse = ((ProductForSellResponse) response.body());
                    mSwipeRefreshLayoutIncompleteListing.setRefreshing(false);
                    if (sellResponse.getStatus() == 1) {
                        metaDatal = sellResponse.getMetadata();
                        getTotalPageDynamic(metaDatal);
                        mIncompleteAdapter.clear();
                        List<Productdatum> results = fetchResults(response);
                        progressBar.setVisibility(View.GONE);
                        mIncompleteAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mIncompleteAdapter.addLoadingFooter();
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
        credMap.put("user_seller_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<ProductForSellResponse> productResponseCall = service.InCompleteLisingSell(credMap);
        productResponseCall.enqueue(new Callback<ProductForSellResponse>() {
            @Override
            public void onResponse(Call<ProductForSellResponse> call, Response<ProductForSellResponse> response) {
                if (response.isSuccessful()) {
                    ProductForSellResponse sellResponse = ((ProductForSellResponse) response.body());
                    if (sellResponse.getStatus() == 1) {
                        mIncompleteAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Productdatum> results = response.body().getProductdata();
                        mIncompleteAdapter.addAll(results);
                        if (TOTAL_PAGES != currentPage)
                            mIncompleteAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<ProductForSellResponse> call, Throwable t) {
                t.printStackTrace();
                mIncompleteAdapter.showRetry(true, fetchErrorMessage(t));
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
        mIncompleteAdapter.clear();
        mIncompleteAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        mIncompleteAdapter.notifyDataSetChanged();
        incompletList_ByServer();
    }
}
