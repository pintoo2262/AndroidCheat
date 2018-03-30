package com.app.noan.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.app.noan.adapters.ProfileMyAdapter;
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

public class ProfileMyFragment extends Fragment implements PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = ProfileNeedAdapter.class.getSimpleName();

    Activity mActivity;
    ViewPager mViewPager;

    //recyclerview
    SwipeRefreshLayout mSwipeRefreshLayoutMy;
    private RecyclerView rv_MyList;
    private ProfileMyAdapter mMyAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PaginationScrollListener mPaginationScrollListener;

    // Adapter
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

    public void setArguments(ViewPager mHomeViewpager, Activity mActivity) {
        this.mViewPager = mHomeViewpager;
        this.mActivity = mActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profilemy, container, false);

        initialization(view);
        userId = MyUtility.getSavedPreferences(getActivity(), "id");

        if (userId != null) {

            mMyAdapter = new ProfileMyAdapter(getActivity(), this, mViewPager);
            rv_MyList.setAdapter(mMyAdapter);
            myProductListByServer_FirstPage();
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
            rv_MyList.addOnScrollListener(mPaginationScrollListener);


            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myProductListByServer_FirstPage();
                }
            });
        }


        return view;
    }

    private void initialization(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        // initilization
        rv_MyList = (RecyclerView) view.findViewById(R.id.rv_profileown);
        mSwipeRefreshLayoutMy = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshProfile);
        mSwipeRefreshLayoutMy.setOnRefreshListener(this);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_MyList.setLayoutManager(linearLayoutManager);
        rv_MyList.setItemAnimator(new DefaultItemAnimator());


    }

    private void myProductListByServer_FirstPage() {
        Log.d(TAG, "myProductListByServer_FirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        service = ApiClient.getClient().create(ApiProduct.class);
        Call<ProductResponse> callMyProductList = service.myProductList(credMap);
        callMyProductList.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideErrorView();
                ProductResponse productResponse = ((ProductResponse) response.body());
                mSwipeRefreshLayoutMy.setRefreshing(false);
                if (productResponse.getStatus() == 1) {
                    metaDatal = productResponse.getMetaData();
                    getTotalPageDynamic(metaDatal);
                    List<Product> results = fetchResults(response);
                    progressBar.setVisibility(View.GONE);
                    mMyAdapter.addAll(results);
                    if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                        mMyAdapter.addLoadingFooter();
                    else
                        isLastPage = true;
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
        Call<ProductResponse> callMyProductList = service.myProductList(credMap);
        callMyProductList.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        mMyAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = fetchResults(response);
                        mMyAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mMyAdapter.addLoadingFooter();
                        else
                            isLastPage = true;

                    }

                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                mMyAdapter.showRetry(true, fetchErrorMessage(t));
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
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created

        }
    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mMyAdapter.clear();
        mMyAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        mMyAdapter.notifyDataSetChanged();
        myProductListByServer_FirstPage();
    }
}



