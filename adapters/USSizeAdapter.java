package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.SizeModel;

import java.util.List;

/**
 * Created by smn on 20/9/17.
 */

public class USSizeAdapter extends RecyclerView.Adapter<USSizeAdapter.SizeViewHolder> {

    Activity activity;
    private List<SizeModel> sizeList;
    private int selectedItem = -1;

    public USSizeAdapter(Activity activity, List<SizeModel> sizeList) {
        this.activity = activity;
        this.sizeList = sizeList;
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
    public void onBindViewHolder(final SizeViewHolder holder, final int listPosition) {
        final SizeModel model = sizeList.get(listPosition);
        holder.txt_ussize.setText(model.getSizeValue());
        holder.txt_ussize.setBackgroundResource(R.drawable.circletext_border_darkgrayborderbg);
        holder.txt_ussize.setTextColor(activity.getResources().getColor(R.color.white));

        if (selectedItem == listPosition) {
            holder.txt_ussize.setBackgroundResource(R.drawable.circletext_border_darkgrayborderbg);
            holder.txt_ussize.setTextColor(activity.getResources().getColor(R.color.white));
        } else {
            holder.txt_ussize.setBackgroundResource(R.drawable.circletext_bgwhite);
            holder.txt_ussize.setTextColor(activity.getResources().getColor(R.color.btn_bgdark));
        }
        holder.txt_ussize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(selectedItem);
                selectedItem = listPosition;
                notifyItemChanged(listPosition);
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
}
