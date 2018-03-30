package com.app.noan.activity;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.app.noan.model.LoginResponse;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ChangesizeActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtSave, txtUserName;
    private RecyclerView rvSizeList;

    private List<SizeModel> sizeList;
    private CustomAdapter horizontalAdapter;
    private int selectedItem = -1;
    String mediaPath;
    CircleImageView ciProCircleImageView;
    String strDefaultSize;
    RelativeLayout mRelativeLayout;
    ApiProduct apiProduct;
    public static final String TAG = ChangesizeActivity.class.getSimpleName();
    SharedPreferences prefs;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_changesize);

        apiProduct = ApiClient.getClient().create(ApiProduct.class);
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

        mediaPath = MyUtility.getSavedPreferences(ChangesizeActivity.this, "image");
        txtUserName.setText(MyUtility.getSavedPreferences(ChangesizeActivity.this, "first_name"));
        strDefaultSize = MyUtility.getSavedPreferences(ChangesizeActivity.this, "default_size");

        if (mediaPath != null && !mediaPath.equals("") && !mediaPath.equals(null)) {
            Glide.with(ChangesizeActivity.this)
                    .load(mediaPath)
                    .asBitmap()
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(ciProCircleImageView);
        } else {
            Glide.with(ChangesizeActivity.this)
                    .load(mediaPath)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.noimage)
                    .into(ciProCircleImageView);
        }
        setSizeDataInRecycleView();


    }

    private void setSizeDataInRecycleView() {
        try {
            sizeList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < sizeList.size(); i++) {
            if (sizeList.get(i).getSizeId().equals(strDefaultSize)) {
                sizeList.get(i).setSelected(true);
            }
        }
        horizontalAdapter = new CustomAdapter(sizeList);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(ChangesizeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvSizeList.setLayoutManager(horizontalLayoutManagaer);
        rvSizeList.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rvSizeList.setAdapter(horizontalAdapter);
    }

    private void initialization() {
        rvSizeList = (RecyclerView) findViewById(R.id.rv_sizeList);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        ciProCircleImageView = (CircleImageView) findViewById(R.id.iv_CompleteProfileImage);
        txtSave = (TextView) findViewById(R.id.txtSave);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtSave.setOnClickListener(this);
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
        mtoolbarTitle.setText(getResources().getString(R.string.changestoolsbar));

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
            case R.id.txtSave:
                chagesDefaultSizeByserver(strDefaultSize);
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
        public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rvsizelist_custom, parent, false);

            CustomAdapter.MyViewHolder myViewHolder = new CustomAdapter.MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int listPosition) {
            TextView textViewName = holder.textViewName;
            textViewName.setText(sizeList.get(listPosition).getSizeValue());

            if (strDefaultSize.equals(sizeList.get(listPosition).getSizeId())) {
                selectedItem = listPosition;
            }

            if (selectedItem == listPosition) {
                textViewName.setBackground(getResources().getDrawable(R.drawable.circletext_border_darkgrayborderbg));
                textViewName.setTextColor(getResources().getColor(R.color.btn_bgwhite));
                strDefaultSize = sizeList.get(listPosition).getSizeId();

            } else {
                textViewName.setBackground(getResources().getDrawable(R.drawable.circle_plain_white));
                textViewName.setTextColor(getResources().getColor(R.color.btn_bgdark));
            }
            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    strDefaultSize = sizeList.get(listPosition).getSizeId();
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


    private void chagesDefaultSizeByserver(String strDefaultSize) {

        mRelativeLayout.setVisibility(View.VISIBLE);
        txtSave.setVisibility(View.GONE);


        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", MyUtility.getSavedPreferences(ChangesizeActivity.this, "id"));
        credMap.put("size_id", strDefaultSize);

        Call<LoginResponse> call = service.changesDefaultSize(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtSave.setVisibility(View.VISIBLE);
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(ChangesizeActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        MyUtility.savePreferences(ChangesizeActivity.this, "default_size", loginResponse.getUser().getDefaultSize());
                        finish();
                    } else {
                        Toast.makeText(ChangesizeActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                mRelativeLayout.setVisibility(View.GONE);
                txtSave.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(ChangesizeActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


}
