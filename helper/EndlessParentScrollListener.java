package com.app.noan.helper;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/**
 * Created by smn on 13/2/18.
 */

public abstract class EndlessParentScrollListener implements NestedScrollView.OnScrollChangeListener {
    // The current offset index of data you have loaded
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
    public EndlessParentScrollListener(LinearLayoutManager layoutManager, int TOTAL_PAGES) {
        this.layoutManager = layoutManager;
        this.PAGE_SIZE = TOTAL_PAGES;
    }


    @Override
    public void onScrollChange(NestedScrollView scrollView, int x, int y, int oldx, int oldy) {
        // We take the last son in the scrollview
        View view = scrollView.getChildAt(scrollView.getChildCount() - 1);
        int distanceToEnd = (view.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

        if (distanceToEnd == 0) {
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