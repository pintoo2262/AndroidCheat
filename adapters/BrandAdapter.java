package com.app.noan.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.noan.R;
import com.app.noan.fragment.Dialog_SearchCategory_Filter;
import com.app.noan.fragment.Dialog_Search_Filter;
import com.app.noan.fragment.Dialog_filter;
import com.app.noan.fragment.ProductFragment;
import com.app.noan.model.BrandModel;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by smn on 13/9/17.
 */

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolderAdvertisment> {
    private ProductFragment mCallback;
    Context context;
    public List<BrandModel> brandAdapterList;
    String isFilter;
    int pos;
    int height, width;
    Display display;

    @Override
    public ViewHolderAdvertisment onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rv_brandadvertisment, parent, false);
        ViewHolderAdvertisment viewHolderAdvertisment = new ViewHolderAdvertisment(view);
        return viewHolderAdvertisment;
    }

    public BrandAdapter(FragmentActivity activity, ProductFragment productFragment, List<BrandModel> brandModelList, String filter, int i) {
        context = activity;
        brandAdapterList = brandModelList;
        isFilter = filter;
        this.mCallback = productFragment;
        pos = i;
        display = activity.getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }


    @Override
    public void onBindViewHolder(final ViewHolderAdvertisment holder, final int position) {
        final BrandModel brandModel = brandAdapterList.get(position);

        ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
        holder.iv_brandIcon.setColorFilter(filter);
        Glide.with(context).load(brandAdapterList.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.iv_brandIcon);
        holder.rlSquare.setBackgroundResource(brandModel.isSelected() ? R.drawable.square_brand : R.drawable.square_nobrand);

        if (brandModel.isSelected()) {
            ColorFilter filterblack = new LightingColorFilter(Color.BLACK, Color.BLACK);
            holder.iv_brandIcon.setColorFilter(filterblack);
            holder.rlSquare.setBackgroundResource(brandModel.isSelected() ? R.drawable.square_brand : R.drawable.square_nobrand);
            brandAdapterList.get(position).setSelected(true);
        }

        holder.iv_brandIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brandModel.setSelected(!brandModel.isSelected());
                holder.rlSquare.setBackgroundResource(brandModel.isSelected() ? R.drawable.square_brand : R.drawable.square_nobrand);
                if (brandModel.isSelected()) {
                    ColorFilter filterblack = new LightingColorFilter(Color.BLACK, Color.BLACK);
                    holder.iv_brandIcon.setColorFilter(filterblack);
                    brandAdapterList.get(position).setSelected(true);
                    if (pos == 0) {

                    } else if (pos == 1) {
                        mCallback.setDataBrandAdvertisment(brandAdapterList);
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount + 1));

                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));

                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));

                    }

                } else {
                    ColorFilter filterwhite = new LightingColorFilter(Color.WHITE, Color.WHITE);
                    holder.iv_brandIcon.setColorFilter(filterwhite);
                    brandAdapterList.get(position).setSelected(false);
                    // New
                    if (pos == 0) {

                    } else if (pos == 1) {
                        mCallback.setDataBrandAdvertisment(brandAdapterList);
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount - 1));

                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount - 1));

                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount - 1));
                    }


                }
                if (!isFilter.equals("filter")) {
                    mCallback.retryBrandId(brandAdapterList);
                }
            }
        });

    }

    public void clearBrandFilterData(List<BrandModel> brandAdapterList, int p, String isF) {
        this.brandAdapterList = brandAdapterList;
        pos = p;
        isFilter = isF;
    }

    @Override
    public int getItemCount() {
        return brandAdapterList.size();
    }

    public class ViewHolderAdvertisment extends RecyclerView.ViewHolder {

        ImageView iv_brandIcon;
        RelativeLayout rlSquare;


        public ViewHolderAdvertisment(View itemView) {
            super(itemView);
            iv_brandIcon = (ImageView) itemView.findViewById(R.id.iv_imageAdvertisement);
            rlSquare = (RelativeLayout) itemView.findViewById(R.id.rl_square);
        }
    }
}