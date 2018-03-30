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
import android.widget.Toast;

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

public class OrderPurchaseFragment extends Fragment implements PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "OrderPurchaseFragment";

    private RecyclerView rv_OrderPurchase;
    private SwipeRefreshLayout mSwipeRefreshLayoutPurchaseList;
    private PaginationScrollListener mOPaginationScrollListener;
    private OrderAdapter mOrderAdapter;
    public EditText edt_Search_PurchaseOrder;


    LinearLayoutManager linearLayoutManager;


    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage;
    private String searchText = "";


    MetaData metaDatal;
    APIRequestSell service;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_purchased, container, false);

        userId = MyUtility.getSavedPreferences(getActivity(), "id");
        service = ApiClient.getClient().create(APIRequestSell.class);

        initilitzation(view);


        mOrderAdapter = new OrderAdapter(getActivity(), this);
        rv_OrderPurchase.setAdapter(mOrderAdapter);


        mOPaginationScrollListener = new PaginationScrollListener(linearLayoutManager, TOTAL_PAGES) {
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
        rv_OrderPurchase.addOnScrollListener(mOPaginationScrollListener);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage_OrderPurchase();
            }
        });


        rv_OrderPurchase.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_OrderPurchase, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("NeedConfirmModel", (Serializable) mOrderAdapter.needToConfirmModelList.get(position));
                startActivity(intent);

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

        rv_OrderPurchase = (RecyclerView) view.findViewById(R.id.rv_purchase);
        mSwipeRefreshLayoutPurchaseList = view.findViewById(R.id.swipeRefreshPurchasedList);
        edt_Search_PurchaseOrder = view.findViewById(R.id.et_search_product_purchase);
        mSwipeRefreshLayoutPurchaseList.setOnRefreshListener(this);

        // userId


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_OrderPurchase.setLayoutManager(linearLayoutManager);
        rv_OrderPurchase.setHasFixedSize(true);


        edt_Search_PurchaseOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText = edt_Search_PurchaseOrder.getText().toString().toLowerCase(Locale.getDefault());


//                mOrderAdapter.filter(search);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                progressBar.setVisibility(View.VISIBLE);
                mOrderAdapter.clear();
                mOrderAdapter.isEmpty();
                mOPaginationScrollListener.resetState();
                mOrderAdapter.notifyDataSetChanged();
                loadFirstPage_OrderPurchase();
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        mOrderAdapter.clear();
        mOrderAdapter.isEmpty();
        mOPaginationScrollListener.resetState();
        mOrderAdapter.notifyDataSetChanged();
        loadFirstPage_OrderPurchase();
    }


    private void loadFirstPage_OrderPurchase() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("search_text", searchText);


        Call<NeedToConfirmResponse> userOrderPurchase = service.userOrderPurchase(credMap);
        userOrderPurchase.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    NeedToConfirmResponse needToConfirmResponse = ((NeedToConfirmResponse) response.body());
                    mSwipeRefreshLayoutPurchaseList.setRefreshing(false);
                    if (needToConfirmResponse.getStatus() == 1) {
                        metaDatal = needToConfirmResponse.getMetadata();
                        getTotalPageDynamic(metaDatal);
                        progressBar.setVisibility(View.GONE);
                        List<NeedToConfirmModel> results = fetchResults(response);
                        mOrderAdapter.clear();
                        mOrderAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES & TOTAL_PAGES != currentPage)
                            mOrderAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    } else {
                        progressBar.setVisibility(View.GONE);
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
        credMap.put("user_id", userId);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("search_text", searchText);


        Call<NeedToConfirmResponse> userOrderPurchase = service.userOrderPurchase(credMap);
        userOrderPurchase.enqueue(new Callback<NeedToConfirmResponse>() {
            @Override
            public void onResponse(Call<NeedToConfirmResponse> call, Response<NeedToConfirmResponse> response) {
                if (response.isSuccessful()) {
                    NeedToConfirmResponse needToConfirmResponse = ((NeedToConfirmResponse) response.body());
                    if (needToConfirmResponse.getStatus() == 1) {
                        mOrderAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<NeedToConfirmModel> results = response.body().getNeedToConfirmModelList();
                        mOrderAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mOrderAdapter.addLoadingFooter();
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
            public void onFailure(Call<NeedToConfirmResponse> call, Throwable t) {
                t.printStackTrace();
                mOrderAdapter.showRetry(true, fetchErrorMessage(t));
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
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mOrderAdapter.clear();
        mOrderAdapter.isEmpty();
        mOPaginationScrollListener.resetState();
        mOrderAdapter.notifyDataSetChanged();
        loadFirstPage_OrderPurchase();
    }
}
