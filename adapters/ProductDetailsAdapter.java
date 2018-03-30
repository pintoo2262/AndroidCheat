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

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ItemViewHolder> {

    Activity context;
    List<Product> productImageList;
    int typeRecyclView;


    public ProductDetailsAdapter(ProductDetailsActivity productDetailsActivity, List<Product> productsImageList, int typeRecycleview) {
        context = productDetailsActivity;
        productImageList = productsImageList;
        typeRecyclView = typeRecycleview;
    }


    @Override
    public ProductDetailsAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (typeRecyclView == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv0_moresandals, null);
        } else if (typeRecyclView == 1) {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv1_moresandals, null);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.layout_rv2_moresandals, null);
        }

        ProductDetailsAdapter.ItemViewHolder itemViewHolder = new ProductDetailsAdapter.ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductDetailsAdapter.ItemViewHolder holder, final int position) {


        Glide.with(context).load(productImageList.get(position).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return productImageList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;

        public ItemViewHolder(View view) {
            super(view);
            productImage = (ImageView) view.findViewById(R.id.iv_moreImage);

        }
    }
}
