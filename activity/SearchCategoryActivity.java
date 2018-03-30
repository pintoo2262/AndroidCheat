package com.app.noan.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.adapters.BuyNowProductAdapter;
import com.app.noan.fragment.Dialog_SearchCategory_Filter;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchCategoryActivity extends AppCompatActivity implements View.OnClickListener, PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener {

    String Tag = SearchCategoryActivity.class.getSimpleName();
    //Toolbar
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    String id, title;

    SwipeRefreshLayout mSwipeRefreshLayoutCategory;
    private RecyclerView rv_CategoryProduct;
    private PaginationScrollListener mCategoryPaginationScrollListener;
    public BuyNowProductAdapter mCategoryProductAdapter;
    GridLayoutManager layoutManager;

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
    ApiProduct service;
    String mType = "0", mSize = "0", mBrand = "0", mColor = "0";
    //Filter Button
    Button btnfilter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchcategory);

        id = String.valueOf(getIntent().getIntExtra("categoryId", 0));
        title = getIntent().getStringExtra("categoryName");

        toolBarAndDrawerInitilization();

        initilization();

        mCategoryProductAdapter = new BuyNowProductAdapter(this, rv_CategoryProduct);
        rv_CategoryProduct.setAdapter(mCategoryProductAdapter);


        loadFirstPage_CategoryProduct();


        mCategoryPaginationScrollListener = new PaginationScrollListener(layoutManager, TOTAL_PAGES) {
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
                }, 1000);
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
        rv_CategoryProduct.addOnScrollListener(mCategoryPaginationScrollListener);


        rv_CategoryProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        rv_CategoryProduct.addOnItemTouchListener(new RecyclerTouchListener(SearchCategoryActivity.this, rv_CategoryProduct, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(SearchCategoryActivity.this, ProductDetailsActivity.class);
                intent.putExtra("product_id", mCategoryProductAdapter.productModelList.get(position).getId());
                startActivity(intent);
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(title);

    }

    private void initilization() {
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        btnRetry = (Button) findViewById(R.id.error_btn_retry);
        txtError = (TextView) findViewById(R.id.error_txt_cause);

        //sandal initlize
        rv_CategoryProduct = (RecyclerView) findViewById(R.id.rv_categoryProduct);
        mSwipeRefreshLayoutCategory = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshCategory);
        mSwipeRefreshLayoutCategory.setOnRefreshListener(this);


        layoutManager = new GridLayoutManager(this, 2);
        rv_CategoryProduct.setLayoutManager(layoutManager);
        rv_CategoryProduct.addItemDecoration(new SpacesItemDecoration(12, 2));


        //Buttton Filter
        btnfilter = (Button) findViewById(R.id.btn_filter);
        btnfilter.setOnClickListener(this);
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
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_filter:
                MyUtility.screenType = 4;
                FragmentManager fm = getSupportFragmentManager();
                Dialog_SearchCategory_Filter dialogFilter = new Dialog_SearchCategory_Filter(SearchCategoryActivity.this);
                dialogFilter.show(fm, "Dialog Fragment");
                break;
        }
    }


    private void loadFirstPage_CategoryProduct() {
        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", id);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("buy_type", "3");
        service = ApiClient.getClient().create(ApiProduct.class);
        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    mSwipeRefreshLayoutCategory.setRefreshing(false);
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        metaDatal = productResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        List<Product> results = fetchResults(response);
                        mCategoryProductAdapter.clear();
                        progressBar.setVisibility(View.GONE);
                        mCategoryProductAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mCategoryProductAdapter.addLoadingFooter();
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
        Log.d("SearchCategoryActivity", "TotalPage--" + TOTAL_PAGES);
    }


    private void loadNextPage() {

        Map<String, String> credMap = new HashMap<>();
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", id);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("buy_type", "3");

        Call<ProductResponse> productResponseCall = service.filterByServer(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        mCategoryProductAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        mCategoryProductAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mCategoryProductAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                mCategoryProductAdapter.showRetry(true, fetchErrorMessage(t));
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
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    public void triggerToNotify_rv(String sType, String sSize, String sBrand, String scolor) {
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
        if (sBrand.equals("")) {
            mBrand = "0";
        } else {
            mBrand = sBrand;
        }
        if (scolor.equals("")) {
            mColor = "0";
        } else {
            mColor = scolor;
        }

        progressBar.setVisibility(View.VISIBLE);
        mCategoryProductAdapter.clear();
        mCategoryPaginationScrollListener.resetState();
        loadFirstPage_CategoryProduct();

    }

    @Override
    public void onRefresh() {
        progressBar.setVisibility(View.VISIBLE);
        mCategoryProductAdapter.clear();
        mCategoryProductAdapter.isEmpty();
        mCategoryPaginationScrollListener.resetState();
        loadFirstPage_CategoryProduct();
    }
}
