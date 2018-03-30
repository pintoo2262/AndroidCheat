package com.app.noan.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.SellMenu;

import java.util.List;

/**
 * Created by smn on 18/9/17.
 */

public class SellMenuAdapter extends RecyclerView.Adapter<SellMenuAdapter.ItemViewHolder>{

    Activity activity;
    List<SellMenu> mList;

    public SellMenuAdapter(Activity activity, List<SellMenu> list) {
        this.activity = activity;
        this.mList = list;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.layout_sell_menu, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(v);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        SellMenu sellMenu = mList.get(position);
        holder.tvSellItem.setText(sellMenu.getSellMenuTitle());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        LinearLayout llMenuItem;
        TextView tvSellItem;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tvSellItem = (TextView) itemView.findViewById(R.id.tv_sell_menu_item);

        }

    }
}
