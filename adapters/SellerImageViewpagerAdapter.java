package com.app.noan.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.noan.R;
import com.app.noan.model.CommonImage;
import com.tiagosantos.enchantedviewpager.EnchantedViewPager;

import java.util.List;

/**
 * Created by smn on 16/9/17.
 */

public class SellerImageViewpagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    List<CommonImage> imageList;

    public SellerImageViewpagerAdapter(FragmentActivity activity, List<CommonImage> imageList) {
        mContext = activity;
        this.imageList = imageList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_collection1, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_viewpager_image);
        imageView.setImageResource(imageList.get(position).getImage());
        imageView.setTag(EnchantedViewPager.ENCHANTED_VIEWPAGER_POSITION + position);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

}