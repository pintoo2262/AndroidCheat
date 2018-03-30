package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.adapters.SearchProductAdapter;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.MetaData;
import com.app.noan.model.Product;
import com.app.noan.model.ProductResponse;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchSaleActivity extends AppCompatActivity implements View.OnClickListener, PaginationAdapterCallback, SearchView.OnQueryTextListener {

    private static final String TAG = "SearchSalesFragment";
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;


    SearchView mSearchView;
    private ImageView ivHelp, closeButton;
    private Dialog dialog;

    private RecyclerView rvSearchResult;
    private PaginationScrollListener mPaginationScrollListener;
    LinearLayoutManager layoutManager;
    public SearchProductAdapter msearchProductAdapter;


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
    APISearch service;
    String text;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sale);

        toolBarAndDrawerInitilization();

        initilitzation();


        service = ApiClient.getClient().create(APISearch.class);

        msearchProductAdapter = new SearchProductAdapter(SearchSaleActivity.this, rvSearchResult, "seachForSell");
        rvSearchResult.setAdapter(msearchProductAdapter);
        progressBar.setVisibility(View.GONE);

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

        rvSearchResult.addOnScrollListener(mPaginationScrollListener);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage_SearchProduct();
            }
        });

        rvSearchResult.addOnItemTouchListener(new RecyclerTouchListener(this, rvSearchResult, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent intent = new Intent(SearchSaleActivity.this, SearchForSellActivity.class);
                intent.putExtra("searchProduct", msearchProductAdapter.productModelList.get(position));
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
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.black));
        mtoolbarTitle.setText(getResources().getString(R.string.search));
    }

    private void initilitzation() {
        rvSearchResult = (RecyclerView) findViewById(R.id.rv_product_search_for_sale);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        layoutManager = new GridLayoutManager(this, 2);
        rvSearchResult.setLayoutManager(layoutManager);
        rvSearchResult.addItemDecoration(new SpacesItemDecoration(6, 2));

        //sandle initlize
        mSearchView = (SearchView) findViewById(R.id.search_sales);
        closeButton = (ImageView) mSearchView.findViewById(R.id.search_close_btn);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getResources().getString(R.string.searchbyname));


        ivHelp = (ImageView) findViewById(R.id.iv_help_for_search);
        ivHelp.setOnClickListener(this);
        closeButton.setOnClickListener(this);


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
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.search_close_btn:
                mSearchView.setQuery("", false);
                msearchProductAdapter.clear();
                msearchProductAdapter.isEmpty();
                msearchProductAdapter.productModelList.clear();
                msearchProductAdapter.notifyDataSetChanged();
                hideErrorView();

                break;

            case R.id.iv_help_for_search:
                dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_help_dialog);
                dialog.setCancelable(false);

                TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
                tvCancel.setOnClickListener(this);

                Button btnOk = dialog.findViewById(R.id.btn_ok);
                btnOk.setOnClickListener(this);

                dialog.show();
                break;

            case R.id.tv_cancel:
                dialog.dismiss();
                break;

            case R.id.btn_ok:
                dialog.dismiss();
                break;
        }
    }

    private void loadFirstPage_SearchProduct() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();

        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("sku", text);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("type", "0");
        credMap.put("size_id", "0");
        credMap.put("category_id", "0");
        credMap.put("brand_id", "0");
        credMap.put("color", "0");


        Call<ProductResponse> productResponseCall = service.searchProductBySKU(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        metaDatal = productResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        List<Product> results = fetchResults(response);
                        progressBar.setVisibility(View.GONE);
                        msearchProductAdapter.clear();
                        msearchProductAdapter.isEmpty();
                        msearchProductAdapter.clearData();
                        msearchProductAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            msearchProductAdapter.addLoadingFooter();
                        else
                            isLastPage = true;

                    } else {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
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
        credMap.put("sku", text);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("type", "0");
        credMap.put("size_id", "0");
        credMap.put("category_id", "0");
        credMap.put("brand_id", "0");
        credMap.put("color", "0");
        Call<ProductResponse> productResponseCall = service.searchProductBySKU(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        msearchProductAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        msearchProductAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            msearchProductAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                msearchProductAdapter.showRetry(true, fetchErrorMessage(t));
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        text = newText;
        if (text.length() > 0) {
            progressBar.setVisibility(View.VISIBLE);
            currentPage = 1;
            msearchProductAdapter.clear();
            msearchProductAdapter.isEmpty();
            mPaginationScrollListener.resetState();
            msearchProductAdapter.productModelList.clear();
            loadFirstPage_SearchProduct();
            hideErrorView();
        } else {
            progressBar.setVisibility(View.GONE);
            msearchProductAdapter.productModelList.clear();
            msearchProductAdapter.notifyDataSetChanged();
            mPaginationScrollListener.resetState();
        }
        return true;
    }
}
