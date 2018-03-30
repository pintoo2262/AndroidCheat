package com.app.noan.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.Dialog_SearchCategory_Filter;
import com.app.noan.fragment.Dialog_Search_Filter;
import com.app.noan.fragment.Dialog_filter;
import com.app.noan.model.SizeModel;
import com.app.noan.utils.MyUtility;

import java.util.List;

/**
 * Created by smn on 2/12/17.
 */

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.SizeViewHolder> {

    Context context;
    public List<SizeModel> sizeList;
    int pos;


    public SizeAdapter(FragmentActivity activity, List<SizeModel> sizeList1, int i) {
        this.context = activity;
        this.sizeList = sizeList1;
        pos = i;

    }

    @Override
    public SizeViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rv_filtersize, parent, false);
        SizeViewHolder myViewHolder = new SizeViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SizeViewHolder holder, final int position) {

        final SizeModel model = sizeList.get(position);
        holder.txt_ussize.setText(model.getSizeValue());
        holder.txt_ussize.setTextColor(model.isSelected() ? Color.BLACK : Color.WHITE);
        holder.txt_ussize.setBackgroundResource(model.isSelected() ? R.drawable.circletext_bgwhite : R.drawable.circletext_bgblack);

        if (model.isSelected()) {
            holder.txt_ussize.setTextColor(model.isSelected() ? Color.BLACK : Color.WHITE);
            holder.txt_ussize.setBackgroundResource(model.isSelected() ? R.drawable.circletext_bgwhite : R.drawable.circletext_bgblack);
            sizeList.get(position).setSelected(true);
        }
        holder.txt_ussize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                holder.txt_ussize.setTextColor(model.isSelected() ? Color.BLACK : Color.WHITE);
                holder.txt_ussize.setBackgroundResource(model.isSelected() ? R.drawable.circletext_bgwhite : R.drawable.circletext_bgblack);
                if (model.isSelected()) {
                    sizeList.get(position).setSelected(true);
                    if (pos == 1) {
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount + 1));
                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));
                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));
                    }

                } else {
                    sizeList.get(position).setSelected(false);
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

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class SizeViewHolder extends RecyclerView.ViewHolder {

        TextView txt_ussize;

        public SizeViewHolder(View itemView) {
            super(itemView);
            txt_ussize = (TextView) itemView.findViewById(R.id.txtSize);

        }
    }


    public void clearFilterData(List<SizeModel> sizeModelList, int p) {
        this.sizeList = sizeModelList;
        pos = p;
    }
}
