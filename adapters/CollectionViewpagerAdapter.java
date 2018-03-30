package com.app.noan.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.CollectionDetailsActivity;
import com.app.noan.model.Collection;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.List;

/**
 * Created by smn on 30/1/18.
 */

public class CollectionViewpagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<Collection> collectionList;

    public CollectionViewpagerAdapter(FragmentActivity activity, List<Collection> imageList) {
        mContext = activity;
        this.collectionList = imageList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_collection, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_viewpager_image);
        TextView txtCollectionTitle = (TextView) itemView.findViewById(R.id.txt_collectionTitle);
        TextView txtCollectionDescrption = (TextView) itemView.findViewById(R.id.txt_collectionshortDescrpriton);
        Glide.with(mContext).load(collectionList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(imageView);
        txtCollectionTitle.setText(collectionList.get(position).getName());
        txtCollectionDescrption.setText(collectionList.get(position).getShortDescription());
        imageView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(mContext, CollectionDetailsActivity.class);
                intent.putExtra("collection_id", collectionList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }


}
