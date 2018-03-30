package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.LoginActivity;
import com.app.noan.activity.RegistrationStep1Activity;
import com.app.noan.activity.SettingsActivity;
import com.app.noan.activity.SplashActivity2;
import com.app.noan.activity.USerOrderActivity;
import com.app.noan.model.LoginResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ProfileFragment extends Fragment implements View.OnClickListener {

    ConstraintLayout clProfileWithLogin, clProfileWithoutLogin;
    Button btnLogin, btnRegistration;

    // Titles of the individual pages (displayed in tabs)
    private String[] PAGE_TITLES;

    ProfileMyFragment mProfileMyFragment;
    ProfileNeedFragment mProfileNeedFragment;

    // The fragments that are used as the individual pages
    private final Fragment[] PAGES = new Fragment[]{
            new ProfileNeedFragment(),
            new ProfileMyFragment()
    };
    ViewPager mHomeViewpager;
    Activity mActivity;

    // The ViewPager is responsible for sliding pages (fragments) in and out upon user input
    private ViewPager mViewPager;

    /*TabLayout tabLayout;*/


    ImageView ivPluse;

    TextView txt_orders, txt_profilename;
    ImageView iv_profiledots;

    DachshundTabLayout tabLayout;
    String  type, UserId;


    public ProfileFragment() {

    }


    public static Fragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        type = MyUtility.getSavedPreferences(getContext(), "sellerType");
        UserId = MyUtility.getSavedPreferences(getContext(), "id");

        initialize(view);


        return view;
    }

    private void initialize(View view) {
        clProfileWithLogin = view.findViewById(R.id.profileConstraint);
        clProfileWithoutLogin = view.findViewById(R.id.withoutProfilelink);
        btnLogin = view.findViewById(R.id.btnIncludeLogin);
        btnRegistration = view.findViewById(R.id.btnIncludeRegister);
        if (!UserId.equals("")) {
            clProfileWithLogin.setVisibility(View.VISIBLE);
            clProfileWithoutLogin.setVisibility(View.GONE);
        } else {
            clProfileWithLogin.setVisibility(View.GONE);
            clProfileWithoutLogin.setVisibility(View.VISIBLE);
        }

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);


        PAGE_TITLES = getResources().getStringArray(R.array.profiletab);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout = (DachshundTabLayout) view.findViewById(R.id.tab_profile_Layout);

        txt_orders = (TextView) view.findViewById(R.id.txt_profileorders);
        txt_orders.setOnClickListener(this);

        txt_profilename = (TextView) view.findViewById(R.id.txt_profilename);

        if (type.equals("vendorseller")) {
            txt_profilename.setText(MyUtility.getSavedPreferences(getContext(), "name"));
        } else {
            txt_profilename.setText(MyUtility.getSavedPreferences(getContext(), "middle_name"));
        }


        iv_profiledots = (ImageView) view.findViewById(R.id.iv_profiledots);
        iv_profiledots.setOnClickListener(this);


        ivPluse = view.findViewById(R.id.imgTraddingAdd);


        ivPluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments("profile");
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragmentcontainer, searchFragment);
                ft.addToBackStack("Profile");
                ft.commit();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_profileorders:
                Intent intent = new Intent(mActivity, USerOrderActivity.class);
                startActivity(intent);
                break;

            case R.id.iv_profiledots:
                Context wrapper = new ContextThemeWrapper(mActivity, R.style.popupmenustyle);
                PopupMenu popupMenu = new PopupMenu(wrapper, iv_profiledots);
                popupMenu.setGravity(Gravity.CENTER_VERTICAL);
                mActivity.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.settings:
                                intent = new Intent(mActivity, SettingsActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.share:
                                int applicationNameId = mActivity.getApplicationInfo().labelRes;
                                final String appPackageName = mActivity.getPackageName();
                                intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                String text = "Hey, I came across this awesome app. You can browse and buy beautiful sandals.Check the Noan application here:\n ";
                                String link = "http://projects-beta.com/noan_new/webservices/user/appshare";
                                Uri appLinkData = Uri.parse(link);
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Try this awesome app!");
                                intent.putExtra(Intent.EXTRA_TEXT, text + " " + appLinkData);
                                startActivity(Intent.createChooser(intent, "Try this awesome app!"));
                                return true;
                            case R.id.logout:
                                if (!isNetworkConnected()) {
                                    Toast.makeText(getContext(), getResources().getString(R.string.error_msg_no_internet), Toast.LENGTH_SHORT).show();
                                } else {
                                    userLogout();
                                }


                                return true;
                        }
                        return false;
                    }
                });
                break;

            case R.id.btnIncludeLogin:
                Intent intent3 = new Intent(mActivity, LoginActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);
                mActivity.finish();
                break;
            case R.id.btnIncludeRegister:
                Intent intent5 = new Intent(mActivity, RegistrationStep1Activity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent5);
                mActivity.finish();
                break;
        }
    }

    public void setArguments(ViewPager viewPager, HomeActivity homeActivity) {
        mHomeViewpager = viewPager;
        mActivity = homeActivity;
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

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    mProfileNeedFragment = new ProfileNeedFragment();
                    mProfileNeedFragment.setArguments(mActivity);
                    return mProfileNeedFragment;
                case 1:
                    mProfileMyFragment = new ProfileMyFragment();
                    mProfileMyFragment.setArguments(mHomeViewpager, mActivity);
                    return mProfileMyFragment;
                default:
                    mProfileNeedFragment = new ProfileNeedFragment();
                    mProfileNeedFragment.setArguments(mActivity);
                    return mProfileNeedFragment;
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) { // fragment is visible and have created
            mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
            tabLayout.setupWithViewPager(mViewPager);
            if (type.equals("vendorseller")) {
                txt_profilename.setText(MyUtility.getSavedPreferences(mActivity, "name"));
            } else {
                txt_profilename.setText(MyUtility.getSavedPreferences(mActivity, "middle_name"));
            }
        }
    }


    private void userLogout() {
        APILogin service = ApiClient.getClient().create(APILogin.class);

        Call<LoginResponse> call = service.userlogOut(UserId, "android");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(getActivity(), "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mActivity, SplashActivity2.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // instagram cook remove
                        CookieManager.getInstance().removeAllCookie();
                        // facebook remove


                        if (type.equals("vendorseller")) {
                            MyUtility.romove(mActivity, "id");
                            MyUtility.romove(mActivity, "email");
                            MyUtility.romove(mActivity, "name");
                            MyUtility.romove(mActivity, "image");
                            MyUtility.romove(mActivity, "payPalEmail");
                            MyUtility.romove(mActivity, "wallet_balance");
                            MyUtility.romove(mActivity, "sellerType");
                            MyUtility.romove(mActivity, "payPalEmail");
                        } else {
                            MyUtility.romove(mActivity, "id");
                            MyUtility.romove(mActivity, "email");
                            MyUtility.romove(mActivity, "mobile");
                            MyUtility.romove(mActivity, "first_name");
                            MyUtility.romove(mActivity, "middle_name");
                            MyUtility.romove(mActivity, "last_name");
                            MyUtility.romove(mActivity, "image");
                            MyUtility.romove(mActivity, "default_size");
                            MyUtility.romove(mActivity, "wallet_balance");
                            MyUtility.romove(mActivity, "sellerType");
                            MyUtility.romove(mActivity, "payPalEmail");
                        }
                        startActivity(intent);
                        mActivity.finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }

}

