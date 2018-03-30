package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.FingurePrintAuthentication;
import com.app.noan.utils.MyUtility;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private ListView rvSetting;
    private ArrayList<String> settingList;
    private ArrayList<String> settingListvendor;
    String isUserSeller;

    List<String> isSellerList;
    List<String> isVendorList;
    String[] sellerList, vendorList;
    SettingAdapter mSettingAdapter;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_settings);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

        isUserSeller = MyUtility.getSavedPreferences(SettingsActivity.this, "sellerType");

        if (isUserSeller.equals("vendorseller")) {
            vendorList = getResources().getStringArray(R.array.vendorsetting);
            for (int i = 0; i < vendorList.length; i++) {
                isVendorList.add(vendorList[i]);
            }
            isVerndorSeller();


        } else {
            sellerList = getResources().getStringArray(R.array.setting);
            for (int j = 0; j < sellerList.length; j++) {
                isSellerList.add(sellerList[j]);
            }

            isUserSeller();
        }


    }

    private void initialization() {
        rvSetting = (ListView) findViewById(R.id.rv_setting);
        settingList = new ArrayList<String>();
        settingListvendor = new ArrayList<String>();
        isSellerList = new ArrayList<>();
        isVendorList = new ArrayList<>();
    }

    private void isUserSeller() {
        mSettingAdapter = new SettingAdapter(SettingsActivity.this, isSellerList);
        rvSetting.setAdapter(mSettingAdapter);

        rvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(SettingsActivity.this, PaymentActivity.class);
                    startActivity(intent);
                } else if (i == 2) {
                    Intent intent = new Intent(SettingsActivity.this, NotificationSettingActivity.class);
                    startActivity(intent);
                } else if (i == 3) {
                    Intent intent = new Intent(SettingsActivity.this, ChangesizeActivity.class);
                    startActivity(intent);
                } else if (i == 4) {
                    Intent intent = new Intent(SettingsActivity.this, FAQActivity.class);
                    startActivity(intent);
                } else if (i == 5) {
                    Intent intent = new Intent(SettingsActivity.this, PaymentHistoryActivity.class);
                    startActivity(intent);
                } else if (i == 6) {
                    Intent intent = new Intent(SettingsActivity.this, FeedBackActivity.class);
                    startActivity(intent);
                } else if (i == 7) {
                    Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                } else if (i == 8) {
                    Intent intent = new Intent(SettingsActivity.this, TermAndConditionsActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        });
    }

    private void isVerndorSeller() {
        mSettingAdapter = new SettingAdapter(SettingsActivity.this, isVendorList);
        rvSetting.setAdapter(mSettingAdapter);
        rvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                    startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(SettingsActivity.this, PaymentActivity.class);
                    startActivity(intent);
                } else if (i == 2) {
                    Intent intent = new Intent(SettingsActivity.this, NotificationSettingActivity.class);
                    startActivity(intent);
                } else if (i == 3) {
                    Intent intent = new Intent(SettingsActivity.this, FAQActivity.class);
                    startActivity(intent);
                } else if (i == 4) {
                    Intent intent = new Intent(SettingsActivity.this, PaymentHistoryActivity.class);
                    startActivity(intent);
                } else if (i == 5) {
                    Intent intent = new Intent(SettingsActivity.this, FeedBackActivity.class);
                    startActivity(intent);
                } else if (i == 6) {
                    Intent intent = new Intent(SettingsActivity.this, PrivacyPolicyActivity.class);
                    startActivity(intent);
                } else if (i == 7) {
                    Intent intent = new Intent(SettingsActivity.this, TermAndConditionsActivity.class);
                    startActivity(intent);
                } else if (i == 8) {

                }
            }
        });
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
        mtoolbarTitle.setText(getResources().getString(R.string.settings));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }


    private class SettingAdapter extends BaseAdapter {
        Context context;
        List<String> settingList;


        public SettingAdapter(SettingsActivity settingsActivity, List<String> isSellerList) {
            context = settingsActivity;
            settingList = isSellerList;
        }


        @Override
        public int getCount() {
            return settingList.size();
        }

        @Override
        public Object getItem(int position) {
            return settingList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Viewholder viewholder = null;
            if (view == null) {
                LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflate.inflate(R.layout.layout_common_txtview, viewGroup, false);
                viewholder = new Viewholder();
                viewholder.txtSystemName = (TextView) view.findViewById(R.id.txtCommontxt);
                viewholder.mASwitch = view.findViewById(R.id.switch_authe);
                view.setTag(viewholder);
            } else {
                viewholder = (Viewholder) view.getTag();
            }
            viewholder.txtSystemName.setText(settingList.get(i));
            if (settingList.get(i).equals("FINGERPRINT AUTHENTICATION")) {
                viewholder.mASwitch.setVisibility(View.VISIBLE);
            } else {
                viewholder.mASwitch.setVisibility(View.GONE);
            }

            FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
            mAuthentication = MyUtility.getSavedObjectFromPreference(SettingsActivity.this, "mPreference", "mObjectKey", FingurePrintAuthentication.class);

            if (mAuthentication.getEnableFignure().equals(false)) {
                viewholder.mASwitch.setChecked(false);
                mAuthentication.setEnableFignure(false);
            } else {
                viewholder.mASwitch.setChecked(true);
                mAuthentication.setEnableFignure(true);
            }


            final FingurePrintAuthentication finalMAuthentication = mAuthentication;
            viewholder.mASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        finalMAuthentication.setEnableFignure(true);
                    } else {
                        finalMAuthentication.setEnableFignure(false);
                    }
                    MyUtility.saveObjectToSharedPreference(SettingsActivity.this, "mPreference", "mObjectKey", finalMAuthentication);
                }
            });

            return view;
        }


        class Viewholder {
            TextView txtSystemName;
            Switch mASwitch;
        }

    }


}