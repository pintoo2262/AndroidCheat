package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.SearchCategoryActivity;
import com.app.noan.adapters.BrandAdapter;
import com.app.noan.adapters.ColorAdapter;
import com.app.noan.adapters.SizeAdapter;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.model.BrandModel;
import com.app.noan.model.ColorModel;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjay on 23/12/17.
 */

@SuppressLint("ValidFragment")
public class Dialog_SearchCategory_Filter extends android.support.v4.app.DialogFragment implements View.OnClickListener {


    // Dialog Filter click
    ImageView imgclose, ivSizeLine;
    TextView txt_men, txt_women, txt_youth, txt_applyfilter;
    TextView txtCategory;

    // Filter  Recylceview size list
    RecyclerView rv_size;
    private List<SizeModel> sizeList;
    public SizeAdapter sizeadapter;

    // filter Recylceview brand list
    RecyclerView rv_brand;
    private List<BrandModel> mbrandModelList;
    public BrandAdapter mBrandAdapter;

    // Filter Recycleview Category List
    RecyclerView rv_category;


    // Filter color list
    RecyclerView rv_color;
    private List<ColorModel> mcolorModelList;
    ColorAdapter mColorAdapter;

    SharedPreferences prefs;

    Dialog dialogfilter;
    public static TextView txtFilterCounter;
    APISearch service;


    // filterValue pass
    List<String> mselectedTypeList;


    public String sType, sSize, sCategory, sBrand, sColor;
    ProductFragment productFragment;
    ImageView ivFilterClear;
    SearchCategoryActivity searchCategoryActivity;


    public Dialog_SearchCategory_Filter(SearchCategoryActivity searchCategoryActivity) {
        this.searchCategoryActivity = searchCategoryActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container);

        prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);

        initilization(view);

        if (MyUtility.selectedSearchCategoryTypeList.size() > 0) {
            for (int i = 0; i < MyUtility.selectedSearchCategoryTypeList.size(); i++) {
                if (MyUtility.selectedSearchCategoryTypeList.get(i).equals("men")) {
                    txt_men.setBackgroundResource(R.drawable.broder_black1);
                    txt_men.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("men");

                } else if (MyUtility.selectedSearchCategoryTypeList.get(i).equals("women")) {
                    txt_women.setBackgroundResource(R.drawable.broder_black1);
                    txt_women.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("women");
                } else if (MyUtility.selectedSearchCategoryTypeList.get(i).equals("youth")) {
                    txt_youth.setBackgroundResource(R.drawable.broder_black1);
                    txt_youth.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("youth");
                }
            }
        }


        //set Data binding

        // Size
        rv_size = (RecyclerView) view.findViewById(R.id.rv_ussize);
        try {
            sizeList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
            if (sizeList.size() > 0) {
                for (int i = 0; i < sizeList.size(); i++) {
                    sizeList.get(i).setSelected(false);
                }
                if (MyUtility.seletedSearchCategorySizeList.size() > 0) {
                    for (int i = 0; i < sizeList.size(); i++) {
                        for (int j = 0; j < MyUtility.seletedSearchCategorySizeList.size(); j++) {
                            if (sizeList.get(i).getSizeId().equals(MyUtility.seletedSearchCategorySizeList.get(j))) {
                                sizeList.get(i).setSelected(true);
                            }
                        }
                    }
                }
                setSizeRecycleview();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // category
        rv_category = (RecyclerView) view.findViewById(R.id.rv_category);
        ivSizeLine = view.findViewById(R.id.iv_lineSize);
        txtCategory = view.findViewById(R.id.txtcategory);
        rv_category.setVisibility(View.GONE);
        ivSizeLine.setVisibility(View.GONE);
        txtCategory.setVisibility(View.GONE);


        // Brand
        rv_brand = (RecyclerView) view.findViewById(R.id.rv_brand);
        try {
            mbrandModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("BrandList", ObjectSerializer.serialize(new ArrayList())));
            if (mbrandModelList.size() > 0) {
                if (MyUtility.seletedSearchCategoryBrandList.size() > 0) {
                    for (int i = 0; i < mbrandModelList.size(); i++) {
                        for (int j = 0; j < MyUtility.seletedSearchCategoryBrandList.size(); j++) {
                            if (MyUtility.seletedSearchCategoryBrandList.get(j).equals(mbrandModelList.get(i).getId())) {
                                mbrandModelList.get(i).setSelected(true);
                            }
                        }
                    }
                }
                setbrandRecyclerview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // color  New changes
        rv_color = (RecyclerView) view.findViewById(R.id.rv_color);
        try {
            mcolorModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("ColorList", ObjectSerializer.serialize(new ArrayList())));
            if (mcolorModelList.size() > 0) {
                if (MyUtility.selectedSearchCategoryColorList.size() > 0) {
                    for (int i = 0; i < mcolorModelList.size(); i++) {
                        for (int j = 0; j < MyUtility.selectedSearchCategoryColorList.size(); j++) {
                            if (MyUtility.selectedSearchCategoryColorList.get(j).equals(mcolorModelList.get(i).getColorCode())) {
                                mcolorModelList.get(i).setSelected(true);
                            }
                        }
                    }
                }
                setColorRecycleview();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        //click filter btn dialog
        MyUtility.selectSearchFilterCategoryCount = MyUtility.selectedSearchCategoryTypeList.size() + MyUtility.seletedSearchCategorySizeList.size() + MyUtility.seletedSearchCategoryCategoryList.size() + MyUtility.seletedSearchCategoryBrandList.size() + MyUtility.selectedSearchCategoryColorList.size();
        txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount));
        txt_applyfilter = (TextView) view.findViewById(R.id.txt_applyfilter);
        txt_applyfilter.setOnClickListener(this);

        return view;
    }


    private void initilization(View view) {
        mselectedTypeList = new ArrayList<>();
        service = ApiClient.getClient().create(APISearch.class);

        imgclose = (ImageView) view.findViewById(R.id.iv_filterclose);
        imgclose.setOnClickListener(this);
        txt_men = (TextView) view.findViewById(R.id.txt_filtermen);
        txt_men.setOnClickListener(this);
        txt_women = (TextView) view.findViewById(R.id.txt_filterwomen);
        txt_women.setOnClickListener(this);
        txt_youth = (TextView) view.findViewById(R.id.txt_filteryouth);
        txt_youth.setOnClickListener(this);
        ivFilterClear = view.findViewById(R.id.iv_filterclear);
        ivFilterClear.setOnClickListener(this);

        txtFilterCounter = (TextView) view.findViewById(R.id.tv_filter_counter);
        txtFilterCounter.setText(getResources().getText(R.string.filter));


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                .TRANSPARENT));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogfilter = getDialog();
        if (dialogfilter != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialogfilter.getWindow().setLayout(width, height);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogfilter.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.txt_applyfilter:

                if (mselectedTypeList != null && mselectedTypeList.size() >= 0) {
                    MyUtility.selectedSearchCategoryTypeList = mselectedTypeList;
                    sType = setCommaSeparateType(mselectedTypeList);
                }

                // Size
                if (sizeadapter != null) {
                    Boolean isSelectedSize = false;
                    for (int h = 0; h < sizeadapter.sizeList.size(); h++) {
                        if (sizeadapter.sizeList.get(h).isSelected() == true) {
                            for (int k = 0; k < MyUtility.seletedSearchCategorySizeList.size(); k++) {
                                if (MyUtility.seletedSearchCategorySizeList.get(k).equals(sizeadapter.sizeList.get(h).getSizeId())) {
                                    isSelectedSize = true;
                                    break;
                                } else {
                                    isSelectedSize = false;
                                }
                            }
                            if (!isSelectedSize) {
                                MyUtility.seletedSearchCategorySizeList.add(sizeadapter.sizeList.get(h).getSizeId());
                            }
                        } else {
                            MyUtility.seletedSearchCategorySizeList.remove(sizeadapter.sizeList.get(h).getSizeId());
                        }

                    }
                    sSize = setCommaSeparateSize(sizeadapter.sizeList);

                }


                /// brand

                if (mBrandAdapter != null) {
                    Boolean isSelected = false;
                    for (int h = 0; h < mBrandAdapter.brandAdapterList.size(); h++) {
                        if (mBrandAdapter.brandAdapterList.get(h).isSelected() == true) {
                            for (int k = 0; k < MyUtility.seletedSearchCategoryBrandList.size(); k++) {
                                if (MyUtility.seletedSearchCategoryBrandList.get(k).equals(mBrandAdapter.brandAdapterList.get(h).getId())) {
                                    isSelected = true;
                                    break;
                                } else {
                                    isSelected = false;
                                }
                            }
                            if (!isSelected) {
                                MyUtility.seletedSearchCategoryBrandList.add(mBrandAdapter.brandAdapterList.get(h).getId());
                            }
                        } else {
                            MyUtility.seletedSearchCategoryBrandList.remove(mBrandAdapter.brandAdapterList.get(h).getId());
                        }

                    }

                    sBrand = setCommaSeparateBrand(mBrandAdapter.brandAdapterList);
                }


                if (mColorAdapter != null) {
                    if (mColorAdapter.selectColorModelList != null && mColorAdapter.selectColorModelList.size() >= 0) {
                        MyUtility.selectedSearchCategoryColorList = mColorAdapter.selectColorModelList;
                    }
                    sColor = setCommaSeparateColor(mColorAdapter.selectColorModelList);
                }

                if (sType != null || sSize != null || sCategory != null || sBrand != null || sColor != null) {
                    if (MyUtility.screenType == 4) {
                        searchCategoryActivity.triggerToNotify_rv(sType.toString(), sSize.toString(), sBrand.toString(), sColor.toString());
                    }

                }

                dialogfilter.dismiss();


                break;

            case R.id.iv_filterclear:
                MyUtility.selectSearchFilterCategoryCount = 0;

                txtFilterCounter.setText(getResources().getText(R.string.filter) + "  " + 0);
                // Type
                txt_men.setBackgroundResource(R.drawable.border_white);
                txt_men.setTextColor(getResources().getColor(R.color.white));
                mselectedTypeList.remove("men");
                txt_women.setBackgroundResource(R.drawable.border_white);
                txt_women.setTextColor(getResources().getColor(R.color.white));
                mselectedTypeList.remove("women");
                txt_youth.setBackgroundResource(R.drawable.border_white);
                txt_youth.setTextColor(getResources().getColor(R.color.white));
                mselectedTypeList.remove("youth");

                // Size

                for (int i = 0; i < sizeList.size(); i++) {
                    sizeList.get(i).setSelected(false);
                }
                sizeadapter.clearFilterData(sizeList, 3);
                sizeadapter.notifyDataSetChanged();


                /* for (int i = 0; i < sizeList.size(); i++) {
                    sizeList.get(i).setSelected(false);
                }
               rv_size.setHasFixedSize(true);
                sizeadapter = new SizeAdapter(getActivity(), sizeList, 3);
                rv_size.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rv_size.setAdapter(sizeadapter);
                sizeadapter.notifyDataSetChanged();*/

                // brand

                for (int i = 0; i < mbrandModelList.size(); i++) {
                    mbrandModelList.get(i).setSelected(false);
                }
                mBrandAdapter.clearBrandFilterData(mbrandModelList, 3, "filter");
                mBrandAdapter.notifyDataSetChanged();
//                setbrandRecyclerview();

                // color

                for (int i = 0; i < mcolorModelList.size(); i++) {
                    mcolorModelList.get(i).setSelected(false);
                }
                setColorRecycleview();
                break;


            case R.id.iv_filterclose:
                dialogfilter.dismiss();
                break;

            case R.id.txt_filtermen:
                if (txt_men.getCurrentTextColor() != getResources().getColor(R.color.black)) {
                    txt_men.setBackgroundResource(R.drawable.broder_black1);
                    txt_men.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("men");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));

                } else {
                    txt_men.setBackgroundResource(R.drawable.border_white);
                    txt_men.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("men");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount - 1));

                }
                break;

            case R.id.txt_filterwomen:
                if (txt_women.getCurrentTextColor() != getResources().getColor(R.color.black)) {
                    txt_women.setBackgroundResource(R.drawable.broder_black1);
                    txt_women.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("women");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));

                } else {
                    txt_women.setBackgroundResource(R.drawable.border_white);
                    txt_women.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("women");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount - 1));
                }

                break;

            case R.id.txt_filteryouth:
                if (txt_youth.getCurrentTextColor() != getResources().getColor(R.color.black)) {
                    txt_youth.setBackgroundResource(R.drawable.broder_black1);
                    txt_youth.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("youth");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount + 1));
                } else {
                    txt_youth.setBackgroundResource(R.drawable.border_white);
                    txt_youth.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("youth");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCategoryCount = MyUtility.selectSearchFilterCategoryCount - 1));
                }
                break;
        }
    }

    private String setCommaSeparateType(List<String> mselectedTypeList) {
        StringBuilder sType1 = new StringBuilder();

        for (int i = 0; i < mselectedTypeList.size(); i++) {
            //append the value into the builder
            sType1.append(mselectedTypeList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != mselectedTypeList.size() - 1) {
                sType1.append(",");
            }
        }
        return sType1.toString();
    }

    private String setCommaSeparateSize(List<SizeModel> msizeList) {
        StringBuilder sType2 = new StringBuilder();

        for (int i = 0; i < msizeList.size(); i++) {
            //append the value into the builder
            if (msizeList.get(i).isSelected()) {
                sType2.append(msizeList.get(i).getSizeId());

                //if the value is not the last element of the list
                //then append the comma(,) as well
                if (i != msizeList.size() - 1) {
                    sType2.append(",");
                }
            }

        }
        if (sType2.toString().endsWith(",")) {
            sType2.deleteCharAt(sType2.length() - (sType2.length() - sType2.lastIndexOf(",")));
        }
        return sType2.toString();


    }


    private String setCommaSeparateBrand(List<BrandModel> mcatgoryList) {
        StringBuilder sType4 = new StringBuilder();

        for (int i = 0; i < mcatgoryList.size(); i++) {
            //append the value into the builder
            if (mcatgoryList.get(i).isSelected()) {
                sType4.append(mcatgoryList.get(i).getId());

                //if the value is not the last element of the list
                //then append the comma(,) as well
                if (i != mcatgoryList.size() - 1) {
                    sType4.append(",");
                }
            }

        }
        if (sType4.toString().endsWith(",")) {
            sType4.deleteCharAt(sType4.length() - (sType4.length() - sType4.lastIndexOf(",")));
        }
        return sType4.toString();
    }


    private String setCommaSeparateColor(List<String> mcatgoryList) {
        StringBuilder sType4 = new StringBuilder();

        for (int i = 0; i < mcatgoryList.size(); i++) {
            //append the value into the builder
            sType4.append(mcatgoryList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != mcatgoryList.size() - 1) {
                sType4.append(",");
            }
        }
        return sType4.toString();
    }


    private void setSizeRecycleview() {
        rv_size.setHasFixedSize(true);
        sizeadapter = new SizeAdapter(getActivity(), sizeList, 3);
        rv_size.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_size.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rv_size.setAdapter(sizeadapter);
    }


    private void setbrandRecyclerview() {
        rv_brand.setHasFixedSize(true);
        mBrandAdapter = new BrandAdapter(getActivity(), productFragment, mbrandModelList, "filter", 3);
        rv_brand.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_brand.setAdapter(mBrandAdapter);
    }

    private void setColorRecycleview() {
        rv_color.setHasFixedSize(true);
        mColorAdapter = new ColorAdapter(getActivity(), mcolorModelList, 3);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        rv_color.setLayoutManager(layoutManager);
        rv_color.setAdapter(mColorAdapter);
    }

}
