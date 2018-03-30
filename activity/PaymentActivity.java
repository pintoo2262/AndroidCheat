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
import com.app.noan.fragment.AccountFragment;
import com.app.noan.fragment.PaypalFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PaymentActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private TabLayout mTabLayout;
    private ViewPager mViewpager;

    private String[] PAGE_TITLES;
    public PaypalFragment mPaypalFragment;
    public AccountFragment mAccountFragment;

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]{
            new PaypalFragment(),
            new AccountFragment()
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
        setContentView(R.layout.activity_payment);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

    }

    private void initialization() {
        PAGE_TITLES = getResources().getStringArray(R.array.paymenttab);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_payment);
        mViewpager = (ViewPager) findViewById(R.id.viewpager_payment);
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
        mtoolbarTitle.setText(getResources().getString(R.string.payment_methode));
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
            switch (position) {
                case 0:
                    mPaypalFragment = new PaypalFragment();
                    mPaypalFragment.setArguments(PaymentActivity.this);
                    return mPaypalFragment;
                case 1:
                    mAccountFragment = new AccountFragment();
                    mAccountFragment.setArguments(PaymentActivity.this);
                    return mAccountFragment;
                default:
                    mPaypalFragment = new PaypalFragment();
                    mPaypalFragment.setArguments(PaymentActivity.this);
                    return mPaypalFragment;
            }
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

}
