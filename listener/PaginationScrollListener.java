package com.app.noan.listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Pagination
 * Created by Suleiman19 on 10/15/16.
 * Copyright (c) 2016. Suleiman Ali Shakir. All rights reserved.
 */
public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    LinearLayoutManager layoutManager;
    int PAGE_SIZE;

    private int previousTotalItemCount = 0;
    private boolean loading = true;
    private boolean isLastPage = true;


    /**
     * Supporting only LinearLayoutManager for now.
     *
     * @param layoutManager
     * @param TOTAL_PAGES
     */
    public PaginationScrollListener(LinearLayoutManager layoutManager, int TOTAL_PAGES) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = TOTAL_PAGES;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

     /*   int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= getTotalPageCount()) {
                loadMoreItems();
            }
        }*/

        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        if (loading && totalItemCount > previousTotalItemCount) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }
        if (!loading && lastVisibleItemPosition == (totalItemCount - 1) && totalItemCount >= PAGE_SIZE) {
            loading = true;
            loadMoreItems();
        }

    }

    public void resetState() {
        this.previousTotalItemCount = 0;
        this.loading = true;
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();


}
