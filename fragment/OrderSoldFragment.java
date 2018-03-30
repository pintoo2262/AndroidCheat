package com.app.noan.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.OrderDetailActivity;
import com.app.noan.adapters.OrderAdapter;
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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by smn on 19/9/17.
 */

public class OrderSoldFragment extends Fragment implements PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "OrderSoldFragment";

    private RecyclerView rv_OrderSold;
    private SwipeRefreshLayout mSwipeRefreshLayoutSoldList;
    private PaginationScrollListener mOSoldPaginationScrollListener;
    public EditText edt_search_sold;

    private OrderAdapter mOrderSoldAdapter;
    LinearLayoutManager linearLayoutManager;


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
    APIRequestSell service;
    String userId;
    private String searchText = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sold, container, false);
        initilitzation(view);


        mOrderSoldAdapter = new OrderAdapter(getActivity(), this);
        rv_OrderSold.setAdapter(mOrderSoldAdapter);


        mOSoldPaginationScrollListener = new PaginationScrollListener(linearLayoutManager, TOTAL_PAGES) {
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
        rv_OrderSold.addOnScrollListener(mOSoldPaginationScrollListener);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderSold_byUserFirstPage();
            }
        });


        rv_OrderSold.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_OrderSold, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("NeedConfirmModel", (Serializable) mOrderSoldAdapter.needToConfirmModelList.get(position));
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        return view;
    }


    private void initilitzation(View view) {
        rv_OrderSold = (RecyclerView) view.findViewById(R.id.rv_sold);
        mSwipeRefreshLayoutSoldList = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshSoldList);
        mSwipeRefreshLayoutSoldList.setOnRefreshListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        edt_search_sold = view.findViewById(R.id.et_search_product_sold);

        // userId
        userId = MyUtility.getSavedPreferences(getActivity(), "id");


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_OrderSold.setLayoutManager(linearLayoutManager);
        rv_OrderSold.setHasFixedSize(true);


        edt_search_sold.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText = edt_search_sold.getText().toString().toLowerCase(Locale.getDefault());
//                mOrderSoldAdapter.filter(search);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                progressBar.setVisibility(View.VISIBLE);
                mOrderSoldAdapter.clear();
                mOrderSoldAdapter.isEmpty();
                mOSoldPaginationScrollListener.resetState();
                mOrderSoldAdapter.notifyDataSetChanged();
                orderSold_byUserFirstPage();
            }
        });

    }


    private void orderSold_byUserFirstPage() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_seller_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("search_text", searchText);

        service = ApiClient.getClient().create(APIRequestSell.class);

        Call<NeedToConfirmResponse> forSellResponseCall = service.userSoldDetail(credMap);
        forSellResponseCall.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {
                hideErrorView();
                if (response.isSuccessful()) {
                    NeedToConfirmResponse needToConfirmResponse = ((NeedToConfirmResponse) response.body());
                    mSwipeRefreshLayoutSoldList.setRefreshing(false);
                    if (needToConfirmResponse.getStatus() == 1) {
                        metaDatal = needToConfirmResponse.getMetadata();
                        getTotalPageDynamic(metaDatal);
                        progressBar.setVisibility(View.GONE);
                        List<NeedToConfirmModel> results = fetchResults(response);
                        mOrderSoldAdapter.clear();
                        mOrderSoldAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mOrderSoldAdapter.addLoadingFooter();
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
        Log.d(TAG, "TotalPage--" + TOTAL_PAGES);
    }


    private void loadNextPage() {

        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_seller_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("search_text", searchText);

        service = ApiClient.getClient().create(APIRequestSell.class);
        Call<NeedToConfirmResponse> productResponseCall = service.userSoldDetail(credMap);
        productResponseCall.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {
                if (response.isSuccessful()) {
                    NeedToConfirmResponse needToConfirmModel = ((NeedToConfirmResponse) response.body());
                    if (needToConfirmModel.getStatus() == 1) {
                        mOrderSoldAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<NeedToConfirmModel> results = response.body().getNeedToConfirmModelList();
                        mOrderSoldAdapter.addAll(results);
                        if (TOTAL_PAGES != currentPage)
                            mOrderSoldAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<NeedToConfirmResponse> call, Throwable t) {
                t.printStackTrace();
                mOrderSoldAdapter.showRetry(true, fetchErrorMessage(t));
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
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        mOrderSoldAdapter.clear();
        mOrderSoldAdapter.isEmpty();
        mOSoldPaginationScrollListener.resetState();
        mOrderSoldAdapter.notifyDataSetChanged();
        orderSold_byUserFirstPage();
    }


    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mOrderSoldAdapter.clear();
        mOrderSoldAdapter.isEmpty();
        mOSoldPaginationScrollListener.resetState();
        mOrderSoldAdapter.notifyDataSetChanged();
        orderSold_byUserFirstPage();
    }
}
