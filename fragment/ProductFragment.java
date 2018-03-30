package com.app.noan.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.BrandAdapter;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.listener.BrandAdapterCallback;
import com.app.noan.model.BrandModel;
import com.app.noan.model.CategoryProduct;
import com.app.noan.model.ColorModel;
import com.app.noan.model.DefaultAPIResponse;
import com.app.noan.model.SizeModel;
import com.app.noan.model.TagModel;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductFragment extends Fragment implements ViewPager.OnPageChangeListener, BrandAdapterCallback {
    String TAG = ProductFragment.class.getSimpleName();

    ApiProduct apiProduct;
    // Titles of the individual pages (displayed in tabs)
    private String[] PAGE_TITLES;
    public BuynowFragment mBuynowFragment;
    public TrendingFragment mTrendingFragment;
    public ExclusiveFragment mExclusiveFragment;


    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]{
            new BuynowFragment(),
            new TrendingFragment(),
            new ExclusiveFragment()
    };

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input

    private ViewPager mViewPager;
    MyPagerAdapter mAdapterViewPager;
    SharedPreferences prefs;


    // brandimages
    private RecyclerView rv_brandImageList;
    public List<BrandModel> brandModelList;
    BrandAdapter mBrandAdapter;
    StringBuilder brandId;

    // Category
    public List<CategoryProduct> categoryProductList;

    List<String> selectedBrandList;
    DachshundTabLayout tabLayout;
    public List<SizeModel> sizeModelList;
    public List<ColorModel> colorModelList;

    public ProductFragment() {
    }

    public static Fragment newInstance() {
        return new ProductFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        prefs = getActivity().getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        // initilzation of xml componet
        initialize(view);
        setUserVisibleHint(false);


        return view;
    }


    private void initialize(View view) {
        apiProduct = ApiClient.getClient().create(ApiProduct.class);
        brandModelList = new ArrayList<>();
        selectedBrandList = new ArrayList<>();
        PAGE_TITLES = getResources().getStringArray(R.array.productTab);
        tabLayout = (DachshundTabLayout) view.findViewById(R.id.product_Tablayout);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);


        //rv  initialize
        rv_brandImageList = (RecyclerView) view.findViewById(R.id.rv_brandadvertisement);

        defaultApplicationByServer();
        //set Viewpager Data
        setViewpagerData();


    }


    private void setViewpagerData() {
        mAdapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapterViewPager);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(mViewPager);
    }


    public void setDataBrandAdvertisment(List<BrandModel> brandModelList) {
        rv_brandImageList.setHasFixedSize(true);
        mBrandAdapter = new BrandAdapter(getActivity(), ProductFragment.this, brandModelList, "isMain", 0);
        rv_brandImageList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rv_brandImageList.setAdapter(mBrandAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = mAdapterViewPager.getFragment(position);
        if (fragment != null) {
            fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void retryBrandId(List<BrandModel> brandList) {
        Boolean isSelected = false;
        brandId = new StringBuilder();

        for (int h = 0; h < brandList.size(); h++) {
            if (brandList.get(h).isSelected() == true) {
                for (int k = 0; k < MyUtility.seletedBrandList.size(); k++) {
                    if (MyUtility.seletedBrandList.get(k).equals(brandList.get(h).getId())) {
                        isSelected = true;
                        break;
                    } else {
                        isSelected = false;
                    }
                }
                if (!isSelected) {
                    MyUtility.seletedBrandList.add(mBrandAdapter.brandAdapterList.get(h).getId());
                }
                brandId.append(brandList.get(h).getId());
                //if the value is not the last element of the list
                //then append the comma(,) as well
                if (h != brandList.size() - 1) {
                    brandId.append(",");
                }
            } else {
                MyUtility.seletedBrandList.remove(mBrandAdapter.brandAdapterList.get(h).getId());
            }

        }


        if (brandId.toString().endsWith(",")) {
            brandId.deleteCharAt(brandId.length() - (brandId.length() - brandId.lastIndexOf(",")));
        }


        mBuynowFragment.triggerToNotify_rv("", "", "", brandId.toString(), "");
        mTrendingFragment.triggerToNotify_rv("", "", "", brandId.toString(), "");
        mExclusiveFragment.triggerToNotify_rv("", "", "", brandId.toString(), "");

    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        private Map<Integer, String> mFragmentTags;
        private FragmentManager mFragmentManager;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            mFragmentManager = fragmentManager;
            mFragmentTags = new HashMap<Integer, String>();
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mBuynowFragment = new BuynowFragment();
                    mBuynowFragment.setArguments(ProductFragment.this);
                    return mBuynowFragment;
                case 1:
                    mTrendingFragment = new TrendingFragment();
                    mTrendingFragment.setArguments(ProductFragment.this);
                    return mTrendingFragment;
                case 2:
                    mExclusiveFragment = new ExclusiveFragment();
                    mExclusiveFragment.setArguments(ProductFragment.this);
                    return mExclusiveFragment;
                default:
                    mBuynowFragment = new BuynowFragment();
                    mBuynowFragment.setArguments(ProductFragment.this);
                    return mBuynowFragment;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object object = super.instantiateItem(container, position);
            if (object instanceof Fragment) {
                Fragment fragment = (Fragment) object;
                String tag = fragment.getTag();
                mFragmentTags.put(position, tag);
            }
            return object;
        }

        public Fragment getFragment(int position) {
            Fragment fragment = null;
            String tag = mFragmentTags.get(position);
            if (tag != null) {
                fragment = mFragmentManager.findFragmentByTag(tag);
            }
            return fragment;
        }


    }


    public void defaultApplicationByServer() {
        Call<DefaultAPIResponse> defaultAPIResponseCall = apiProduct.getDefaultAPIByServer();
        defaultAPIResponseCall.enqueue(new Callback<DefaultAPIResponse>() {
            @Override
            public void onResponse(Call<DefaultAPIResponse> call, Response<DefaultAPIResponse> response) {
                DefaultAPIResponse defaultAPIResponse = response.body();
                if (defaultAPIResponse.getStatus() == 1) {
                    brandModelList = defaultAPIResponse.getBrandModelList();
                    setDataBrandAdvertisment(brandModelList);
                    categoryProductList = defaultAPIResponse.getCategoryProductList();
                    sizeModelList = defaultAPIResponse.getSizeModelList();
                    colorModelList = defaultAPIResponse.getColorModelList();
                    setDataInSharedpreferences(brandModelList, categoryProductList, sizeModelList, colorModelList);

                }
            }

            @Override
            public void onFailure(Call<DefaultAPIResponse> call, Throwable t) {
                call.cancel();
                if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Not connected to internet", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                } else {
                    Toast.makeText(getActivity(), "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                }

            }
        });
    }

    private void setDataInSharedpreferences(List<BrandModel> brandModelList, List<CategoryProduct> categoryProductList, List<SizeModel> sizeModelList, List<ColorModel> colorModelList) {
        SharedPreferences.Editor edit = prefs.edit();
        if (brandModelList != null) {
            try {
                edit.putString("BrandList", ObjectSerializer.serialize((Serializable) brandModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (categoryProductList != null) {
            try {
                edit.putString("CategoryList", ObjectSerializer.serialize((Serializable) categoryProductList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (sizeModelList != null) {
            try {
                edit.putString("SizeList", ObjectSerializer.serialize((Serializable) sizeModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (colorModelList != null) {
            try {
                edit.putString("ColorList", ObjectSerializer.serialize((Serializable) colorModelList));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        edit.commit();


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            // fragment is visible and have created
        }

    }

}
