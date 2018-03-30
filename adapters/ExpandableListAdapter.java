package com.app.noan.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.ProductBuyActivity;
import com.app.noan.activity.UserSellerDetailActivity;
import com.app.noan.model.ProductSize;
import com.app.noan.model.SelllerProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by smn on 15/9/17.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ProductSize> productSizeList;
    private List<SelllerProduct> selllerProductList;
    private List<SelllerProduct> selllerProductList1;
    private ExpandableListView exp;


    public ExpandableListAdapter(FragmentActivity activity, List<ProductSize> productSizeList, List<SelllerProduct> childSizeProductsList, ExpandableListView expandableListView) {
        this.context = activity;
        this.exp = expandableListView;
        this.productSizeList = productSizeList;
        this.selllerProductList = childSizeProductsList;
        selllerProductList1 = new ArrayList<>();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<SelllerProduct> chList = productSizeList.get(groupPosition).getSellerData();
        return chList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SelllerProduct childModel = (SelllerProduct) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_title, null);
        }


        LinearLayout lltitle = convertView.findViewById(R.id.ll_title);
        LinearLayout llChildclick = convertView.findViewById(R.id.llchildclick);

        View viewtitle = convertView.findViewById(R.id.view_title);

        if (childPosition == 0) {
            lltitle.setVisibility(View.VISIBLE);
            viewtitle.setVisibility(View.VISIBLE);
        } else {
            lltitle.setVisibility(View.GONE);
            viewtitle.setVisibility(View.GONE);
        }

        TextView tvPrice = (TextView) convertView
                .findViewById(R.id.tv_price);
        TextView tvSize = (TextView) convertView
                .findViewById(R.id.tv_size);
        TextView tvSoldby = (TextView) convertView
                .findViewById(R.id.tv_soldby);


        tvPrice.setText("$ "+childModel.getPrice());
        tvSoldby.setText(childModel.getName());

        llChildclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!childModel.getSellerId().equals("0")) {
                    Intent intent = new Intent(context, ProductBuyActivity.class);
                    intent.putExtra("screenType", "buynow");
                    intent.putExtra("SelllerProductList", (Serializable) childModel);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, UserSellerDetailActivity.class);
                    intent.putExtra("SelllerProductList", (Serializable) childModel);
                    context.startActivity(intent);
                }

            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<SelllerProduct> chlist = productSizeList.get(groupPosition).getSellerData();
        Collections.sort(chlist);
        return chlist.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return productSizeList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return productSizeList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProductSize gm = (ProductSize) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        ImageView ivGroupIndicator = (ImageView) convertView.findViewById(R.id.ivGroupIndicator);

        TextView tvSize = (TextView) convertView
                .findViewById(R.id.tv_size);
        TextView tvPrice = (TextView) convertView
                .findViewById(R.id.tv_price);

        tvSize.setTypeface(null, Typeface.BOLD);
        tvSize.setText(gm.getSize());

        selllerProductList1 = gm.getSellerData();
        Collections.sort(selllerProductList1);
        for (int i = 0; i < selllerProductList1.size(); i++) {
            tvPrice.setText("$ " + selllerProductList1.get(0).getPrice());
        }


        ivGroupIndicator.setSelected(isExpanded);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
