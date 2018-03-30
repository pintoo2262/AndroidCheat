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
import com.app.noan.adapters.BrandAdapter;
import com.app.noan.adapters.CategoryAdapter;
import com.app.noan.adapters.ColorAdapter;
import com.app.noan.adapters.SizeAdapter;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.model.BrandModel;
import com.app.noan.model.CategoryProduct;
import com.app.noan.model.ColorModel;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanjay on 22/12/17.
 */

@SuppressLint("ValidFragment")
public class Dialog_Search_Filter extends android.support.v4.app.DialogFragment implements View.OnClickListener {


    // Dialog Filter click
    ImageView imgclose;
    TextView txt_men, txt_women, txt_youth, txt_applyfilter;


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
    private List<CategoryProduct> categoryResponseList;
    public CategoryAdapter mCategoryAdapter;


    // Filter color list
    RecyclerView rv_color;
    private List<ColorModel> mcolorModelList;
    ColorAdapter mColorAdapter;

    Dialog dialogfilter;
    public static TextView txtFilterCounter;
    APISearch service;

    // filterValue pass
    List<String> mselectedTypeList;

    public String sType, sSize, sCategory, sBrand, sColor;
    ProductFragment productFragment;
    ImageView ivFilterClear;
    SearchFragment searchFragment;

    SharedPreferences prefs;

    public Dialog_Search_Filter(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_filter, container);
        prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        initilization(view);

        if (MyUtility.selectedSearchTypeList.size() > 0) {
            for (int i = 0; i < MyUtility.selectedSearchTypeList.size(); i++) {
                if (MyUtility.selectedSearchTypeList.get(i).equals("men")) {
                    txt_men.setBackgroundResource(R.drawable.broder_black1);
                    txt_men.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("men");

                } else if (MyUtility.selectedSearchTypeList.get(i).equals("women")) {
                    txt_women.setBackgroundResource(R.drawable.broder_black1);
                    txt_women.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("women");
                } else if (MyUtility.selectedSearchTypeList.get(i).equals("youth")) {
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
                if (MyUtility.seletedSearchSizeList.size() > 0) {
                    for (int i = 0; i < sizeList.size(); i++) {
                        for (int j = 0; j < MyUtility.seletedSearchSizeList.size(); j++) {
                            if (sizeList.get(i).getSizeId().equals(MyUtility.seletedSearchSizeList.get(j))) {
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
        try {
            categoryResponseList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("CategoryList", ObjectSerializer.serialize(new ArrayList())));
            if (categoryResponseList.size() > 0) {
                for (int i = 0; i < categoryResponseList.size(); i++) {
                    categoryResponseList.get(i).setSelected(false);
                }
                if (MyUtility.seletedSearchCategoryList.size() > 0) {
                    for (int i = 0; i < categoryResponseList.size(); i++) {
                        for (int j = 0; j < MyUtility.seletedSearchCategoryList.size(); j++) {
                            if (MyUtility.seletedSearchCategoryList.get(j).equals(categoryResponseList.get(i).getCategory_Id())) {
                                categoryResponseList.get(i).setSelected(true);
                            }
                        }
                    }
                }
                setcategoryRecyclerview();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Brand
        rv_brand = (RecyclerView) view.findViewById(R.id.rv_brand);
        try {
            mbrandModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("BrandList", ObjectSerializer.serialize(new ArrayList())));
            if (mbrandModelList.size() > 0) {
                if (MyUtility.seletedSearchBrandList.size() > 0) {
                    for (int i = 0; i < mbrandModelList.size(); i++) {
                        for (int j = 0; j < MyUtility.seletedSearchBrandList.size(); j++) {
                            if (MyUtility.seletedSearchBrandList.get(j).equals(mbrandModelList.get(i).getId())) {
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
                if (MyUtility.selectedSearchColorList.size() > 0) {
                    for (int i = 0; i < mcolorModelList.size(); i++) {
                        for (int j = 0; j < MyUtility.selectedSearchColorList.size(); j++) {
                            if (MyUtility.selectedSearchColorList.get(j).equals(mcolorModelList.get(i).getColorCode())) {
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


        MyUtility.selectSearchFilterCount = MyUtility.selectedSearchTypeList.size() + MyUtility.seletedSearchSizeList.size() + MyUtility.seletedSearchCategoryList.size() + MyUtility.seletedSearchBrandList.size() + MyUtility.selectedSearchColorList.size();

        txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + MyUtility.selectSearchFilterCount);
        //click filter btn dialog

        txt_applyfilter = (TextView) view.findViewById(R.id.txt_applyfilter);
        txt_applyfilter.setOnClickListener(this);


        return view;
    }

    private void setColorRecycleview() {
        rv_color.setHasFixedSize(true);
        mColorAdapter = new ColorAdapter(getActivity(), mcolorModelList, 2);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        rv_color.setLayoutManager(layoutManager);
        rv_color.setAdapter(mColorAdapter);
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
                    MyUtility.selectedSearchTypeList = mselectedTypeList;
                    sType = setCommaSeparateType(mselectedTypeList);
                }


                // Size
                if (sizeadapter != null) {
                    Boolean isSelectedSize = false;
                    for (int h = 0; h < sizeadapter.sizeList.size(); h++) {
                        if (sizeadapter.sizeList.get(h).isSelected() == true) {
                            for (int k = 0; k < MyUtility.seletedSearchSizeList.size(); k++) {
                                if (MyUtility.seletedSearchSizeList.get(k).equals(sizeadapter.sizeList.get(h).getSizeId())) {
                                    isSelectedSize = true;
                                    break;
                                } else {
                                    isSelectedSize = false;
                                }
                            }
                            if (!isSelectedSize) {
                                MyUtility.seletedSearchSizeList.add(sizeadapter.sizeList.get(h).getSizeId());
                            }
                        } else {
                            MyUtility.seletedSearchSizeList.remove(sizeadapter.sizeList.get(h).getSizeId());
                        }

                    }
                    sSize = setCommaSeparateSize(sizeadapter.sizeList);

                   /* if (sizeadapter.selecteSizeList != null && sizeadapter.selecteSizeList.size() >= 0) {
                        MyUtility.seletedSizeList = sizeadapter.selecteSizeList;
                    }
                    */

                }


                /// brand

                if (mBrandAdapter != null) {
                    Boolean isSelected = false;
                    for (int h = 0; h < mBrandAdapter.brandAdapterList.size(); h++) {
                        if (mBrandAdapter.brandAdapterList.get(h).isSelected() == true) {
                            for (int k = 0; k < MyUtility.seletedSearchBrandList.size(); k++) {
                                if (MyUtility.seletedSearchBrandList.get(k).equals(mBrandAdapter.brandAdapterList.get(h).getId())) {
                                    isSelected = true;
                                    break;
                                } else {
                                    isSelected = false;
                                }
                            }
                            if (!isSelected) {
                                MyUtility.seletedSearchBrandList.add(mBrandAdapter.brandAdapterList.get(h).getId());
                            }
                        } else {
                            MyUtility.seletedSearchBrandList.remove(mBrandAdapter.brandAdapterList.get(h).getId());
                        }

                    }
                    sBrand = setCommaSeparateBrand(mBrandAdapter.brandAdapterList);
                }


                if (mColorAdapter != null) {
                    if (mColorAdapter.selectColorModelList != null && mColorAdapter.selectColorModelList.size() >= 0) {
                        MyUtility.selectedSearchColorList = mColorAdapter.selectColorModelList;
                    }
                    sColor = setCommaSeparateColor(mColorAdapter.selectColorModelList);

                }
                if (mCategoryAdapter != null) {
                    if (mCategoryAdapter.selectedCategorylist != null && mCategoryAdapter.selectedCategorylist.size() >= 0) {
                        MyUtility.seletedSearchCategoryList = mCategoryAdapter.selectedCategorylist;
                    }
                    sCategory = setCommaSeparatecategory(mCategoryAdapter.selectedCategorylist);
                }


                if (sType != null || sSize != null || sCategory != null || sBrand != null || sColor != null) {
                    if (MyUtility.screenType == 3) {
                        searchFragment.triggerToNotify_rv(sType.toString(), sSize.toString(), sCategory.toString(), sBrand.toString(), sColor.toString());
                    }
                }

                dialogfilter.dismiss();


                break;

            case R.id.iv_filterclear:

                MyUtility.selectSearchFilterCount = 0;
                txtFilterCounter.setText(getResources().getText(R.string.filter) + "  " + 0);
                // type

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
//                rv_size.setHasFixedSize(true);
//                sizeadapter = new SizeAdapter(getActivity(), sizeList, 2);
//                rv_size.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//                rv_size.setAdapter(sizeadapter);
//                sizeadapter.notifyDataSetChanged();

                sizeadapter.clearFilterData(sizeList, 2);
                sizeadapter.notifyDataSetChanged();


                // Category

                for (int i = 0; i < categoryResponseList.size(); i++) {
                    categoryResponseList.get(i).setSelected(false);
                }
                rv_category.setHasFixedSize(true);
                mCategoryAdapter = new CategoryAdapter(getActivity(), categoryResponseList, 2);
                rv_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rv_category.setAdapter(mCategoryAdapter);
                mCategoryAdapter.notifyDataSetChanged();

                // Brand
                for (int i = 0; i < mbrandModelList.size(); i++) {
                    mbrandModelList.get(i).setSelected(false);
                }
                mBrandAdapter.clearBrandFilterData(mbrandModelList, 3, "filter");
                mBrandAdapter.notifyDataSetChanged();
                setbrandRecyclerview();

                // Color

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
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));
                } else {
                    txt_men.setBackgroundResource(R.drawable.border_white);
                    txt_men.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("men");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount - 1));

                }
                break;

            case R.id.txt_filterwomen:
                if (txt_women.getCurrentTextColor() != getResources().getColor(R.color.black)) {
                    txt_women.setBackgroundResource(R.drawable.broder_black1);
                    txt_women.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("women");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));


                } else {
                    txt_women.setBackgroundResource(R.drawable.border_white);
                    txt_women.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("women");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount - 1));

                }

                break;

            case R.id.txt_filteryouth:
                if (txt_youth.getCurrentTextColor() != getResources().getColor(R.color.black)) {
                    txt_youth.setBackgroundResource(R.drawable.broder_black1);
                    txt_youth.setTextColor(getResources().getColor(R.color.black));
                    mselectedTypeList.add("youth");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount + 1));

                } else {
                    txt_youth.setBackgroundResource(R.drawable.border_white);
                    txt_youth.setTextColor(getResources().getColor(R.color.white));
                    mselectedTypeList.remove("youth");
                    txtFilterCounter.setText(getResources().getText(R.string.filter) + " " + String.valueOf(MyUtility.selectSearchFilterCount = MyUtility.selectSearchFilterCount - 1));

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


    private String setCommaSeparatecategory(List<String> mcatgoryList) {
        StringBuilder sType3 = new StringBuilder();

        for (int i = 0; i < mcatgoryList.size(); i++) {
            //append the value into the builder
            sType3.append(mcatgoryList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != mcatgoryList.size() - 1) {
                sType3.append(",");
            }
        }
        return sType3.toString();
    }


    private String setCommaSeparateColor(List<String> mColoList) {
        StringBuilder sType4 = new StringBuilder();

        for (int i = 0; i < mColoList.size(); i++) {
            //append the value into the builder
            sType4.append(mColoList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != mColoList.size() - 1) {
                sType4.append(",");
            }
        }
        return sType4.toString();
    }


    private void setSizeRecycleview() {
        rv_size.setHasFixedSize(true);
        sizeadapter = new SizeAdapter(getActivity(), sizeList, 2);
        rv_size.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_size.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rv_size.setAdapter(sizeadapter);
    }

    private void setcategoryRecyclerview() {
        rv_category.setHasFixedSize(true);
        mCategoryAdapter = new CategoryAdapter(getActivity(), categoryResponseList, 2);
        rv_category.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_category.addItemDecoration(new SpacesItemDecoration_horizontal(50));
        rv_category.setAdapter(mCategoryAdapter);
    }

    private void setbrandRecyclerview() {
        rv_brand.setHasFixedSize(true);
        mBrandAdapter = new BrandAdapter(getActivity(), productFragment, mbrandModelList, "filter", 2);
        rv_brand.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_brand.setAdapter(mBrandAdapter);
    }


}

