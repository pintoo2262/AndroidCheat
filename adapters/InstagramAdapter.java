package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.noan.R;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.model.instagram.Edge;
import com.app.noan.model.instagram.Edge_;
import com.app.noan.model.instagram.Edge__;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by smn on 15/9/17.
 */

public class InstagramAdapter extends RecyclerView.Adapter<InstagramAdapter.ItemViewHolder> {

    Activity context;
    List<Edge__> edgeList;



    public InstagramAdapter(ProductDetailsActivity productDetailsActivity, List<Edge__> mInstagramTagPhotoList) {
        context = productDetailsActivity;
        edgeList = mInstagramTagPhotoList;


    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.layout_rv1_moresandals, null);
        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {


        Glide.clear(holder.sandal_moreImage);
        Glide.with(context)
                .load(edgeList.get(position).getNode().getDisplayUrl())
                .placeholder(R.drawable.noimage)
                .dontAnimate()
//                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(holder.sandal_moreImage);


    }

    @Override
    public int getItemCount() {
        return edgeList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView sandal_moreImage;

        public ItemViewHolder(View view) {
            super(view);
            sandal_moreImage = (ImageView) view.findViewById(R.id.iv_moreImage);

        }
    }
}
