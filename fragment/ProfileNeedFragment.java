package com.app.noan.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.adapters.ProfileNeedAdapter;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.model.MetaData;
import com.app.noan.model.Product;
import com.app.noan.model.ProductResponse;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileNeedFragment extends Fragment implements PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ProfileNeedAdapter.class.getSimpleName();
    Activity mActivity;

    //recyclerview
    SwipeRefreshLayout mSwipeRefreshLayoutNeed;
    private RecyclerView rv_needList;
    private PaginationScrollListener mPaginationScrollListener;

    // Adapter
    private ProfileNeedAdapter profileNeedAdapter;
    LinearLayoutManager linearLayoutManager;


    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;


    MetaData metaDatal;
    ApiProduct service;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_needlist, container, false);

        userId = MyUtility.getSavedPreferences(getContext(), "id");
        service = ApiClient.getClient().create(ApiProduct.class);

        initialization(view);


        profileNeedAdapter = new ProfileNeedAdapter(mActivity, this);
        rv_needList.setAdapter(profileNeedAdapter);


        needProductList_FirstPage();

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
        rv_needList.addOnScrollListener(mPaginationScrollListener);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                needProductList_FirstPage();
            }
        });


        return view;
    }

    private void initialization(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        rv_needList = (RecyclerView) view.findViewById(R.id.rv_profilewant);
        mSwipeRefreshLayoutNeed = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshNeed);
        mSwipeRefreshLayoutNeed.setOnRefreshListener(this);


        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        rv_needList.setLayoutManager(linearLayoutManager);
        rv_needList.setItemAnimator(new DefaultItemAnimator());


    }


    private void needProductList_FirstPage() {
        Log.d(TAG, "needProductList_FirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));

        Call<ProductResponse> callMyProductList = service.needProductList(credMap);
        callMyProductList.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideErrorView();
                ProductResponse productResponse = ((ProductResponse) response.body());
                mSwipeRefreshLayoutNeed.setRefreshing(false);
                if (productResponse.getStatus() == 1) {
                    metaDatal = productResponse.getMetaData();
                    getTotalPageDynamic(metaDatal);
                    profileNeedAdapter.clear();
                    progressBar.setVisibility(View.GONE);
                    List<Product> results = fetchResults(response);
                    profileNeedAdapter.addAll(results);
                    if (currentPage <= TOTAL_PAGES && TOTAL_PAGES != currentPage)
                        profileNeedAdapter.addLoadingFooter();
                    else isLastPage = true;
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });


    }

    /**
     * @param response extracts List<{@link Result>} from response
     * @return
     */
    private List<Product> fetchResults(Response<ProductResponse> response) {
        ProductResponse topRatedMovies = response.body();
        return topRatedMovies.getProductList();
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
        Log.d(TAG, "loadNextPage: " + currentPage);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(ApiProduct.class);
        Call<ProductResponse> callMyProductList = service.needProductList(credMap);
        callMyProductList.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        profileNeedAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        profileNeedAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            profileNeedAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                profileNeedAdapter.showRetry(true, fetchErrorMessage(t));
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
        String errorMsg = mActivity.getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }


    public void setArguments(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        profileNeedAdapter.clear();
        profileNeedAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        profileNeedAdapter.notifyDataSetChanged();
        needProductList_FirstPage();
    }
}
