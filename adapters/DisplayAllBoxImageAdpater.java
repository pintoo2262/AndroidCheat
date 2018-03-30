package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.noan.R;
import com.app.noan.model.Boximagedatum;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by smn on 20/9/17.
 */

public class DisplayAllBoxImageAdpater extends RecyclerView.Adapter<DisplayAllBoxImageAdpater.ProductItemHolder> {

    Activity activity;
    List<Boximagedatum> imagedatumList;


    public DisplayAllBoxImageAdpater(Activity activity, List<Boximagedatum> productImageList) {
        this.activity = activity;
        imagedatumList = productImageList;
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_circle_image, parent, false);
        ProductItemHolder holder = new ProductItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductItemHolder holder, int position) {
        Glide.with(activity).load(imagedatumList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return imagedatumList.size();
    }

    public class ProductItemHolder extends RecyclerView.ViewHolder {

        CircleImageView ivProduct;

        public ProductItemHolder(View itemView) {
            super(itemView);

            ivProduct = itemView.findViewById(R.id.iv_ProductImage);
        }
    }
}
