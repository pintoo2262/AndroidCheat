package com.app.noan.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.CategoryProduct;

import java.util.List;

/**
 * Created by smn on 16/9/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.HeaderViewHolder> {

    Context context;
    private List<CategoryProduct> categoryModelList;

    public SearchAdapter(FragmentActivity activity, List<CategoryProduct> categoryResponseList) {
        context = activity;
        categoryModelList = categoryResponseList;
    }


    @Override
    public HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_search_description, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeaderViewHolder holder, int position) {
        CategoryProduct categoryModel = categoryModelList.get(position);
        holder.mTitle.setText(categoryModel.getCategory_Name());
    }


    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.txt_search_description);
        }
    }


}
