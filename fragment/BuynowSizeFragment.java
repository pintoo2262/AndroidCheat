package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.app.noan.R;
import com.app.noan.adapters.ExpandableListAdapter;
import com.app.noan.model.BuyNowSIzeResponse;
import com.app.noan.model.ProductSize;
import com.app.noan.model.SelllerProduct;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class BuynowSizeFragment extends Fragment {

    Dialog pDialog;
    ApiProduct apiProduct;
    List<ProductSize> productSizeList;
    List<ProductSize> tempSizeList;


    List<SelllerProduct> subSelllerProductsList;
    List<SelllerProduct> childSizeProductsList;


    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;


    int product_id;

    public BuynowSizeFragment() {
    }

    public BuynowSizeFragment(int product_id) {
        this.product_id = product_id;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buynowsize, container, false);
        apiProduct = ApiClient.getClient().create(ApiProduct.class);


        initilization(view);


        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if ((previousGroup != -1) && (groupPosition != previousGroup)) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

             /*   Intent intent = new Intent(getActivity(), ProductBuyActivity.class);
                intent.putExtra("SelllerProductList", (Serializable) childSizeProductsList.get(childPosition));
                startActivity(intent);*/
                return false;
            }
        });
        return view;
    }

    private void setExpandableAdapter(List<ProductSize> productSizeList, List<SelllerProduct> childSizeProductsList) {
        expandableListAdapter = new ExpandableListAdapter(getActivity(), productSizeList, childSizeProductsList, expandableListView);
        expandableListView.setAdapter(expandableListAdapter);
    }

    private void initilization(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        productSizeList = new ArrayList<>();
        tempSizeList = new ArrayList<>();
        subSelllerProductsList = new ArrayList<>();
        childSizeProductsList = new ArrayList<>();
    }


    private void BuyNowProductSizeByServer() {
        pDialog = new Dialog(getActivity());
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();

        Map<String, String> credMap = new HashMap<>();
        credMap.put("product_id", String.valueOf(product_id));
        Call<BuyNowSIzeResponse> productResponseCall = apiProduct.viewProductSize(credMap);
        productResponseCall.enqueue(new Callback<BuyNowSIzeResponse>() {
            @Override
            public void onResponse(Call<BuyNowSIzeResponse> call, Response<BuyNowSIzeResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    BuyNowSIzeResponse buyNowSIzeResponse = ((BuyNowSIzeResponse) response.body());
                    if (buyNowSIzeResponse.getStatus() == 1) {
                        if (buyNowSIzeResponse.getData().size() > 0 && buyNowSIzeResponse.getData() != null) {
                            productSizeList = response.body().getData();
                            for (int i = 0; i < productSizeList.size(); i++) {
                                subSelllerProductsList = productSizeList.get(i).getSellerData();
                                for (int j = 0; j < subSelllerProductsList.size(); j++) {
                                    childSizeProductsList.add(subSelllerProductsList.get(j));
                                }
                                Collections.sort(productSizeList.get(i).getSellerData());
                            }

                            setExpandableAdapter(productSizeList, childSizeProductsList);
                        }

                    } else {
//                        Toast.makeText(getActivity(), "" + buyNowSIzeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<BuyNowSIzeResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
              /*  Toast.makeText(getActivity(), "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();*/
                Log.d("onFailure", t.toString());
            }
        });
    }


    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onResume() {
        super.onResume();
        productSizeList.clear();
        childSizeProductsList.clear();
        BuyNowProductSizeByServer();
    }


}
