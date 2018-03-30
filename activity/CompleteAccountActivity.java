package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.FingurePrintAuthentication;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CompleteAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mToolbarTitle, txtCompleteAccount, txtUserName;
    private RecyclerView rvSizeList;
    private List<SizeModel> listSize;
    private CustomAdapter horizontalAdapter;
    private int selectedItem = -1;

    private String TAG = CompleteAccountActivity.class.getSimpleName();
    String email = null, password = null, mobile = null, username = null, firstname = null, lastname = null, defaultSize = null;
    CircleImageView circleImageView;
    String mediaPath;

    // Dialog
    RelativeLayout mRelativeLayout;
    ApiProduct apiProduct;
    SharedPreferences prefs;

    static String Tocken;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_complete_account);

        prefs = CompleteAccountActivity.this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

        setSizeDataInRecycleView();
        // data binding

        saveToPassingData();


        rvSizeList.addOnItemTouchListener(new RecyclerTouchListener(this, rvSizeList, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                defaultSize = listSize.get(position).getSizeId();
                registraionDark();
            }

            @Override
            public void onLongClick(View view, final int position) {
                // Long press Dialog

            }
        }));


    }

    private void setSizeDataInRecycleView() {
        listSize = new ArrayList<>();
        try {
            listSize = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        horizontalAdapter = new CustomAdapter(listSize);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(CompleteAccountActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvSizeList.setLayoutManager(horizontalLayoutManagaer);
        rvSizeList.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rvSizeList.setAdapter(horizontalAdapter);
    }

    private void initialization() {
        txtCompleteAccount = (TextView) findViewById(R.id.txtNext);
        rvSizeList = (RecyclerView) findViewById(R.id.rv_sizeList);
        circleImageView = (CircleImageView) findViewById(R.id.iv_CompleteProfileImage);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        circleImageView.setOnClickListener(this);
        //dialog
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        apiProduct = ApiClient.getClient().create(ApiProduct.class);


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbarCreate);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);

        mToolbarTitle.setText(getResources().getString(R.string.registrationcompletToolbar));

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtNext:
                CompleteSingupProcess();
                break;
        }

    }


    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        private List<SizeModel> sizeList;

        public CustomAdapter(List<SizeModel> listSize) {
            sizeList = listSize;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textViewName;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.textViewName = (TextView) itemView.findViewById(R.id.txtSize);
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rvsizelist_custom, parent, false);

            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
            SizeModel sizeModel = listSize.get(listPosition);

            TextView textViewName = holder.textViewName;
            textViewName.setText(sizeModel.getSizeValue());

          /*  if (selectedItem > listSize.size()) {
                selectedItem = 0;
            }*/
            if (selectedItem == listPosition) {
                textViewName.setBackground(getResources().getDrawable(R.drawable.circletext_border_darkgrayborderbg));
                textViewName.setTextColor(getResources().getColor(R.color.btn_bgwhite));

            } else {
                textViewName.setBackground(getResources().getDrawable(R.drawable.circle_plain_white));
                textViewName.setTextColor(getResources().getColor(R.color.btn_bgdark));
            }
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selectedItem);
                    selectedItem = listPosition;
                    notifyItemChanged(listPosition);
                }
            });

        }

        @Override
        public int getItemCount() {
            return sizeList.size();
        }
    }


    private void saveToPassingData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            password = extras.getString("password");
            mobile = extras.getString("mobile");
            username = extras.getString("username");
            firstname = extras.getString("firstname");
            lastname = extras.getString("lastname");
            mediaPath = extras.getString("mediaPath");
            if (mediaPath != null) {
                Glide.with(CompleteAccountActivity.this)
                        .load(mediaPath)
                        .into(circleImageView);
            } else {
                Glide.with(CompleteAccountActivity.this)
                        .load(R.drawable.iv_profile)
                        .into(circleImageView);
            }
            txtUserName.setText("Wecolme " + username);
        }

    }


    private void CompleteSingupProcess() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtCompleteAccount.setVisibility(View.GONE);
        String token = FirebaseInstanceId.getInstance().getToken();
        Tocken = String.valueOf(token);

        APILogin service = ApiClient.getClient().create(APILogin.class);
        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody reqPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody reqFirstname = RequestBody.create(MediaType.parse("text/plain"), firstname);
        RequestBody reqMidlename = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody reqLastname = RequestBody.create(MediaType.parse("text/plain"), lastname);
        RequestBody reqMobileNo = RequestBody.create(MediaType.parse("text/plain"), mobile);
        RequestBody reqDefaultsize = RequestBody.create(MediaType.parse("text/plain"), defaultSize);
        RequestBody reqDeviceType = RequestBody.create(MediaType.parse("text/plain"), "android");
        RequestBody reqDeviceTocken = RequestBody.create(MediaType.parse("text/plain"), token);
        MultipartBody.Part fileToUpload = null;
        if (mediaPath != null) {

          /*  File file = new File(mediaPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);*/

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            String imageName = timeStamp + ".jpg";
            Bitmap bm = BitmapFactory.decodeFile(mediaPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();


            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            fileToUpload = MultipartBody.Part.createFormData("image", imageName, requestFile);


        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            fileToUpload = MultipartBody.Part.createFormData("image", "", requestBody);
        }


        Call<LoginResponse> userCall = service.registerUser(reqEmail,
                reqPassword, reqFirstname, reqMidlename, reqLastname, reqMobileNo, reqDefaultsize, reqDeviceType, reqDeviceTocken, fileToUpload);

        userCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtCompleteAccount.setVisibility(View.VISIBLE);
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(CompleteAccountActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (loginResponse.getUser() != null) {


                            MyUtility.savePreferences(CompleteAccountActivity.this, "id", loginResponse.getUser().getId());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "email", loginResponse.getUser().getEmail());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "mobile", loginResponse.getUser().getMobile());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "first_name", loginResponse.getUser().getFirstName());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "middle_name", loginResponse.getUser().getMiddleName());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "last_name", loginResponse.getUser().getLastName());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "image", loginResponse.getUser().getImage());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "default_size", loginResponse.getUser().getDefaultSize());

                            MyUtility.savePreferences(CompleteAccountActivity.this, "wallet_balance", loginResponse.getUser().getWallet());
                            MyUtility.savePreferences(CompleteAccountActivity.this, "sellerType", loginResponse.getUser().getVerifiedSaller());
                            FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
                            mAuthentication.setEmail(email);
                            mAuthentication.setPassword(password);
                            mAuthentication.setEnableFignure(false);
                            MyUtility.saveObjectToSharedPreference(CompleteAccountActivity.this, "mPreference", "mObjectKey", mAuthentication);

                            Log.d(TAG, "response" + response);
                            Log.d(TAG, "id" + loginResponse.getUser().getId());
                            Log.d(TAG, "email" + loginResponse.getUser().getEmail());
                            Log.d(TAG, "mobile" + loginResponse.getUser().getMobile());
                            Log.d(TAG, "firstname" + loginResponse.getUser().getFirstName());
                            Log.d(TAG, "userName" + loginResponse.getUser().getMiddleName());
                            Log.d(TAG, "lastname" + loginResponse.getUser().getLastName());
                            Log.d(TAG, "image" + loginResponse.getUser().getImage());
                            Log.d(TAG, "defaultsize" + loginResponse.getUser().getDefaultSize());
                            Log.d(TAG, "wallet_balance" + loginResponse.getUser().getWallet());
                            Intent intent = new Intent(CompleteAccountActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(CompleteAccountActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                mRelativeLayout.setVisibility(View.GONE);
                txtCompleteAccount.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(CompleteAccountActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }


    public void registraionDark() {
        txtCompleteAccount.setOnClickListener(this);
        txtCompleteAccount.setEnabled(true);
        txtCompleteAccount.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtCompleteAccount.setTextColor(getResources().getColor(R.color.white));
    }


}
