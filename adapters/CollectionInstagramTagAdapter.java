package com.app.noan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.CollectionFragment;
import com.app.noan.model.instagram.Edge__;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 11/11/17.
 */

public class CollectionInstagramTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public List<Edge__> mCollectionTagPhotoList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private String errorMsg;
    private GridLayoutManager mManager;
    private CollectionFragment mCallback;


    public CollectionInstagramTagAdapter(FragmentActivity activity, CollectionFragment mCollectionFragment, RecyclerView rvOwenerCollectionList) {
        this.context = activity;
        this.mCallback = mCollectionFragment;
        mCollectionTagPhotoList = new ArrayList<>();
        this.mManager = (GridLayoutManager) rvOwenerCollectionList.getLayoutManager();
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return getItemViewType(position) == LOADING ? mManager.getSpanCount() : 1;
            }
        });

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem;

        switch (viewType) {
            case ITEM:
                viewItem = inflater.inflate(R.layout.collection_layout, parent, false);
                viewHolder = new ItemViewHolder(viewItem);
                break;
            case LOADING:
                viewItem = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewItem);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Edge__ edge = mCollectionTagPhotoList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;


                // load movie thumbnail
                loadImage(edge.getNode().getDisplayUrl())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                itemViewHolder.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                itemViewHolder.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .into(itemViewHolder.ivImage);
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;


        }
    }

    @Override
    public int getItemCount() {
        return mCollectionTagPhotoList == null ? 0 : mCollectionTagPhotoList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mCollectionTagPhotoList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

   /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Edge__ r) {
        mCollectionTagPhotoList.add(r);
        notifyItemInserted(mCollectionTagPhotoList.size() - 1);
    }

    public void addAll(List<Edge__> edgeList) {
        for (Edge__ result : edgeList) {
            add(result);
        }
    }

    public void remove(Edge__ r) {
        int position = mCollectionTagPhotoList.indexOf(r);
        if (position > -1) {
            mCollectionTagPhotoList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Edge__());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        if (mCollectionTagPhotoList.size() > 0) {
            int position = mCollectionTagPhotoList.size() - 1;
            mCollectionTagPhotoList.remove(position);
            notifyItemRemoved(position);

        }

    }

    public void clearData() {
        mCollectionTagPhotoList.removeAll(mCollectionTagPhotoList);
        notifyDataSetChanged();
    }


    public Edge__ getItem(int position) {
        return mCollectionTagPhotoList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mCollectionTagPhotoList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        ProgressBar mProgress;

        public ItemViewHolder(View view) {
            super(view);
            ivImage = (ImageView) view.findViewById(R.id.iv_collectiontagImage);
            mProgress = (ProgressBar) itemView.findViewById(R.id.collectionProgress);


        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }


    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .crossFade();
    }

}

