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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.adapters.NeedConfirmAdapter;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.MetaData;
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.model.NeedToConfirmResponse;
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

public class NeedToConfirmActivity extends AppCompatActivity implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private ImageButton ibtnCredit;


    private SwipeRefreshLayout mSwipeRefreshLayoutNeedtoConfrimList;
    private RecyclerView rv_needConfirmList;
    private PaginationScrollListener mPaginationScrollListener;
    NeedConfirmAdapter mNeedConfirmAdapter;
    LinearLayoutManager layoutManager;

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
    String userId;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_need_to_confirm);

        toolBarAndDrawerInitilization();

        initialize();


        mPaginationScrollListener = new PaginationScrollListener(layoutManager, TOTAL_PAGES) {
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
        rv_needConfirmList.addOnScrollListener(mPaginationScrollListener);


        rv_needConfirmList.addOnItemTouchListener(new RecyclerTouchListener(this, rv_needConfirmList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(NeedToConfirmActivity.this, ConfirmSoldeActivity.class);
                intent.putExtra("NeedToConfirmModel", (Serializable) mNeedConfirmAdapter.needToConfirmModelList.get(position));
                startActivity(intent);

                overridePendingTransition(R.anim.enter, R.anim.exit);
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
        mtoolbarTitle.setText(getResources().getString(R.string.needtoconfirm));
        ibtnCredit = mToolbar.findViewById(R.id.ibtn_credit);
        ibtnCredit.setOnClickListener(this);
    }

    private void initialize() {
        rv_needConfirmList = (RecyclerView) findViewById(R.id.rv_confirm_sales);
        mSwipeRefreshLayoutNeedtoConfrimList = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshNeedtoconfirmlist);
        mSwipeRefreshLayoutNeedtoConfrimList.setOnRefreshListener(this);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        // userId
        userId = MyUtility.getSavedPreferences(this, "id");
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_needConfirmList.setLayoutManager(layoutManager);
        rv_needConfirmList.setHasFixedSize(true);
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
            case R.id.ibtn_credit:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setAdapater();
        progressBar.setVisibility(View.VISIBLE);
        mNeedConfirmAdapter.clear();
        mNeedConfirmAdapter.isEmpty();
        mNeedConfirmAdapter.notifyDataSetChanged();
        mPaginationScrollListener.resetState();
        confirmList_ByFirstPage();
    }

    private void setAdapater() {
        mNeedConfirmAdapter = new NeedConfirmAdapter(NeedToConfirmActivity.this);
        rv_needConfirmList.setAdapter(mNeedConfirmAdapter);
    }


    private void confirmList_ByFirstPage() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("status", "pending");
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<NeedToConfirmResponse> forSellResponseCall = service.needToConfirmList(credMap);
        forSellResponseCall.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    NeedToConfirmResponse sellResponse = ((NeedToConfirmResponse) response.body());
                    mSwipeRefreshLayoutNeedtoConfrimList.setRefreshing(false);
                    if (sellResponse.getStatus() == 1) {
                        metaDatal = sellResponse.getMetadata();
                        getTotalPageDynamic(metaDatal);
                        mNeedConfirmAdapter.clear();
                        List<NeedToConfirmModel> results = fetchResults(response);
                        progressBar.setVisibility(View.GONE);
                        mNeedConfirmAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mNeedConfirmAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<NeedToConfirmResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private List<NeedToConfirmModel> fetchResults(Response<NeedToConfirmResponse> response) {
        NeedToConfirmResponse forSellResponse = response.body();
        return forSellResponse.getNeedToConfirmModelList();
    }

    private void getTotalPageDynamic(MetaData metaDatal) {

        int modulos = Integer.parseInt(metaDatal.getTableTotalRows()) % Integer.parseInt(metaDatal.getRecordPerPage());
        int total1 = Integer.parseInt(metaDatal.getTableTotalRows()) / Integer.parseInt(metaDatal.getRecordPerPage());
        if (modulos != 0) {
            TOTAL_PAGES = total1 + 1;
        } else if (modulos == 0) {
            TOTAL_PAGES = total1;
        }

    }


    private void loadNextPage() {

        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("status", "pending");
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<NeedToConfirmResponse> productResponseCall = service.needToConfirmList(credMap);
        productResponseCall.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {
                if (response.isSuccessful()) {
                    NeedToConfirmResponse sellResponse = ((NeedToConfirmResponse) response.body());

                    if (sellResponse.getStatus() == 1) {
                        mNeedConfirmAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<NeedToConfirmModel> results = response.body().getNeedToConfirmModelList();
                        mNeedConfirmAdapter.addAll(results);
                        if (TOTAL_PAGES != currentPage)
                            mNeedConfirmAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<NeedToConfirmResponse> call, Throwable t) {
                t.printStackTrace();
                mNeedConfirmAdapter.showRetry(true, fetchErrorMessage(t));
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
        mNeedConfirmAdapter.clear();
        mNeedConfirmAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        mNeedConfirmAdapter.notifyDataSetChanged();
        confirmList_ByFirstPage();
    }
}
