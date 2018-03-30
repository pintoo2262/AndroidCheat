package com.app.noan.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.app.noan.R;
import com.app.noan.adapters.ViewPagerAdapter;
import com.app.noan.fragment.CollectionFragment;
import com.app.noan.fragment.ProductFragment;
import com.app.noan.fragment.ProfileFragment;
import com.app.noan.fragment.RequestToSellFragment;
import com.app.noan.fragment.SearchFragment;
import com.app.noan.helper.BottomNavigationViewHelper;
import com.app.noan.utils.MyUtility;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class HomeActivity extends AppCompatActivity {

    //bottom navigation
    BottomNavigationView bottomNavigationView;

    // viewPager
    private ViewPager viewPager;
    ViewPagerAdapter adapter;

    //Fragments
    ProductFragment productFragment;
    SearchFragment searchFragment;
    CollectionFragment exploreFragment;
    RequestToSellFragment requestToSellFragment;

    ProfileFragment profileFragment;
    MenuItem prevMenuItem;


    View iconView;
    BottomNavigationMenuView menuView;
    ViewGroup.LayoutParams layoutParams;
    DisplayMetrics displayMetrics;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        // initilzation of xml componet
        initialize();

        Log.d("shakey", MyUtility.printKeyHash(HomeActivity.this));

        //View pager change listener
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        // bottom navigation Change Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sandals:
                                MyUtility.onResumeScreenType = 0;
                                iconView = menuView.getChildAt(0).findViewById(android.support.design.R.id.icon);
                                layoutParams = iconView.getLayoutParams();
                                displayMetrics = getResources().getDisplayMetrics();
                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                                iconView.setLayoutParams(layoutParams);
                                viewPager.setCurrentItem(0);
                                final AnimatorSet animSandal = new AnimatorSet();
                                animSandal.playSequentially(ObjectAnimator.ofFloat(iconView, View.ROTATION_X, 0, -90), ObjectAnimator.ofFloat(iconView, View.ROTATION_X, -90, 0));
                                animSandal.setInterpolator(new LinearInterpolator());
                                animSandal.setDuration(200);
                                animSandal.start();

                                return true;
                            case R.id.search:
                                MyUtility.onResumeScreenType = 1;
                                iconView = menuView.getChildAt(1).findViewById(android.support.design.R.id.icon);
                                layoutParams = iconView.getLayoutParams();
                                displayMetrics = getResources().getDisplayMetrics();
                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                                iconView.setLayoutParams(layoutParams);
                                viewPager.setCurrentItem(1);
                                final AnimatorSet animSearch = new AnimatorSet();
                                animSearch.playSequentially(ObjectAnimator.ofFloat(iconView, View.ROTATION_Y, 0, -90), ObjectAnimator.ofFloat(iconView, View.ROTATION_Y, -90, 0));
                                animSearch.setInterpolator(new LinearInterpolator());
                                animSearch.setDuration(200);
                                animSearch.start();

                                return true;
                            case R.id.explore:
                                MyUtility.onResumeScreenType = 2;
                                iconView = menuView.getChildAt(2).findViewById(android.support.design.R.id.icon);
                                layoutParams = iconView.getLayoutParams();
                                displayMetrics = getResources().getDisplayMetrics();
                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                                iconView.setLayoutParams(layoutParams);
                                viewPager.setCurrentItem(2);

                                final AnimatorSet animHome = new AnimatorSet();
                                animHome.playTogether(ObjectAnimator.ofFloat(iconView, View.ROTATION, 0, 360));
                                animHome.setInterpolator(new LinearInterpolator());
                                animHome.setDuration(500);
                                animHome.start();

                                return true;
                            case R.id.sell:
                                MyUtility.onResumeScreenType = 3;
                                iconView = menuView.getChildAt(3).findViewById(android.support.design.R.id.icon);
                                layoutParams = iconView.getLayoutParams();
                                displayMetrics = getResources().getDisplayMetrics();
                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                                iconView.setLayoutParams(layoutParams);
                                viewPager.setCurrentItem(3);

                                final AnimatorSet animSell = new AnimatorSet();
                                animSell.playSequentially(ObjectAnimator.ofFloat(iconView, View.ROTATION_X, 0, -90), ObjectAnimator.ofFloat(iconView, View.ROTATION_X, -90, 0));
                                animSell.setInterpolator(new LinearInterpolator());
                                animSell.setDuration(200);
                                animSell.start();
                                return true;
                            case R.id.profile:
                                MyUtility.onResumeScreenType = 4;
                                iconView = menuView.getChildAt(4).findViewById(android.support.design.R.id.icon);
                                layoutParams = iconView.getLayoutParams();
                                displayMetrics = getResources().getDisplayMetrics();
                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
                                iconView.setLayoutParams(layoutParams);
                                viewPager.setCurrentItem(4);
                                final AnimatorSet animProfile = new AnimatorSet();
                                animProfile.playSequentially(ObjectAnimator.ofFloat(iconView, View.ROTATION_Y, 0, -90), ObjectAnimator.ofFloat(iconView, View.ROTATION_Y, -90, 0));
                                animProfile.setInterpolator(new LinearInterpolator());
                                animProfile.setDuration(200);
                                animProfile.start();
                                return true;
                        }
                        return false;
                    }
                });

        setupViewPager(viewPager);


    }

    private void initialize() {

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewpager_main);
        viewPager.setOffscreenPageLimit(5);

        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        // Resize size bottom navigation icon

        menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            layoutParams = iconView.getLayoutParams();
            displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 42, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        productFragment = new ProductFragment();
        searchFragment = new SearchFragment();
        exploreFragment = new CollectionFragment();
        requestToSellFragment = new RequestToSellFragment(HomeActivity.this);
        profileFragment = new ProfileFragment();
        adapter.addFragment(productFragment);
        adapter.addFragment(searchFragment);
        searchFragment.setArguments("home");
        adapter.addFragment(exploreFragment);
        adapter.addFragment(requestToSellFragment);
        adapter.addFragment(profileFragment);
        viewPager.setAdapter(adapter);

        profileFragment.setArguments(viewPager, HomeActivity.this);
        if (getIntent().getExtras() != null) {
            String type = getIntent().getStringExtra("Notification");
            if (type.equals("isNotification")) {
                MyUtility.onResumeScreenType = 3;
                viewPager.setCurrentItem(3);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (viewPager.getCurrentItem() != 0) {
                viewPager.setCurrentItem(0);
            } else {
                super.onBackPressed();
            }
        }
    }


}