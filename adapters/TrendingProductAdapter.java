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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.TrendingFragment;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.Product;
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

public class TrendingProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public List<Product> trendingProductList;
    List<Product> productSearchList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    private GridLayoutManager mManager;

    public List<Product> getTrendingProductList() {
        return trendingProductList;
    }

    public void setTrendingProductList(List<Product> trendingProductList) {
        this.trendingProductList = trendingProductList;
    }


    public TrendingProductAdapter(FragmentActivity activity, TrendingFragment buynowFragment, RecyclerView rv_Trending) {
        this.context = activity;
        this.mCallback = (PaginationAdapterCallback) buynowFragment;
        this.trendingProductList = new ArrayList<>();
        productSearchList = new ArrayList<>();

        this.mManager = (GridLayoutManager) rv_Trending.getLayoutManager();
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

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.layout_rv_sandal, parent, false);
                viewHolder = new TrendingProductAdapter.ItemViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new TrendingProductAdapter.LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Product product = trendingProductList.get(position);
        switch (getItemViewType(position)) {
            case ITEM:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;


                itemViewHolder.txtperoff.setText("");
                itemViewHolder.txtdescription.setText(product.getProduct_name());


                String mStock = product.getProduct_finalStock();
                if (mStock.equals("0")) {
                    itemViewHolder.iv_offerIcon.setVisibility(View.VISIBLE);
                    itemViewHolder.txtprice.setVisibility(View.INVISIBLE);
                } else {
                    itemViewHolder.iv_offerIcon.setVisibility(View.INVISIBLE);
                    itemViewHolder.txtprice.setVisibility(View.VISIBLE);
                    itemViewHolder.txtprice.setText("$ " + product.getProduct_finalprice());
                }


                // load movie thumbnail
                loadImage(product.getProduct_image())
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
                        .into(itemViewHolder.ivProductImage);
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
        return trendingProductList == null ? 0 : trendingProductList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == trendingProductList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

   /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Product r) {
        trendingProductList.add(r);
        notifyItemInserted(trendingProductList.size() - 1);
    }

    public void addAll(List<Product> moveResults) {
        for (Product result : moveResults) {
            add(result);
        }
    }

    public void remove(Product r) {
        int position = trendingProductList.indexOf(r);
        if (position > -1) {
            trendingProductList.remove(position);
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
        add(new Product());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        if (trendingProductList.size() > 0) {
            int position = trendingProductList.size() - 1;
            Product result = getItem(position);

            if (result != null) {
                trendingProductList.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public Product getItem(int position) {
        return trendingProductList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(trendingProductList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage, iv_offerIcon;
        TextView txtprice, txtperoff, txtdescription, txtStockDescrption;
        ProgressBar mProgress;
        FrameLayout frameLayout;

        public ItemViewHolder(View view) {
            super(view);
            ivProductImage = (ImageView) view.findViewById(R.id.iv_sandal);
            iv_offerIcon = (ImageView) itemView.findViewById(R.id.iv_offerIcon1);
            txtprice = (TextView) view.findViewById(R.id.txt_price);
            txtperoff = (TextView) view.findViewById(R.id.txt_peroff);
            txtdescription = (TextView) view.findViewById(R.id.txt_description);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
            frameLayout = itemView.findViewById(R.id.tranparent);
            txtStockDescrption = itemView.findViewById(R.id.txtStockdescrption);

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

