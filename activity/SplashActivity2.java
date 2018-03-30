package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.fragment.FragmentFour;
import com.app.noan.fragment.FragmentOne;
import com.app.noan.fragment.FragmentThree;
import com.app.noan.fragment.FragmentTwo;
import com.app.noan.utils.MyUtility;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity2 extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private Button btnRegistratiom, btnLogin;
    private MyUtility mMyUtility;

    ViewPager mViewPager;
    MyAdapter mAdapter;
    static int NUMBER_OF_PAGES = 4;

    private LinearLayout ll_dots;
    private TextView[] dots;
    int page_position = 0;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        // initilzation of xml componet
        initialization();

        if (Build.VERSION.SDK_INT < 23) {

        } else {
            if (mMyUtility.checkSelfPermission()) {

            }
        }

    }

    private void initialization() {
        // Object Creationg
        mMyUtility = new MyUtility(SplashActivity2.this);
        mAdapter = new MyAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.page);
        mViewPager.setAdapter(mAdapter);
        ll_dots = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegistratiom = (Button) findViewById(R.id.btnRegistration);


        btnLogin.setOnClickListener(this);
        btnRegistratiom.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        addBottomDots(page_position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Intent intent = new Intent(SplashActivity2.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegistration:
                Intent intent1 = new Intent(SplashActivity2.this, RegistrationStep1Activity.class);
                startActivity(intent1);
                break;
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[NUMBER_OF_PAGES];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(30);
            dots[i].setPadding(4, 4, 4, 4);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#CBD2D4"));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        addBottomDots(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    return new FragmentOne(SplashActivity2.this);
                case 1:
                    // return a different Fragment class here
                    // if you want want a completely different layout
                    return FragmentTwo.newInstance();
                case 2:
                    // return a different Fragment class here
                    // if you want want a completely different layout
                    return FragmentThree.newInstance();
                case 3:
                    // return a different Fragment class here
                    // if you want want a completely different layout
                    return FragmentFour.newInstance();

                default:
                    return null;
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
