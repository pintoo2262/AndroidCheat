package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.BrandModel;
import com.app.noan.model.SuggestedModel;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchNotProductFoundActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtSku, txtMessage, txtSuggest;
    RelativeLayout rlMessage;
    ConstraintLayout mConstraintLayout;
    String text;
    private RecyclerView rvBrand;
    public List<BrandModel> brandModelList;
    SearchNotFoundAdapter mBrandAdapter;
    SharedPreferences prefs;
    private int selectedItem = -1;
    private String brandId;
    Dialog pDialog;
    APISearch mAPApiSearch;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_search_not_product_found);
        mAPApiSearch = ApiClient.getClient().create(APISearch.class);
        text = getIntent().getStringExtra("sku");
        prefs = SearchNotProductFoundActivity.this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        toolBarAndDrawerInitilization();
        initialization();

        txtSku.setText(text);

        try {
            brandModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("BrandList", ObjectSerializer.serialize(new ArrayList())));
            setDataBrandAdvertisment(brandModelList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        rvBrand.addOnItemTouchListener(new RecyclerTouchListener(this, rvBrand, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                brandId = brandModelList.get(position).getId();
                registraionDark();
            }

            @Override
            public void onLongClick(View view, final int position) {
                // Long press Dialog

            }
        }));
    }

    private void initialization() {
        txtSku = (TextView) findViewById(R.id.txtSkuSet);

        rvBrand = (RecyclerView) findViewById(R.id.rv_brandSearch);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        rlMessage = (RelativeLayout) findViewById(R.id.rl_Message);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.constrianlayoutMainLayout);
        txtSuggest = (TextView) findViewById(R.id.txtSuggest);
        txtSuggest.setOnClickListener(this);

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
        mtoolbarTitle.setText(getResources().getString(R.string.search));
    }

    public void setDataBrandAdvertisment(List<BrandModel> brandModelList) {
        rvBrand.setHasFixedSize(true);
        mBrandAdapter = new SearchNotFoundAdapter(SearchNotProductFoundActivity.this, brandModelList);
        rvBrand.setLayoutManager(new LinearLayoutManager(SearchNotProductFoundActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvBrand.setAdapter(mBrandAdapter);
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
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSuggest:
                if (!brandId.equals("")) {
                    getSuggestedByServer();
                } else {
                    Toast.makeText(this, "Please select one BrandId", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    public class SearchNotFoundAdapter extends RecyclerView.Adapter<SearchNotFoundAdapter.ViewHolderAdvertisment> {
        Context context;
        List<BrandModel> brandAdapterList;
        public List<String> selectedBrandIdList;
        int height, width;
        Display display;

        public SearchNotFoundAdapter(FragmentActivity activity, List<BrandModel> brandModelList) {
            context = activity;
            brandAdapterList = brandModelList;
            selectedBrandIdList = new ArrayList<>();
            display = activity.getWindowManager().getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();
        }

        @Override
        public SearchNotFoundAdapter.ViewHolderAdvertisment onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rv_searchbrand, parent, false);
            SearchNotFoundAdapter.ViewHolderAdvertisment viewHolderAdvertisment = new SearchNotFoundAdapter.ViewHolderAdvertisment(view);
            return viewHolderAdvertisment;
        }

        @Override
        public void onBindViewHolder(final ViewHolderAdvertisment holder, final int position) {
            final BrandModel brandModel = brandAdapterList.get(position);


            if (selectedItem == position) {
                ColorFilter filterblack = new LightingColorFilter(Color.BLACK, Color.BLACK);
                holder.iv_brandIcon.setColorFilter(filterblack);
                holder.rlSquare.setBackgroundResource(R.drawable.square_brand);
            } else {
                ColorFilter filter = new LightingColorFilter(Color.WHITE, Color.WHITE);
                holder.iv_brandIcon.setColorFilter(filter);
                holder.rlSquare.setBackgroundResource(R.drawable.square_nobrand);

            }
            Glide.with(context).load(brandAdapterList.get(position).getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.iv_brandIcon);


            holder.iv_brandIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemChanged(selectedItem);
                    selectedItem = position;
                    notifyItemChanged(position);
                }
            });
        }


        @Override
        public int getItemCount() {
            return brandAdapterList.size();
        }

        public class ViewHolderAdvertisment extends RecyclerView.ViewHolder {

            ImageView iv_brandIcon;
            RelativeLayout rlSquare;


            public ViewHolderAdvertisment(View itemView) {
                super(itemView);
                iv_brandIcon = (ImageView) itemView.findViewById(R.id.iv_imageAdvertisement);
                rlSquare = (RelativeLayout) itemView.findViewById(R.id.rl_square);

            }
        }
    }


    public void showDialog() {
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();
    }

    public void hideDialog() {

        if (pDialog != null && pDialog.isShowing())
            pDialog.dismiss();
    }

    public void registraionDark() {
        txtSuggest.setEnabled(true);
        txtSuggest.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtSuggest.setTextColor(getResources().getColor(R.color.white));
    }

    private void getSuggestedByServer() {
        pDialog = new Dialog(SearchNotProductFoundActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("brand_id", brandId);
        credMap.put("sku", text);


        Call<SuggestedModel> suggestedModelCall = mAPApiSearch.getSuggestedSku(credMap);

        suggestedModelCall.enqueue(new Callback<SuggestedModel>() {
            @Override
            public void onResponse(Call<SuggestedModel> call, Response<SuggestedModel> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    SuggestedModel suggestedModel = ((SuggestedModel) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (suggestedModel.getStatus() == 1) {
                        txtMessage.setText(suggestedModel.getMessage());
                        mConstraintLayout.setVisibility(View.GONE);
                        rlMessage.setVisibility(View.VISIBLE);
//                        Toast.makeText(SearchNotProductFoundActivity.this, "" + suggestedModel.getMessage(),
//                                Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(SearchNotProductFoundActivity.this, "" + suggestedModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SuggestedModel> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(SearchNotProductFoundActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

}
