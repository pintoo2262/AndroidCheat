package com.app.noan.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.Productdatum;
import com.app.noan.model.SizeModel;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 22/11/17.
 */

public class ListingForSellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;


    public List<Productdatum> productdatumList;
    public List<SizeModel> sizeModelList;
    String sizeId;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    SharedPreferences prefs;
    String sizevalue, screenType, typeUser;


    public ListingForSellAdapter(Activity mActivity, String screenName, String isUserSeller) {
        context = mActivity;
        this.productdatumList = new ArrayList<>();
        mCallback = (PaginationAdapterCallback) context;
        sizeModelList = new ArrayList<>();
        screenType = screenName;
        typeUser = isUserSeller;
        prefs = context.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
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

        Productdatum product = productdatumList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

                itemViewHolder.tvProudctSKU.setText("SKU " + product.getProductSku());
                itemViewHolder.tvProductTitle.setText(product.getProductName());
                itemViewHolder.tvProductPrice.setText("$ " + product.getPrice());
                if (screenType.equals("Listing")) {

                    if (typeUser.equals("vendorseller")) {
                        itemViewHolder.tvApproval.setText("Stock : " + product.getStock());
                    } else {
                        itemViewHolder.tvApproval.setText(product.getStatus());
                    }

                } else {
                    if (!product.getComment().equals("")) {
                        itemViewHolder.tvApproval.setText(product.getComment());
                    } else {
                        itemViewHolder.tvApproval.setText(product.getStatus());
                    }
                }

                try {
                    sizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sizeId = product.getSize();
                if (sizeModelList.size() > 0) {
                    for (int i = 0; i < sizeModelList.size(); i++) {
                        if (sizeId.equals(sizeModelList.get(i).getSizeId())) {
                            sizevalue = sizeModelList.get(i).getSizeValue();
                        }
                    }
                }

                itemViewHolder.tvProductSize.setText("Size : " + sizevalue);

                // load movie thumbnail
                loadImage(product.getProductMainImage())
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
        return productdatumList == null ? 0 : productdatumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == productdatumList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

   /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Productdatum r) {
        productdatumList.add(r);
        notifyItemInserted(productdatumList.size() - 1);
    }

    public void addAll(List<Productdatum> moveResults) {
        for (Productdatum result : moveResults) {
            add(result);
        }
    }

    public void remove(Productdatum r) {
        int position = productdatumList.indexOf(r);
        if (position > -1) {
            productdatumList.remove(position);
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
        add(new Productdatum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = productdatumList.size() - 1;
        Productdatum result = getItem(position);

        if (result != null) {
            productdatumList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Productdatum getItem(int position) {
        return productdatumList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(productdatumList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProudctSKU, tvProductTitle, tvProductPrice, tvApproval, tvProductSize;

        public ItemViewHolder(View view) {
            super(view);
            ivProductImage = itemView.findViewById(R.id.iv_product_sale);
            tvProudctSKU = itemView.findViewById(R.id.tv_product_sku);
            tvProductTitle = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvApproval = itemView.findViewById(R.id.tv_product_approval);
            tvProductSize = itemView.findViewById(R.id.tv_product_size);
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
