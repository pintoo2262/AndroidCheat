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
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.OrderPurchaseFragment;
import com.app.noan.fragment.OrderSoldFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewpager;

    private String[] PAGE_TITLES;

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]{
            new OrderPurchaseFragment(),
            new OrderSoldFragment()

    };


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_order);


        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();


    }

    private void initialization() {
        PAGE_TITLES = getResources().getStringArray(R.array.orderTab);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_order);
        mViewpager = (ViewPager) findViewById(R.id.viewpager_order);
        mViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewpager);

    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.orders));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];

        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
