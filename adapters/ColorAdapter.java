package com.app.noan.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.noan.R;
import com.app.noan.fragment.Dialog_SearchCategory_Filter;
import com.app.noan.fragment.Dialog_Search_Filter;
import com.app.noan.fragment.Dialog_filter;
import com.app.noan.model.ColorModel;
import com.app.noan.utils.MyUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smn on 2/12/17.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {

    Context context;
    public List<ColorModel> colorModelList;
    public List<String> selectColorModelList;
    int pos;


    public ColorAdapter(FragmentActivity activity, List<ColorModel> mcolorModelList, int i) {
        this.context = activity;
        colorModelList = mcolorModelList;
        selectColorModelList = new ArrayList<>();
        pos = i;
    }

    @Override
    public ColorViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_rv_color, parent, false);
        ColorViewHolder myViewHolder = new ColorViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ColorViewHolder holder, final int position) {

        final ColorModel model = colorModelList.get(position);
        ((GradientDrawable) holder.ivCircleImage.getBackground()).setColor(Color.parseColor(model.getColorCode()));
        ((GradientDrawable) holder.ivCricleBig.getBackground()).setColor(Color.parseColor(model.getColorCode()));
        if (model.isSelected()) {
            selectColorModelList.add(model.getColorCode());
            holder.ivCricleSelect.setVisibility(View.VISIBLE);
            holder.ivCricleBig.setVisibility(View.VISIBLE);
        }
        holder.ivCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                if (model.isSelected()) {
                    selectColorModelList.add(String.valueOf(model.getColorCode()));
                    holder.ivCricleSelect.setVisibility(View.VISIBLE);
                    holder.ivCricleBig.setVisibility(View.VISIBLE);

                    if (pos == 1) {
                        Dialog_filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectFilterCount = MyUtility.selectFilterCount + 1));
                    } else if (pos == 2) {
                        Dialog_Search_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));
                    } else if (pos == 3) {
                        Dialog_SearchCategory_Filter.txtFilterCounter.setText(context.getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));
                    }
                } else {
                    selectColorModelList.remove(model.getColorCode());
                    holder.ivCricleSelect.setVisibility(View.GONE);
                    holder.ivCricleBig.setVisibility(View.GONE);

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
        return colorModelList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCircleImage, ivCricleSelect, ivCricleBig;

        public ColorViewHolder(View itemView) {
            super(itemView);
            ivCircleImage = (ImageView) itemView.findViewById(R.id.circleColor);
            ivCricleSelect = itemView.findViewById(R.id.circleColorSelect);
            ivCricleBig = itemView.findViewById(R.id.circleColorBig);
        }
    }


}
