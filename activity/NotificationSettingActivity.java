package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.NotificationModel;
import com.app.noan.model.NotificationResponse;
import com.app.noan.model.OrderResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NotificationSettingActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private RecyclerView rvNotificationList;
    LinearLayoutManager linearLayoutManager;
    NotificationAdapter mAdapter;
    List<NotificationModel> notificationModelList;
    public List<String> selectedNotificationId;
    Dialog pDialog;
    APILogin mApiLogin;
    public String sNotificationId, mUserId;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_notificataion_setting);

        toolBarAndDrawerInitilization();

        initilization();

        myNotificationListByServer();


    }

    private void initilization() {
        notificationModelList = new ArrayList<>();
        rvNotificationList = (RecyclerView) findViewById(R.id.rv_NotificationList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNotificationList.setLayoutManager(linearLayoutManager);
        rvNotificationList.setHasFixedSize(true);
        mUserId = MyUtility.getSavedPreferences(NotificationSettingActivity.this, "id");

    }

    private void setAdapterNotificationList() {
        mAdapter = new NotificationAdapter(NotificationSettingActivity.this, notificationModelList);
        rvNotificationList.setAdapter(mAdapter);
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
        mtoolbarTitle.setText(getResources().getString(R.string.notificationSetting));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btnSave:
                sNotificationId = setCommaSeparateNotificationId(selectedNotificationId);
                userNotificationByserver(sNotificationId);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private String setCommaSeparateNotificationId(List<String> mselectedTypeList) {
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

    private void userNotificationByserver(String sNotificationId) {
        pDialog = new Dialog(NotificationSettingActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();

        mApiLogin = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap1 = new HashMap<>();
        credMap1.put("user_id", mUserId);
        credMap1.put("notification_id", sNotificationId);

        Call<OrderResponse> mResponseCall = mApiLogin.userChangesNotification(credMap1);
        mResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    OrderResponse mNotificationResponse = ((OrderResponse) response.body());
                    if (mNotificationResponse.getStatus() == 1) {
                        Toast.makeText(NotificationSettingActivity.this, "" + mNotificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificationSettingActivity.this, "" + mNotificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(NotificationSettingActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }


    private void myNotificationListByServer() {
        pDialog = new Dialog(NotificationSettingActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();

        mApiLogin = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", mUserId);
        Call<NotificationResponse> mResponseCall = mApiLogin.notificaitonSetting(credMap);
        mResponseCall.enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    NotificationResponse mNotificationResponse = ((NotificationResponse) response.body());
                    if (mNotificationResponse.getStatus() == 1) {
                        notificationModelList = mNotificationResponse.getmNotificationResponseList();
                        setAdapterNotificationList();
                    } else {
                        Toast.makeText(NotificationSettingActivity.this, "" + mNotificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(NotificationSettingActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }

    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewholder> {

        Context context;
        private List<NotificationModel> notificationModelList;

        public NotificationAdapter(FragmentActivity activity, List<NotificationModel> notificationModelList1) {
            context = activity;
            notificationModelList = notificationModelList1;
            selectedNotificationId = new ArrayList<>();
        }

        @Override
        public NotificationViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_notificationsetting, parent, false);
            return new NotificationViewholder(view);
        }

        @Override
        public void onBindViewHolder(final NotificationViewholder holder, final int position) {
            NotificationModel notificationModel = notificationModelList.get(position);
            holder.txtNotificationName.setText(notificationModel.getName());
            if (notificationModel.getValue().equals("1")) {
                holder.mSwitch.setChecked(true);
                selectedNotificationId.add(notificationModelList.get(position).getId());
            }
            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        holder.mSwitch.setChecked(true);
                        selectedNotificationId.add(notificationModelList.get(position).getId());
                    }else {
                        holder.mSwitch.setChecked(false);
                        selectedNotificationId.remove(notificationModelList.get(position).getId());
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return notificationModelList.size();
        }


        public class NotificationViewholder extends RecyclerView.ViewHolder {
            private TextView txtNotificationName;
            private Switch mSwitch;

            public NotificationViewholder(View itemView) {
                super(itemView);
                txtNotificationName = (TextView) itemView.findViewById(R.id.txt_notificationsetting);
                mSwitch = itemView.findViewById(R.id.switch_notificationsetting);
            }
        }


    }

}