package com.app.noan.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.Dialog_SearchCategory_Filter;
import com.app.noan.fragment.Dialog_Search_Filter;
import com.app.noan.fragment.Dialog_filter;
import com.app.noan.model.CategoryProduct;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 15/9/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {

    Activity context;
    public List<CategoryProduct> categoryModelList;
    public List<String> selectedCategorylist;
    Display display;
    Point size;
    int height, width;
    int pos;

    public CategoryAdapter(FragmentActivity activity, List<CategoryProduct> categoryResponseList, int i) {
        this.context = activity;
        this.categoryModelList = categoryResponseList;
        selectedCategorylist = new ArrayList<>();
        display = activity.getWindowManager().getDefaultDisplay();
        size = new Point();
        width = display.getWidth();
        height = display.getHeight();
        pos = i;

    }


    @Override
    public CategoryAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_rv_category, null);
        CategoryAdapter.ItemViewHolder itemViewHolder = new CategoryAdapter.ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ItemViewHolder holder, final int position) {

        final CategoryProduct mCategoryProduct = categoryModelList.get(position);

        holder.txt_category.setText(mCategoryProduct.getCategory_Name());
        holder.txt_category.setTextColor(mCategoryProduct.isSelected() ? Color.WHITE : Color.GRAY);
        holder.ll_category.setBackgroundResource(mCategoryProduct.isSelected() ? R.drawable.circletext_bgblack : R.drawable.non_selected_category);

        Glide.with(context).load(mCategoryProduct.getCategory_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(holder.iv_category);
        if (mCategoryProduct.isSelected()) {
            selectedCategorylist.add(categoryModelList.get(position).getCategory_Id());
            holder.txt_category.setTextColor(mCategoryProduct.isSelected() ? Color.WHITE : Color.GRAY);
            holder.ll_category.setBackgroundResource(mCategoryProduct.isSelected() ? R.drawable.circletext_bgblack : R.drawable.non_selected_category);
            categoryModelList.get(position).setSelected(true);
        }


        holder.ll_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryProduct.setSelected(!mCategoryProduct.isSelected());
                holder.txt_category.setTextColor(mCategoryProduct.isSelected() ? Color.WHITE : Color.GRAY);
                holder.ll_category.setBackgroundResource(mCategoryProduct.isSelected() ? R.drawable.circletext_bgblack : R.drawable.non_selected_category);
                if (mCategoryProduct.isSelected()) {
                    categoryModelList.get(position).setSelected(true);
                    selectedCategorylist.add(categoryModelList.get(position).getCategory_Id());
                    if (pos == 1) {
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount + 1));
                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));
                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));
                    }

                } else {
                    categoryModelList.get(position).setSelected(false);
                    selectedCategorylist.remove(categoryModelList.get(position).getCategory_Id());
                    if (pos == 1) {
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount - 1));
                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount - 1));
                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount - 1));
                    }
                }
            }
        });
    }

    public void clearCategoryFilterData(List<CategoryProduct> categoryModelLists, int p) {
        this.categoryModelList = categoryModelLists;
        pos = p;
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relative;
        LinearLayout ll_category;
        ImageView iv_category;
        TextView txt_category;

        public ItemViewHolder(View view) {
            super(view);

            relative = (RelativeLayout) view.findViewById(R.id.relative);
            ll_category = view.findViewById(R.id.ll_circle);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (width / 4), ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 0, 0);
            relative.setLayoutParams(params);

            iv_category = (ImageView) view.findViewById(R.id.iv_category);
            txt_category = (TextView) view.findViewById(R.id.txt_category);
        }
    }


}