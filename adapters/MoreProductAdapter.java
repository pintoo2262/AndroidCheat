package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.noan.R;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.model.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by smn on 15/9/17.
 */

public class MoreProductAdapter extends RecyclerView.Adapter<MoreProductAdapter.ItemViewHolder> {

    Activity context;
    public List<Product> moreProductList;
    int typeRecyclView;

    public MoreProductAdapter(ProductDetailsActivity productDetailsActivity, List<Product> moreProductimageList, int typeRecycleview) {
        context = productDetailsActivity;
        moreProductList = moreProductimageList;
        typeRecyclView = typeRecycleview;
    }


    @Override
    public MoreProductAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (typeRecyclView == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv0_moresandals, null);
        } else if (typeRecyclView == 1) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv1_moresandals, null);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv2_moresandals, null);
        }

        MoreProductAdapter.ItemViewHolder itemViewHolder = new MoreProductAdapter.ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(MoreProductAdapter.ItemViewHolder holder, final int position) {


        Glide.with(context).load(moreProductList.get(position).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.iv_MoreImage);

    }

    @Override
    public int getItemCount() {
        return moreProductList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_MoreImage;

        public ItemViewHolder(View view) {
            super(view);
            iv_MoreImage = (ImageView) view.findViewById(R.id.iv_moreImage);

        }
    }
}
