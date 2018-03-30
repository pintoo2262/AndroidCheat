package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.NeedViewResponse;
import com.app.noan.model.Product;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NeedActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView rvMoreSizeNeed;
    LinearLayoutManager linearLayoutManager;


    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtSaveNeed;
    ImageView ciClose, ivProductImage;


    private List<SizeModel> needSizeList;
    NeedSizeAdapter mNeedSizeAdpater;
    SharedPreferences prefs;

    Product mProduct;

    String sSizId, userId, getSizeId;
    RelativeLayout mRelativeLayout;
    ApiProduct service;
    Dialog pDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_need);

        service = ApiClient.getClient().create(ApiProduct.class);
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        mProduct = (Product) getIntent().getSerializableExtra("product");
        userId = MyUtility.getSavedPreferences(NeedActivity.this, "id");
        toolBarAndDrawerInitilization();


        initilization();


        setAdapterNotificationList();


        Glide.with(this).load(mProduct.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);


        getNeedProductSize();

    }

    private void initilization() {
        rvMoreSizeNeed = (RecyclerView) findViewById(R.id.rvNeedSizeList);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMoreSizeNeed.setLayoutManager(linearLayoutManager);
        rvMoreSizeNeed.setHasFixedSize(true);
        ivProductImage = (ImageView) findViewById(R.id.iv_ProductImage);
        txtSaveNeed = (TextView) findViewById(R.id.txtSaveneed);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);

        txtSaveNeed.setOnClickListener(this);


    }

    private void setAdapterNotificationList() {
        try {
            needSizeList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mNeedSizeAdpater = new NeedSizeAdapter(needSizeList);
        rvMoreSizeNeed.setAdapter(mNeedSizeAdpater);


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar_myneed);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        ciClose = mToolbar.findViewById(R.id.iv_close);
        mtoolbarTitle.setText(mProduct.getProduct_name());
        ciClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSaveneed:
                sSizId = setCommaSeparatedSize(mNeedSizeAdpater.sizeModelList);
                addNeeProductSize();
                break;
        }
    }


    public class NeedSizeAdapter extends RecyclerView.Adapter<NeedSizeAdapter.ViewHolder> {

        private List<SizeModel> sizeModelList;


        public NeedSizeAdapter(List<SizeModel> numbers) {
            this.sizeModelList = new ArrayList<>(numbers);

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rv_notificationsetting, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.bindData(sizeModelList.get(position));

            //in some cases, it will prevent unwanted situations
            holder.mSwitch.setOnCheckedChangeListener(null);

            //if true, your checkbox will be selected, else unselected
            holder.mSwitch.setChecked(sizeModelList.get(position).isSelected());
            if (sizeModelList.get(position).isSelected()) {
                sizeModelList.get(position).setSelected(true);
            }

            holder.mSwitch.setTag(sizeModelList.get(position));

            holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Switch cb = (Switch) buttonView;
                    SizeModel contact = (SizeModel) cb.getTag();

                    contact.setSelected(cb.isChecked());
                    if (isChecked) {
                        sizeModelList.get(position).setSelected(true);
                    } else {
                        sizeModelList.get(position).setSelected(false);
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return sizeModelList.size();
        }

        public void notifySetDataChanges(ArrayList<String> items) {
            for (int i = 0; i < sizeModelList.size(); i++) {
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j).equals(sizeModelList.get(i).getSizeId())) {
                        sizeModelList.get(i).setSelected(true);
                        break;
                    }
                }
            }
            notifyDataSetChanged();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            private TextView txtSizeValue;
            private Switch mSwitch;

            public ViewHolder(View v) {
                super(v);
                txtSizeValue = (TextView) itemView.findViewById(R.id.txt_notificationsetting);
                mSwitch = itemView.findViewById(R.id.switch_notificationsetting);
            }

            public void bindData(SizeModel number) {
                txtSizeValue.setText(number.getSizeValue());
            }
        }
    }

    private String setCommaSeparatedSize(List<SizeModel> mSizeselction) {
        StringBuilder sType3 = new StringBuilder();

        for (int i = 0; i < mSizeselction.size(); i++) {
            //append the value into the builder
            if (mSizeselction.get(i).isSelected()) {
                sType3.append(mSizeselction.get(i).getSizeId());
                //if the value is not the last element of the list
                //then append the comma(,) as well
                if (i != mSizeselction.size() - 1) {
                    sType3.append(",");
                }
            }
        }
        if (sType3.length() != 0) {
            sType3.deleteCharAt(sType3.length() - (sType3.length() - sType3.lastIndexOf(",")));
        }
        return sType3.toString();
    }


    private void addNeeProductSize() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtSaveNeed.setVisibility(View.INVISIBLE);

        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("admin_product_id", String.valueOf(mProduct.getProduct_id()));
        credMap.put("size", sSizId);

        Call<LoginResponse> addNeedProduct = service.needProduct(credMap);
        addNeedProduct.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    mRelativeLayout.setVisibility(View.GONE);
                    txtSaveNeed.setVisibility(View.VISIBLE);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                mRelativeLayout.setVisibility(View.GONE);
                txtSaveNeed.setVisibility(View.VISIBLE);
                Toast.makeText(NeedActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void getNeedProductSize() {
        pDialog = new Dialog(NeedActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();

        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("admin_product_id", String.valueOf(mProduct.getProduct_id()));

        Call<NeedViewResponse> addNeedProduct = service.viewNeedSizeId(credMap);
        addNeedProduct.enqueue(new Callback<NeedViewResponse>() {
            @Override
            public void onResponse(Call<NeedViewResponse> call, Response<NeedViewResponse> response) {
                hideDialog();
                if (response.isSuccessful()) {

                    NeedViewResponse viewResponse = ((NeedViewResponse) response.body());
                    getSizeId = viewResponse.getData();
                    ArrayList<String> items = new ArrayList<>();
                    Collections.addAll(items, getSizeId.split(","));
                    mNeedSizeAdpater.notifySetDataChanges(items);
                }
            }

            @Override
            public void onFailure(Call<NeedViewResponse> call, Throwable t) {
                call.cancel();
                mRelativeLayout.setVisibility(View.GONE);
                txtSaveNeed.setVisibility(View.VISIBLE);
                Toast.makeText(NeedActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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

}
