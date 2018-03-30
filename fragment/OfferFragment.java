package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Offer;
import com.app.noan.model.OfferPriceMinMax;
import com.app.noan.model.OfferProduct;
import com.app.noan.model.OrderLastSold;
import com.app.noan.model.Product;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanjay on 20/9/17.
 */

@SuppressLint("ValidFragment")
public class OfferFragment extends Fragment {

    // Offer sizeList
    public RecyclerView rv_OfferSizeList;
    public List<SizeModel> msizeModelList;
    public OfferSizeAdapter mSizeAdapter;
    int product_id;
    ApiProduct mApiProduct;
    Dialog pDialog;
    SharedPreferences prefs;
    Product mProduct;

    public OfferFragment(int product_id, Product mProductObj) {
        this.product_id = product_id;
        mProduct = mProductObj;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_offer, container, false);

        initialization(view);


        setOfferSizeListinRecycleView();

        recycleViewClick();

        return view;

    }

    private void initialization(View view) {
        rv_OfferSizeList = (RecyclerView) view.findViewById(R.id.rv_offerList);
        msizeModelList = new ArrayList<>();
        mApiProduct = ApiClient.getClient().create(ApiProduct.class);
        prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
    }

    private void recycleViewClick() {

        rv_OfferSizeList.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_OfferSizeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                offerPriceMinMaxPriceByServer(String.valueOf(product_id), msizeModelList.get(position).getSizeId(), msizeModelList.get(position).getSizeValue());

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void setOfferSizeListinRecycleView() {
        try {
            msizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSizeAdapter = new OfferSizeAdapter(msizeModelList);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv_OfferSizeList.setLayoutManager(horizontalLayoutManagaer);
        rv_OfferSizeList.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rv_OfferSizeList.setAdapter(mSizeAdapter);
    }


    private class OfferSizeAdapter extends RecyclerView.Adapter<OfferSizeAdapter.MyViewHolder> {
        List<SizeModel> sizeModelList;

        public OfferSizeAdapter(List<SizeModel> msizeModelList) {
            sizeModelList = msizeModelList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtOfferSize;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.txtOfferSize = (TextView) itemView.findViewById(R.id.txt_offerSize);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.laayout_offer_size, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder(view);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            SizeModel sizeModel = sizeModelList.get(position);
            holder.txtOfferSize.setText(sizeModel.getSizeValue());
            holder.txtOfferSize.setTypeface(null, Typeface.BOLD);
        }


        @Override
        public int getItemCount() {
            return sizeModelList.size();
        }
    }


    private void offerPriceMinMaxPriceByServer(final String productId, final String sizeId, final String sizeValue) {
        pDialog = new Dialog(getActivity());
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();

        Map<String, String> credMap = new HashMap<>();
        credMap.put("product_id", String.valueOf(productId));
        credMap.put("size_id", String.valueOf(sizeId));
        Call<Offer> minMaxCall = mApiProduct.offerPriceDetails(credMap);
        minMaxCall.enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(Call<Offer> call, Response<Offer> response) {
                hideDialog();
                if (response.isSuccessful()) {
                    Offer mOffer = response.body();
                    if (mOffer.getStatus() == 1) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        OfferDialogFragment mOfferDialogFragment = new OfferDialogFragment(productId, sizeId, sizeValue, mOffer);
                        mOfferDialogFragment.show(fm, "Offer Fragment");
                    } else {
                        OfferPriceMinMax mOfferPrice = new OfferPriceMinMax();
                        mOfferPrice.setLowOfferPrice("");
                        mOfferPrice.setSeller_min_price("");
                        mOfferPrice.setTopOfferPrice("");
                        OfferProduct product = new OfferProduct();
                        product.setId(mProduct.getProduct_id());
                        product.setProductName(mProduct.getProduct_name());
                        product.setImage(mProduct.getProduct_image());
                        product.setSku(mProduct.getProduct_skuno());
                        product.setDescription(mProduct.getProduct_description());
                        product.setSize(sizeId);
                        mOfferPrice.setProductData(product);
                        mOffer.setData(mOfferPrice);

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        OfferDialogFragment mOfferDialogFragment = new OfferDialogFragment(productId, sizeId, sizeValue, mOffer);
                        mOfferDialogFragment.show(fm, "Offer Fragment");
                    }

                }
            }

            @Override
            public void onFailure(Call<Offer> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(getActivity(), "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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


}
