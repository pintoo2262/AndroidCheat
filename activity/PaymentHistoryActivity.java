package com.app.noan.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.PaymentHistoryAdapter;
import com.app.noan.adapters.PaymentHistoryAdpater1;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.model.MetaData;
import com.app.noan.model.Payment;
import com.app.noan.model.PaymentHistoryModel;
import com.app.noan.model.PaymentHistoryResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentHistoryActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private String TAG = PaymentHistoryActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private RecyclerView rvPaymentHistory;
    SwipeRefreshLayout mSwipeRefreshLayout;
    PaymentHistoryAdpater1 mHistoryAdpater;
    LinearLayoutManager mLayoutManager;
    private PaginationScrollListener mPaginationScrollListener;


    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError, txtWalletBalnce;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage;
    MetaData metaDatal;
    APILogin mApiLogin;
    String mUserId;


    private List<Payment> paymentList;
    PaymentHistoryAdapter paymentHistoryAdapter;

    private String paymentDescrption[] = {"Your Paypal Account has be Creadited", "Sales from Order #123456 ", "Your Paypal Account has be Creadited", "Sales from Order #123456 "
    };
    private String paymentAmount[] = {"\u0024 150", "\u0024 50", "\u0024 100", "\u0024 150"
    };
    private String paymentDate[] = {"01/01/2016", "01/01/2016", "01/01/2016", "01/01/2016"
    };
    private String paymentTransactionmnp[] = {"Transaction No:1253456", "Transaction No:1253456", "Transaction No:1253456", "Transaction No:1253456"
    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);

        toolBarAndDrawerInitilization();

        mApiLogin = ApiClient.getClient().create(APILogin.class);
        mUserId = MyUtility.getSavedPreferences(PaymentHistoryActivity.this, "id");
        // initilzation of xml componet
        initialization();

        // set Data in Recycle view
        setPaymentHistoryData();

        mPaginationScrollListener = new PaginationScrollListener(mLayoutManager, TOTAL_PAGES) {
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
                }, 3);
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
        rvPaymentHistory.addOnScrollListener(mPaginationScrollListener);

        loadFirstPagePaymentHistory();
    }

    private void setPaymentHistoryData() {

        rvPaymentHistory.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mHistoryAdpater = new PaymentHistoryAdpater1(this);
        rvPaymentHistory.setLayoutManager(mLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(rvPaymentHistory.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.bottom_line));
        rvPaymentHistory.addItemDecoration(divider);
        rvPaymentHistory.setAdapter(mHistoryAdpater);


    }

    private void initialization() {
        rvPaymentHistory = (RecyclerView) findViewById(R.id.rv_paymentHistory);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipePaymentHistory);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);
        txtWalletBalnce = (TextView) findViewById(R.id.txt_paymetn_Amount);

    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.paymenthistory));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_help:
                break;

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public void onRefresh() {
        mPaginationScrollListener.resetState();
        loadFirstPagePaymentHistory();
    }


    private void loadFirstPagePaymentHistory() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_code", mUserId);
        credMap.put("page", String.valueOf(currentPage));
        Call<PaymentHistoryResponse> historyResponseCall = mApiLogin.userPaymentHistory(credMap);
        historyResponseCall.enqueue(new Callback<PaymentHistoryResponse>() {
            @Override
            public void onResponse(Call<PaymentHistoryResponse> call, Response<PaymentHistoryResponse> response) {
                hideErrorView();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    PaymentHistoryResponse historyResponse = ((PaymentHistoryResponse) response.body());
                    if (historyResponse.getStatus() == 1) {
                        txtWalletBalnce.setText("$ " + historyResponse.getWalletBalance());
                        metaDatal = historyResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        List<PaymentHistoryModel> results = fetchResults(response);
                        mHistoryAdpater.clearData();
                        mHistoryAdpater.addAll(results);
                        if (currentPage <= TOTAL_PAGES && TOTAL_PAGES != currentPage)
                            mHistoryAdpater.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(PaymentHistoryActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(PaymentHistoryActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(PaymentHistoryActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentHistoryResponse> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                showErrorView(t);
            }
        });


    }

    private List<PaymentHistoryModel> fetchResults(Response<PaymentHistoryResponse> response) {
        PaymentHistoryResponse topRatedMovies = response.body();
        return topRatedMovies.getData();
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
        credMap.put("user_code", mUserId);
        credMap.put("page", String.valueOf(currentPage));
        Call<PaymentHistoryResponse> historyResponseCall = mApiLogin.userPaymentHistory(credMap);
        historyResponseCall.enqueue(new Callback<PaymentHistoryResponse>() {
            @Override
            public void onResponse(Call<PaymentHistoryResponse> call, Response<PaymentHistoryResponse> response) {
                if (response.isSuccessful()) {
                    PaymentHistoryResponse historyResponse = ((PaymentHistoryResponse) response.body());
                    if (historyResponse.getStatus() == 1) {
                        mHistoryAdpater.removeLoadingFooter();
                        isLoading = false;
                        List<PaymentHistoryModel> results = response.body().getData();
                        mHistoryAdpater.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mHistoryAdpater.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(PaymentHistoryActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(PaymentHistoryActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(PaymentHistoryActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentHistoryResponse> call, Throwable t) {
                t.printStackTrace();
                mHistoryAdpater.showRetry(true, fetchErrorMessage(t));
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
        ConnectivityManager cm = (ConnectivityManager) PaymentHistoryActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}
