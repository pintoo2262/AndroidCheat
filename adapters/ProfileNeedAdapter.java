package com.app.noan.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.SizeSelectionActiviy;
import com.app.noan.fragment.ProfileNeedFragment;
import com.app.noan.listener.PaginationAdapterCallback;
import com.app.noan.model.Product;
import com.app.noan.model.ProductResponse;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by smn on 19/9/17.
 */

public class ProfileNeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Product> productList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    Dialog pDialog;
    int finalStock;

    public ProfileNeedAdapter(Activity activity, ProfileNeedFragment profileWantFragment) {
        context = activity;
        this.mCallback = (PaginationAdapterCallback) profileWantFragment;
        productList = new ArrayList<>();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.layout_rv_profilewant, parent, false);
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
        final Product result = productList.get(position); // Movie

        switch (getItemViewType(position)) {

            case ITEM:
                final ItemViewHolder viewHolder = (ItemViewHolder) holder;

                viewHolder.sandalname.setText("SZ " + result.getProduct_sizevalue() + " / " + result.getProduct_name());
                viewHolder.sandalcode.setText(result.getProduct_skuno());
                viewHolder.topoffer.setText("$" + result.getProduct_topOfferPrice());
                viewHolder.lowprice.setText("$" + result.getProduct_lowprice());
                viewHolder.lastsale.setText("Last SALE $" + result.getProduct_lastSaleprice());
                viewHolder.iv_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        custome_deleteDialog(productList.get(position).getNpId(), position);
                    }
                });


                viewHolder.card_offer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!MyUtility.getSavedPreferences(context, "id").equals("")) {
                            Intent intent = new Intent(context, SizeSelectionActiviy.class);
                            intent.putExtra("product_id", result.getProduct_id());
                            intent.putExtra("ProductObjct", result);
                            finalStock = Integer.parseInt(result.getProduct_finalStock());
                            if (finalStock > 0) {
                                intent.putExtra("istypeOrder", "regularOrder");
                            } else {
                                intent.putExtra("istypeOrder", "offer");
                            }


                            context.startActivity(intent);
                        } else {
                           /* FragmentManager fm = getSupportFragmentManager();
                            LoginDialogFragment dialogFragment = new LoginDialogFragment();
                            dialogFragment.show(fm, "Dialog Fragment");*/
                        }
                    }
                });


                // load movie thumbnail
                loadImage(result.getProduct_image())
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
                        .into(viewHolder.sandalimage);
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
        return productList == null ? 0 : productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == productList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .crossFade();
    }



     /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Product r) {
        productList.add(r);
        notifyItemInserted(productList.size() - 1);
    }

    public void addAll(List<Product> productList1) {
        for (Product result : productList1) {
            add(result);
        }
    }

    public void remove(Product r) {
        int position = productList.indexOf(r);
        if (position > -1) {
            productList.remove(position);
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

        int position = productList.size() - 1;
        Product result = getItem(position);

        if (result != null) {
            productList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Product getItem(int position) {
        return productList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(productList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        CardView card_offer;
        ImageView sandalimage, iv_menu, iv_saleicon;
        TextView sandalname, sandalcode, topoffer, lowprice, lastsale, txtBuynow;


        public ItemViewHolder(View view) {
            super(view);
            sandalimage = (ImageView) view.findViewById(R.id.iv_imagesandal);
            iv_menu = (ImageView) view.findViewById(R.id.iv_offermenu);
            iv_saleicon = (ImageView) view.findViewById(R.id.iv_saleicon);
            card_offer = (CardView) view.findViewById(R.id.card_offer);
            sandalname = (TextView) view.findViewById(R.id.txt_name);
            sandalcode = (TextView) view.findViewById(R.id.txt_code);
            topoffer = (TextView) view.findViewById(R.id.txt_topoffer);
            lowprice = (TextView) view.findViewById(R.id.txt_lowprice);
            lastsale = (TextView) view.findViewById(R.id.txt_lastsale);
            card_offer = (CardView) view.findViewById(R.id.card_offer);


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

    private void deleteNeedProduct(String npId, final int position) {
        pDialog = new Dialog(context);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("np_id", npId);
        ApiProduct service = ApiClient.getClient().create(ApiProduct.class);
        Call<ProductResponse> detailResponseCall = service.deleteneedProductList(credMap);
        detailResponseCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    ProductResponse productResponse = ((ProductResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (productResponse.getStatus() == 1) {
                        remove(productList.get(position));
                        notifyDataSetChanged();
//                        Toast.makeText(context, "" + productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(context, "" + productResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(context, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    public void custome_deleteDialog(final String npId, final int position) {
        final TextView dialogMessage, dialogTitle;
        final Button btnYes, btnNo;
        final Dialog deleteDialog = new Dialog(context);
        deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        deleteDialog.setContentView(R.layout.custom_dialog_delete_address);
        deleteDialog.setCanceledOnTouchOutside(true);
        deleteDialog.setCancelable(false);
        dialogTitle = deleteDialog.findViewById(R.id.txtTitle);
        dialogMessage = deleteDialog.findViewById(R.id.txtMessage);
        dialogTitle.setText(R.string.are_you_product);
        dialogTitle.setAllCaps(true);
        dialogMessage.setText(R.string.msg_product);
        btnNo = deleteDialog.findViewById(R.id.btnNo);
        btnNo.setText(R.string.buycancel);
        btnNo.setAllCaps(true);
        btnYes = deleteDialog.findViewById(R.id.btnYes);
        btnYes.setAllCaps(true);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNeedProduct(npId, position);
                deleteDialog.dismiss();

            }
        });
        deleteDialog.show();
    }


}
