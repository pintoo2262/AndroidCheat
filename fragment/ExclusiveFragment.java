package com.app.noan.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.adapters.ExclusiveProductAdapter;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.listener.RecyclerTouchListener;
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

/**
 * Created by smn on 17/10/17.
 */

@SuppressLint("ValidFragment")
public class ExclusiveFragment extends Fragment implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "Exclusive Fragment";

    // Recycle view
    private PaginationScrollListener mExPaginationScrollListener;
    public ExclusiveProductAdapter mExclusiveAdapter;
    GridLayoutManager layoutManager;

    SwipeRefreshLayout mSwipeRefreshLayoutExculsive;
    RecyclerView rv_Exclusive;

    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES;
    private int currentPage = PAGE_START;


    MetaData metaDatal;
    ApiProduct service;

    // Filter Button
    Button btnfilter;
    ProductFragment mProductFragment;
    String mType = "0", mSize = "0", mCategory = "0", mBrand = "0", mColor = "0";


    public static Fragment newInstance() {
        return new ExclusiveFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exclusive, container, false);


        // initilzation of xml componet
        initilitzation(view);

        service = ApiClient.getClient().create(ApiProduct.class);
        mExclusiveAdapter = new ExclusiveProductAdapter(getActivity(), this, rv_Exclusive);
        rv_Exclusive.setAdapter(mExclusiveAdapter);
        loadFirstPage_ExculsiveProduct();


        mExPaginationScrollListener = new PaginationScrollListener(layoutManager, TOTAL_PAGES) {
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
                }, 1);
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
        rv_Exclusive.addOnScrollListener(mExPaginationScrollListener);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProductFragment.defaultApplicationByServer();
                loadFirstPage_ExculsiveProduct();
            }
        });


        rv_Exclusive.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scroll Down
                    if (btnfilter.isShown()) {
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(ObjectAnimator.ofFloat(btnfilter, View.TRANSLATION_Y, 0, getResources().getDimension(R.dimen.dimen_40dp)));
                        animatorSet.setDuration(200);
                        animatorSet.start();
                        btnfilter.setVisibility(View.GONE);

                    }
                } else if (dy < 0) {
                    // Scroll Up
                    if (!btnfilter.isShown()) {
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(ObjectAnimator.ofFloat(btnfilter, View.TRANSLATION_Y, getResources().getDimension(R.dimen.dimen_100dp), 0));
                        animatorSet.setDuration(200);
                        animatorSet.start();
                        btnfilter.setVisibility(View.VISIBLE);

                    }
                }
            }
        });


        rv_Exclusive.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_Exclusive, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mExclusiveAdapter.exclusiveProductList.get(position).getId() != null) {
                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                    intent.putExtra("product_id", mExclusiveAdapter.exclusiveProductList.get(position).getId());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }

    private void initilitzation(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);


        //sandle initlize
        rv_Exclusive = (RecyclerView) view.findViewById(R.id.rv_exclusive);
        mSwipeRefreshLayoutExculsive = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshExculsive);
        mSwipeRefreshLayoutExculsive.setOnRefreshListener(this);

        rv_Exclusive.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rv_Exclusive.setLayoutManager(layoutManager);
        rv_Exclusive.addItemDecoration(new SpacesItemDecoration(12, 2));


        //Buttton Filter
        btnfilter = (Button) view.findViewById(R.id.btn_filter);
        btnfilter.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Dialog_filter mExclusiveFilter = new Dialog_filter(mProductFragment);
                mExclusiveFilter.show(fm, "Dialog Fragment");
                break;

        }
    }


    private void loadFirstPage_ExculsiveProduct() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", mCategory);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("buy_type", "2");
        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideErrorView();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mSwipeRefreshLayoutExculsive.setRefreshing(false);
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {

                        metaDatal = productResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        List<Product> results = fetchResults(response);
                        mExclusiveAdapter.clear();
                        mExclusiveAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && TOTAL_PAGES != currentPage)
                            mExclusiveAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(getActivity(), "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                mSwipeRefreshLayoutExculsive.setRefreshing(false);
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

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

        Map<String, String> credMap = new HashMap<>();
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", mCategory);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("buy_type", "2");
        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        mExclusiveAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        mExclusiveAdapter.addAll(results);
                        if (TOTAL_PAGES != currentPage)
                            mExclusiveAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }

                } else {
                    // error case
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(getActivity(), "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(getActivity(), "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                mExclusiveAdapter.showRetry(true, fetchErrorMessage(t));
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

    public void triggerToNotify_rv(String sType, String sSize, String sCategory, String sBrand, String sColor) {
//        Log.d(TAG, "d____type" + sType);
//        Log.d(TAG, "d____size" + sSize);
//        Log.d(TAG, "d____category" + sCategory);
//        Log.d(TAG, "d____brand" + sBrand);
        if (sType.equals("")) {
            mType = "0";
        } else {
            mType = sType;
        }
        if (sSize.equals("")) {
            mSize = "0";
        } else {
            mSize = sSize;
        }
        if (sCategory.equals("")) {
            mCategory = "0";
        } else {
            mCategory = sCategory;
        }
        if (sBrand.equals("")) {
            mBrand = "0";
        } else {
            mBrand = sBrand;
        }
        if (sColor.equals("")) {
            mColor = "0";
        } else {
            mColor = sBrand;
        }


        progressBar.setVisibility(View.VISIBLE);
        mExclusiveAdapter.clear();
        mExclusiveAdapter.isEmpty();
        mExPaginationScrollListener.resetState();
        mExclusiveAdapter.notifyDataSetChanged();
        loadFirstPage_ExculsiveProduct();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            // fragment is visible and have created
            MyUtility.screenType = 2;
        }
    }

    public void setArguments(ProductFragment productFragment) {
        mProductFragment = productFragment;
    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mExclusiveAdapter.clear();
        mExclusiveAdapter.isEmpty();
        mExPaginationScrollListener.resetState();
        mExclusiveAdapter.notifyDataSetChanged();
        loadFirstPage_ExculsiveProduct();
    }
}