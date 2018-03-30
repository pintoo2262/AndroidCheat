package com.app.noan.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.activity.SearchCategoryActivity;
import com.app.noan.activity.SearchNotProductFoundActivity;
import com.app.noan.adapters.SearchAdapter;
import com.app.noan.adapters.SearchProductAdapter;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.PaginationScrollListener;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.CategoryProduct;
import com.app.noan.model.CategoryResponse;
import com.app.noan.model.MetaData;
import com.app.noan.model.Product;
import com.app.noan.model.ProductResponse;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment implements View.OnClickListener, SearchView.OnQueryTextListener, PaginationAdapterCallback {

    private static final String TAG = "SearchFragment";
    //Toolbar
    Toolbar toolbar;
    TextView tvTbTitle;
    AppBarLayout appbar;

    //Recyclerview Category
    RecyclerView rv_category;
    TextView txtCategoryTitle;
    private List<CategoryProduct> categoryResponseList;
    private SearchAdapter searchAdapter;


    //Search
    SearchView searchView;

    ImageView closeButton, img_about;

    //Dialog about
    Dialog dialog_about;


    // Recycleview searchBy sKu Enter text
    private PaginationScrollListener mSearchBySKuPaginationScrollListener;
    RecyclerView rv_SearchBySku;
    RelativeLayout rlNotFound;
    TextView txtSuggestLink;
    SearchProductAdapter mSearchProductAdapter;
    LinearLayoutManager layoutManager;


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
    APISearch service;
    String text;

    //Filter Button
    Button btnfilter;
    Dialog dialog;

    String mType = "0", mSize = "0", mCategory = "0", mBrand = "0", mColor = "0";

    //Intent
    Intent intent;
    String type;
    Handler mHandler;


    public SearchFragment() {

    }

    public static Fragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        setToolbarInitilization(view);

        initialization(view);

        mHandler = new Handler();
        service = ApiClient.getClient().create(APISearch.class);

        progressBar.setVisibility(View.VISIBLE);
        rv_SearchBySku.setVisibility(View.INVISIBLE);
        categoryListByServer();

        rv_category.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_category, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                IntentDataPass(categoryResponseList.get(position).getCategory_Id(), categoryResponseList.get(position).getCategory_Name());
                searchView.clearFocus();
                rv_SearchBySku.setVisibility(View.GONE);
                btnfilter.setVisibility(View.GONE);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        mSearchProductAdapter = new SearchProductAdapter(getActivity(), this, rv_SearchBySku);
        rv_SearchBySku.setAdapter(mSearchProductAdapter);
        progressBar.setVisibility(View.GONE);


        mSearchBySKuPaginationScrollListener = new PaginationScrollListener(layoutManager, TOTAL_PAGES) {
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
        rv_SearchBySku.addOnScrollListener(mSearchBySKuPaginationScrollListener);


        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryListByServer();
                loadFirstPage_SearchProduct();
            }
        });


        rv_SearchBySku.addOnScrollListener(new RecyclerView.OnScrollListener() {
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


        rv_SearchBySku.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_SearchBySku, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("product_id", mSearchProductAdapter.productModelList.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    private void IntentDataPass(String categoryid, String category_name) {
        intent = new Intent(getActivity(), SearchCategoryActivity.class);
        intent.putExtra("categoryId", Integer.parseInt(categoryid));
        intent.putExtra("categoryName", category_name);
        startActivity(intent);
    }

    private void initialization(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);


        btnfilter = (Button) view.findViewById(R.id.btn_filter);
        rv_SearchBySku = (RecyclerView) view.findViewById(R.id.rv_popularsearch);
        searchView = (SearchView) view.findViewById(R.id.search_view);
        closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        img_about = (ImageView) view.findViewById(R.id.iv_about);
        rlNotFound = view.findViewById(R.id.rlSearchNotFound);
        txtSuggestLink = view.findViewById(R.id.txtSuggest);


        // Get the search close button image view
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getResources().getString(R.string.searchbyname));
        closeButton.setOnClickListener(this);
        img_about.setOnClickListener(this);

        //Buttton Filter


        btnfilter.setVisibility(View.GONE);
        btnfilter.setOnClickListener(this);


        rv_SearchBySku.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 2);
        rv_SearchBySku.setLayoutManager(layoutManager);
        rv_SearchBySku.addItemDecoration(new SpacesItemDecoration(12, 2));


        // CategoryList
        rv_category = (RecyclerView) view.findViewById(R.id.rv_listCategory);
        txtCategoryTitle = view.findViewById(R.id.cat);
        categoryResponseList = new ArrayList<>();


    }

    private void customTextView1(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder();
        spanTxt.append(" " + getResources().getString(R.string.suggest));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(getActivity(), SearchNotProductFoundActivity.class);
                intent.putExtra("sku", text);
                startActivity(intent);
            }
        }, spanTxt.length() - getResources().getString(R.string.suggest).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
        view.setTextColor(getResources().getColor(R.color.linkColor));

    }

    private void setToolbarInitilization(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar_fragment);
        toolbar = (Toolbar) appbar.findViewById(R.id.tb_fragment);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        tvTbTitle = (TextView) toolbar.findViewById(R.id.txt_toolbar);
        tvTbTitle.setText(R.string.search);

        if (type != null) {
            if (type.equals("home")) {
                assert ((HomeActivity) getActivity()).getSupportActionBar() != null;
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            } else if (type.equals("profile")) {
                setHasOptionsMenu(true);
                assert ((HomeActivity) getActivity()).getSupportActionBar() != null;
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ((HomeActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
            } else {
                assert ((HomeActivity) getActivity()).getSupportActionBar() != null;
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

            }
        } else {
            assert ((HomeActivity) getActivity()).getSupportActionBar() != null;
            ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
            ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

    }


    private void setCategoryInList(List<CategoryProduct> categoryResponseList) {
        rv_category.setHasFixedSize(true);
        searchAdapter = new SearchAdapter(getActivity(), categoryResponseList);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv_category.setAdapter(searchAdapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_about:
                dialog = new Dialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_help_dialog);
                dialog.setCancelable(false);

                TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button btnOk = dialog.findViewById(R.id.btn_ok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;

            case R.id.btn_ok:
                dialog_about.dismiss();
                break;

            case R.id.btn_filter:
                MyUtility.screenType = 3;
                FragmentManager fm = getActivity().getSupportFragmentManager();
                Dialog_Search_Filter dialogFilter = new Dialog_Search_Filter(SearchFragment.this);
                dialogFilter.show(fm, "Dialog Fragment");

                break;
            case R.id.search_close_btn:
                searchView.setQuery("", false);
                mSearchProductAdapter.clear();
                mSearchProductAdapter.isEmpty();
                mSearchProductAdapter.notifyDataSetChanged();
                mSearchProductAdapter.productModelList.clear();
                btnfilter.setVisibility(View.GONE);
                rv_SearchBySku.setVisibility(View.GONE);
                rv_category.setVisibility(View.VISIBLE);
                txtCategoryTitle.setVisibility(view.VISIBLE);
                if (errorLayout.getVisibility() == View.VISIBLE) {
                    errorLayout.setVisibility(View.GONE);
                }
                break;
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onDestroyView() {
        super.onDestroyView();
        searchView.setQuery("", false);
        btnfilter.setVisibility(View.GONE);
        rv_SearchBySku.setVisibility(View.GONE);
        rv_category.setVisibility(View.VISIBLE);
    }

    private void categoryListByServer() {
        hideErrorView();
        Call<CategoryResponse> call = service.getAllcategory();
        call.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                hideErrorView();
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    CategoryResponse categoryResponse = response.body();
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (categoryResponse.getStatus() == 1) {
                        categoryResponseList = categoryResponse.getCategoryProductList();
                        setCategoryInList(categoryResponseList);
                    }
                }

            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();
                showErrorView(t);
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void loadFirstPage_SearchProduct() {
        // To ensure list is visible when retry button in error view is clicked
        progressBar.setVisibility(View.VISIBLE);
        hideErrorView();
        currentPage = PAGE_START;
        Map<String, String> credMap = new HashMap<>();
        credMap.put("sku", text);
        credMap.put("page", String.valueOf(currentPage));
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", mCategory);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);


        Call<ProductResponse> productResponseCall = service.searchProductBySKU(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {

                hideErrorView();
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        List<Product> results = fetchResults(response);
                        rlNotFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        metaDatal = productResponse.getMetaData();
                        getTotalPageDynamic(metaDatal);
                        mSearchProductAdapter.clear();
                        mSearchProductAdapter.isEmpty();
                        mSearchProductAdapter.clearData();
                        mSearchProductAdapter.addAll(results);
                        if (currentPage <= TOTAL_PAGES && currentPage != TOTAL_PAGES)
                            mSearchProductAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    } else {
                        rlNotFound.setVisibility(View.VISIBLE);
                        rv_SearchBySku.setVisibility(View.GONE);
                        customTextView1(txtSuggestLink);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
                progressBar.setVisibility(View.GONE);
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
        credMap.put("type", mType);
        credMap.put("size_id", mSize);
        credMap.put("category_id", mCategory);
        credMap.put("brand_id", mBrand);
        credMap.put("color", mColor);
        Call<ProductResponse> productResponseCall = service.searchProductBySKU(credMap);
        productResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    if (productResponse.getStatus() == 1) {
                        mSearchProductAdapter.removeLoadingFooter();
                        isLoading = false;
                        List<Product> results = response.body().getProductList();
                        mSearchProductAdapter.addAll(results);
                        if (currentPage != TOTAL_PAGES)
                            mSearchProductAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    }

                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                t.printStackTrace();
                mSearchProductAdapter.showRetry(true, fetchErrorMessage(t));
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        text = newText;

        if (!text.isEmpty()) {
            rv_SearchBySku.setVisibility(View.VISIBLE);
            rv_category.setVisibility(View.GONE);
            txtCategoryTitle.setVisibility(View.GONE);
            currentPage = 1;
            mSearchBySKuPaginationScrollListener.resetState();
            loadFirstPage_SearchProduct();

        } else {
            progressBar.setVisibility(View.GONE);
            btnfilter.setVisibility(View.GONE);
            mSearchProductAdapter.productModelList.clear();
            rv_SearchBySku.setVisibility(View.GONE);
            rv_category.setVisibility(View.VISIBLE);
            txtCategoryTitle.setVisibility(View.VISIBLE);
        }
        return true;
    }


    public void triggerToNotify_rv(String sType, String sSize, String sCategory, String sBrand, String sColor) {
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
            mColor = sColor;
        }
        loadFirstPage_SearchProduct();

    }

    public void setArguments(String profile) {
        type = profile;
    }
}