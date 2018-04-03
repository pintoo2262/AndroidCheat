package com.example.jay.myappbasic.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import com.example.jay.myappbasic.R;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.example.jay.myappbasic.adapter.GridAdapter;
import com.example.jay.myappbasic.listener.PaginationScrollListener;
import com.example.jay.myappbasic.listener.PaginationAdapterCallback;
import com.example.jay.myappbasic.listener.RecyclerTouchListener;
import com.example.jay.myappbasic.utils.SpacesItemDecoration;


public class BFragment extends Fragment implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {


    private static final String TAG = "BFragment";
    private PaginationScrollListener mPaginationScrollListener;


    // Recycle view
    SwipeRefreshLayout mSwipeRefreshLayoutBuynow;
    RecyclerView rv_Product;
    public GridAdapter mProductAdapter;
    GridLayoutManager gridLayoutManager;


    // Filter Button
    Button btnfilter;


    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;


    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage;


    String mType = "0", mSize = "0", mCategory = "0", mBrand = "0", mColor = "0";
//    MetaData metaDatal;
  //  ApiProduct service;

    public static boolean shown = false;

    public BFragment() {
    }

    public static Fragment newInstance() {
        return new BFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_b, container, false);

        // initilzation of xml componet
        initilitzation(view);
//        service = ApiClient.getClient().create(ApiProduct.class);


        mProductAdapter = new GridAdapter(getActivity(), this, rv_Product);
        rv_Product.setAdapter(mProductAdapter);

//        loadFirstPage_BuyNowProduct();


        mPaginationScrollListener = new PaginationScrollListener(gridLayoutManager, TOTAL_PAGES) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        loadNextPage();
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
        rv_Product.addOnScrollListener(mPaginationScrollListener);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                loadFirstPage_BuyNowProduct();
            }
        });





        rv_Product.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_Product, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mProductAdapter.productModelList.get(position).getId() != null) {
//                    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
//                    intent.putExtra("product_id", mProductAdapter.productModelList.get(position).getId());
//                    startActivity(intent);
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
        rv_Product = (RecyclerView) view.findViewById(R.id.rv_sandle);
        mSwipeRefreshLayoutBuynow = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshBuyNow);
        mSwipeRefreshLayoutBuynow.setOnRefreshListener(this);


        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rv_Product.setLayoutManager(gridLayoutManager);
        rv_Product.addItemDecoration(new SpacesItemDecoration(12, 2));



    }

    @Override
    public void onStart() {
        super.onStart();
    }


/*
    private void loadFirstPage_BuyNowProduct() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("color", mColor);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("buy_type", "0");
        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                hideErrorView();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mSwipeRefreshLayoutBuynow.setRefreshing(false);
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        metaDatal = productResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        List<Product> results = fetchResults(response);
                        mProductAdapter.clearData();
                        mProductAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && TOTAL_PAGES != currentPage)
                            mProductAdapter.addLoadingFooter();
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
                mSwipeRefreshLayoutBuynow.setRefreshing(false);
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }
*/

   /* private List<Product> fetchResults(Response<ProductResponse> response) {
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
        credMap.put("buy_type", "0");
        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        mProductAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        mProductAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mProductAdapter.addLoadingFooter();
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
                mProductAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }*/

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
//        loadNextPage();
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            // fragment is visible and have created

        }
    }





    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mProductAdapter.clear();
        mProductAdapter.isEmpty();
        mPaginationScrollListener.resetState();
        mProductAdapter.notifyDataSetChanged();
//        loadFirstPage_BuyNowProduct();
    }

    @Override
    public void onClick(View v) {

    }
}
