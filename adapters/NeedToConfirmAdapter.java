package com.app.noan.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 22/11/17.
 */

public class NeedToConfirmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public List<NeedToConfirmModel> needToConfirmModelList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;


    public NeedToConfirmAdapter(Activity searchCategoryActivity) {
        this.context = searchCategoryActivity;
        this.needToConfirmModelList = new ArrayList<>();
        mCallback = (PaginationAdapterCallback) context;

    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.layout_need_to_comfirm_list, parent, false);
                viewHolder = new ItemViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        NeedToConfirmModel needToConfirmModel = needToConfirmModelList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                itemViewHolder.tvOrderNo.setText(context.getResources().getString(R.string.orderno) + "#" +
                        needToConfirmModel.getId());
                itemViewHolder.tvProductName.setText(needToConfirmModel.getProductName());
                String date = MyUtility.getConvertDate(needToConfirmModel.getCreated());
                itemViewHolder.tvOrderDate.setText(date);


                // load movie thumbnail
                loadImage(needToConfirmModel.getImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now

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
        return needToConfirmModelList == null ? 0 : needToConfirmModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == needToConfirmModelList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(NeedToConfirmModel confirmModel) {
        needToConfirmModelList.add(confirmModel);
        notifyItemInserted(needToConfirmModelList.size() - 1);
    }

    public void addAll(List<NeedToConfirmModel> needToConfirmModels) {
        for (NeedToConfirmModel result : needToConfirmModels) {
            add(result);
        }
    }

    public void remove(NeedToConfirmModel r) {
        int position = needToConfirmModelList.indexOf(r);
        if (position > -1) {
            needToConfirmModelList.remove(position);
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
        add(new NeedToConfirmModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = needToConfirmModelList.size() - 1;
        NeedToConfirmModel result = getItem(position);

        if (result != null) {
            needToConfirmModelList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NeedToConfirmModel getItem(int position) {
        return needToConfirmModelList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(needToConfirmModelList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvOrderNo, tvProductName, tvOrderDate;

        public ItemViewHolder(View view) {
            super(view);

            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvOrderNo = itemView.findViewById(R.id.tv_order_number);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvOrderDate = itemView.findViewById(R.id.tv_order_date_time);

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
