package com.app.noan.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.app.noan.fragment.OrderPurchaseFragment;
import com.app.noan.fragment.OrderSoldFragment;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.NeedToConfirmModel;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by smn on 22/11/17.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public List<NeedToConfirmModel> needToConfirmModelList;
    public List<NeedToConfirmModel> mFilterList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;


    public OrderAdapter(FragmentActivity activity, OrderPurchaseFragment fragment) {
        this.context = activity;
        this.mCallback = (PaginationAdapterCallback) fragment;
        this.needToConfirmModelList = new ArrayList<>();
        mFilterList = new ArrayList<>();
    }

    public OrderAdapter(FragmentActivity activity, OrderSoldFragment fragment) {
        this.context = activity;
        this.mCallback = (PaginationAdapterCallback) fragment;
        this.needToConfirmModelList = new ArrayList<>();
        mFilterList = new ArrayList<>();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.layout_active_sale_item, parent, false);
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

                itemViewHolder.tvProudctSKU.setText("SKU " + needToConfirmModel.getProductSku());
                itemViewHolder.tvProductTitle.setText(needToConfirmModel.getProductName());
                itemViewHolder.tvProductPrice.setText("$ " + needToConfirmModel.getTotal());

                String status = needToConfirmModel.getStatus();
                if (status.equals("pending")) {
                    itemViewHolder.tvApproval.setText("Approval Pending");
                } else if (status.equals("cancel_by_admin")) {
                    itemViewHolder.tvApproval.setText("Cancel");
                } else if (status.equals("cancel_by_user")) {
                    itemViewHolder.tvApproval.setText("Cancel");
                } else if (status.equals("cancel_by_seller")) {
                    itemViewHolder.tvApproval.setText("Cancel");
                } else if (status.equals("confirmed")) {
                    itemViewHolder.tvApproval.setText("Ordered and Approved");
                } else {
                    itemViewHolder.tvApproval.setText(status);
                }

                if (needToConfirmModel.getTransactionId().equals("")) {
                    itemViewHolder.ivOfferIcon.setVisibility(View.INVISIBLE);
                } else {
                    itemViewHolder.ivOfferIcon.setVisibility(View.VISIBLE);
                }
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

    public void add(NeedToConfirmModel r) {
        needToConfirmModelList.add(r);
        notifyItemInserted(needToConfirmModelList.size() - 1);
    }

    public void filteradd(NeedToConfirmModel r) {
        mFilterList.add(r);
        notifyItemInserted(mFilterList.size() - 1);
    }


    public void addAll(List<NeedToConfirmModel> moveResults) {
        for (NeedToConfirmModel result : moveResults) {
            add(result);
            filteradd(result);

        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        needToConfirmModelList.clear();
        if (charText.length() == 0) {
            needToConfirmModelList.addAll(mFilterList);
        } else {
            for (NeedToConfirmModel needToConfirmModel : mFilterList) {
                if (needToConfirmModel.getProductSku().toLowerCase(Locale.getDefault())
                        .contains(charText) || needToConfirmModel.getProductName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    needToConfirmModelList.add(needToConfirmModel);
                }
            }
        }
        notifyDataSetChanged();
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

    public void clearData() {
        needToConfirmModelList.removeAll(needToConfirmModelList);
        notifyDataSetChanged();
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

        if (needToConfirmModelList.size() > 0) {
            int position = needToConfirmModelList.size() - 1;
            NeedToConfirmModel result = getItem(position);
            if (result != null) {
                needToConfirmModelList.remove(position);
                notifyItemRemoved(position);
            }
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
        TextView tvProudctSKU, tvProductTitle, tvProductPrice, tvApproval;
        ImageView ivOfferIcon;


        public ItemViewHolder(View view) {
            super(view);
            ivProductImage = itemView.findViewById(R.id.iv_product_sale);
            tvProudctSKU = itemView.findViewById(R.id.tv_product_sku);
            tvProductTitle = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvApproval = itemView.findViewById(R.id.tv_product_approval);
            ivOfferIcon = itemView.findViewById(R.id.iv_offerIcon);

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
                ;
    }


}
