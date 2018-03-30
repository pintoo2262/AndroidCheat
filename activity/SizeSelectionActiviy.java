package com.app.noan.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.BuynowSizeFragment;
import com.app.noan.fragment.OfferFragment;
import com.app.noan.helper.CustomViewPager;
import com.app.noan.model.Product;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SizeSelectionActiviy extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mToolbarTitle;

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private ImageView tabOne, tabTwo;
    private int product_Id;
    String typeOrder;

    private Product mProductObj;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Transparent);
        setContentView(R.layout.activity_size_selection);

        product_Id = Integer.parseInt(getIntent().getStringExtra("product_id"));
        mProductObj = (Product) getIntent().getSerializableExtra("ProductObjct");
        typeOrder = getIntent().getStringExtra("istypeOrder");

        // Toolbar setup
        ToolbarSetup();

        // Initilization
        initilization();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    setupTabIcons();
                } else {
                    setupTabIcons1();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        if (typeOrder.equals("regularOrder")) {
            viewPager.setCurrentItem(0);
        } else {


            LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            }
//            viewPager.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
            viewPager.disableScroll(true);
            viewPager.setCurrentItem(1);



        }


    }

    private void initilization() {
        viewPager = (CustomViewPager) findViewById(R.id.vp_Sizer);
        viewPager.setOffscreenPageLimit(2);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tl_buynow);
        tabLayout.setupWithViewPager(viewPager);

        tabOne = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        tabOne.setImageResource(R.drawable.buy_now_select);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo.setImageResource(R.drawable.offer_normal);
        tabLayout.getTabAt(1).setCustomView(tabTwo);


    }

    private void ToolbarSetup() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar_black);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarTitle = mToolbar.findViewById(R.id.txt_toolbar_black);
        String type = mProductObj.getProduct_type();

        mToolbarTitle.setText(type + "  s i z e s");
        mToolbar.setBackgroundColor(getResources().getColor(R.color.toolbarmensize));
    }

    private void setupTabIcons() {
        tabOne.setImageResource(R.drawable.buy_now_select);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabTwo.setImageResource(R.drawable.offer_normal);
        tabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setupTabIcons1() {
        tabOne.setImageResource(R.drawable.buy_now_normal);
        tabLayout.getTabAt(0).setCustomView(tabOne);
        tabTwo.setImageResource(R.drawable.offer_select);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BuynowSizeFragment(product_Id), "Buy now");
        adapter.addFrag(new OfferFragment(product_Id, mProductObj), "Offer");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                this.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }


}
