package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.noan.R;
import com.app.noan.activity.SubmitListingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by smn on 20/9/17.
 */

public class ProductImageAdpater extends RecyclerView.Adapter<ProductImageAdpater.ProductItemHolder> {

    Activity activity;
    ArrayList<String> mList;


    public ProductImageAdpater(SubmitListingActivity activity, ArrayList<String> productImageList) {
        this.activity = activity;
        this.mList = productImageList;
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_circle_image, parent, false);
        ProductItemHolder holder = new ProductItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        Glide.with(activity).load(mList.get(position))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ProductItemHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProduct;

        public ProductItemHolder(View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.iv_ProductImage);
        }
    }
}
