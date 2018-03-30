package com.app.noan.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
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
import com.app.noan.activity.LargeView;
import com.app.noan.adapters.CollectionInstagramTagAdapter;
import com.app.noan.adapters.CollectionViewpagerAdapter;
import com.app.noan.helper.EndlessParentScrollListener;
import com.app.noan.helper.SpacesItemDecoration;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Collection;
import com.app.noan.model.CollectionResponse;
import com.app.noan.model.TagModel;
import com.app.noan.model.instagram.Edge__;
import com.app.noan.model.instagram.InstagramResponse;
import com.app.noan.retrofit_api.APICollection;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.IMediaService;
import com.app.noan.retrofit_api.ServiceGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CollectionFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PaginationAdapterCallback {
    String Tag = CollectionFragment.class.getSimpleName();
    // Viewpager
    private ViewPager viewPagerCollection;
    private List<Collection> mCollectionList;
    private CollectionViewpagerAdapter viewpagerAdapter;


    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private Handler mHandler;
    private Timer mTimerTask;
    private Runnable mRunnable;


    // Recycleview
    private RecyclerView rvInstagramPhoto;
    SwipeRefreshLayout mSwipeRefreshLayout;
    GridLayoutManager gridLayoutManager;

    private NestedScrollView mNestedScrollView;
    EndlessParentScrollListener mEndlessParentScrollListener;

    // instagram photos
    IMediaService iMediaService;
    CollectionInstagramTagAdapter mTagAdapter;


    // Image
    APICollection mApiCollection;
    private List<String> tempProductImageList;
    boolean mUserVisibleHint = true;


    TextView txtownerCollection;


    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0, currentPage1 = 0;
    List<TagModel> tagModelList;


    ProgressBar progressBar, mRvprogressbar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    public static Fragment newInstance() {
        return new CollectionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        initilitzation(view);

        mHandler = new Handler();
        mTimerTask = new Timer();

        collectionListByServer();

        rvInstagramPhoto.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvInstagramPhoto, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mTagAdapter.mCollectionTagPhotoList.get(position).getNode() != null) {
                    Intent intent = new Intent(getActivity(), LargeView.class);
                    intent.putExtra("tempImageList", (Serializable) tempProductImageList);
                    int pos1 = position;
                    intent.putExtra("index", pos1);
                    startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        mEndlessParentScrollListener = new EndlessParentScrollListener(gridLayoutManager, TOTAL_PAGES) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage1 += 1;

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
        mNestedScrollView.setOnScrollChangeListener(mEndlessParentScrollListener);


        return view;
    }


    private void initilitzation(View view) {
        mApiCollection = ApiClient.getClient().create(APICollection.class);
        iMediaService = ServiceGenerator.createService(IMediaService.class);
        mCollectionList = new ArrayList<>();
        tagModelList = new ArrayList<>();


        viewPagerCollection = view.findViewById(R.id.viewpager_collections);
        rvInstagramPhoto = view.findViewById(R.id.rv_ownerCollectionList);
        mSwipeRefreshLayout = view.findViewById(R.id.refreshLayout);
        mNestedScrollView = view.findViewById(R.id.nestedScrollview);


        mRvprogressbar = view.findViewById(R.id.main_recycleviewProgress);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        rvInstagramPhoto.setHasFixedSize(false);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvInstagramPhoto.setLayoutManager(gridLayoutManager);
        rvInstagramPhoto.addItemDecoration(new SpacesItemDecoration(12, 2));
        txtownerCollection = view.findViewById(R.id.txtownerCollection);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rvInstagramPhoto.setNestedScrollingEnabled(false);

        mTagAdapter = new CollectionInstagramTagAdapter(getActivity(), CollectionFragment.this, rvInstagramPhoto);
        rvInstagramPhoto.setAdapter(mTagAdapter);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRvprogressbar.setVisibility(View.VISIBLE);
                mTagAdapter.clear();
                mTagAdapter.clearData();
                mEndlessParentScrollListener.resetState();
                collectionListByServer();
            }
        });

    }


    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        super.onDestroyView();
        if (mTimerTask != null) {
            mTimerTask.cancel();
            Log.d("Tag", "timer&handler");
        }
        Log.d(Tag, "onDestroyView");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUserVisibleHint) {
            setUserVisibleHint(mUserVisibleHint);
        }
        Log.d(Tag, "onStart");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(Tag, "stop");
        if (mUserVisibleHint) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (mHandler != null) {
            mTimerTask.cancel();
            Log.e("Tag", "timer&handler");
        }
        Log.d(Tag, "onDestroy");
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) { // fragment is visible and have created

            setOnRefershWebCall();
            Log.d(Tag, "Refersh");

        }
    }

    void setOnRefershWebCall() {
//        mRvprogressbar.setVisibility(View.VISIBLE);
//        mTagAdapter.clear();
//        mTagAdapter.clearData();
//        mEndlessParentScrollListener.resetState();
//        collectionListByServer();
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            mRvprogressbar.setVisibility(View.VISIBLE);
            txtownerCollection.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorView(Throwable throwable) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            mRvprogressbar.setVisibility(View.GONE);
            txtownerCollection.setVisibility(View.GONE);
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

    private void collectionListByServer() {
        hideErrorView();
        Call<CollectionResponse> call = mApiCollection.collectionListAPI();
        call.enqueue(new Callback<CollectionResponse>() {
            @Override
            public void onResponse(Call<CollectionResponse> call, Response<CollectionResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    txtownerCollection.setVisibility(View.VISIBLE);
                    CollectionResponse collectionResponse = ((CollectionResponse) response.body());
                    tagModelList = collectionResponse.getCollectionTagModelList();
                    if (collectionResponse.getStatus() == 1) {
                        mCollectionList.clear();
                        mCollectionList = collectionResponse.getData();
                        viewpagerAdapter = new CollectionViewpagerAdapter(getActivity(), mCollectionList);
                        viewPagerCollection.setAdapter(viewpagerAdapter);

                        NUM_PAGES = mCollectionList.size();

                        // Auto start of viewpager

                        mRunnable = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                viewPagerCollection.setCurrentItem(currentPage++, true);
                            }
                        };

                        mTimerTask.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mHandler.post(mRunnable);
                            }
                        }, 4000, 4000);

                        // Pager listener over indicator
                        viewPagerCollection.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                            @Override
                            public void onPageSelected(int position) {
                                currentPage = position;

                            }

                            @Override
                            public void onPageScrolled(int pos, float arg1, int arg2) {

                            }

                            @Override
                            public void onPageScrollStateChanged(int pos) {

                            }
                        });
                    }

                    currentPage1 = 0;
                    TOTAL_PAGES = tagModelList.size();

                    collectionFetchPhotosByTag();
                }
            }

            @Override
            public void onFailure(Call<CollectionResponse> call, Throwable t) {
                // Log error here since request failed
                call.cancel();
                showErrorView(t);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void collectionFetchPhotosByTag() {
        Call<InstagramResponse> call = iMediaService.getMediaByTag("noannew");
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {
                mRvprogressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    tempProductImageList = new ArrayList<>();
                    tempProductImageList.clear();
                    List<Edge__> results = fetchResults(response);
                    for (int k = 0; k < results.size(); k++) {
                        tempProductImageList.add(results.get(k).getNode().getDisplayUrl());
                    }
                    mTagAdapter.clearData();
                    mTagAdapter.addAll(results);
                    if (currentPage1 <= TOTAL_PAGES && TOTAL_PAGES != currentPage1)
                        mTagAdapter.addLoadingFooter();
                    else
                        isLastPage = true;
                } else {
                    Toast.makeText(getActivity(), "Couldn't refresh feed",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                mRvprogressbar.setVisibility(View.GONE);
                call.cancel();

            }
        });
    }


    private void loadNextPage() {
        String tag = tagModelList.get(currentPage1).getProduct_hasTag();
        Call<InstagramResponse> call = iMediaService.getMediaByTag(tag);
        call.enqueue(new Callback<InstagramResponse>() {
            @Override
            public void onResponse(Call<InstagramResponse> call, Response<InstagramResponse> response) {
                if (response.isSuccessful()) {
                    mTagAdapter.removeLoadingFooter();
                    isLoading = false;
                    List<Edge__> results = fetchResults(response);
                    mTagAdapter.addAll(results);
                    if (results.size() > 0) {
                        for (int k = 0; k < results.size(); k++) {
                            tempProductImageList.add(results.get(k).getNode().getDisplayUrl());
                        }
                        if (currentPage1 != TOTAL_PAGES)
                            mTagAdapter.addLoadingFooter();
                        else
                            isLastPage = true;
                    } else {
                        isLastPage = true;
                        Log.d(Tag, "isLastPage = true;" + currentPage1);
                    }
                } else {
                    currentPage1 = currentPage1 + 1;
                    loadNextPage();
                    Log.d(Tag, "isLastPage = false;" + currentPage1);
                }
            }

            @Override
            public void onFailure(Call<InstagramResponse> call, Throwable t) {
                t.printStackTrace();
                mTagAdapter.showRetry(true, fetchErrorMessage(t));
            }
        });


    }


    private List<Edge__> fetchResults(Response<InstagramResponse> response) {
        InstagramResponse topRatedMovies = response.body();
        return topRatedMovies.getGraphql().getHashtag().getEdgeHashtagToTopPosts().getEdges();
    }


    @Override
    public void onRefresh() {
        mRvprogressbar.setVisibility(View.VISIBLE);
        mTagAdapter.clear();
        mTagAdapter.clearData();
        mEndlessParentScrollListener.resetState();
        collectionListByServer();
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }
}
