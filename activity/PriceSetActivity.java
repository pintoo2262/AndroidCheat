package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.OrderDatum;
import com.app.noan.model.PriceModel;
import com.app.noan.model.Product;
import com.app.noan.retrofit_api.APISearch;
import com.app.noan.retrofit_api.ApiClient;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PriceSetActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PriceSetActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private Button btnNext;

    private ScrollingValuePicker myScrollingValuePicker;
    private static float MIN_VALUE = 5;
    private static float MAX_VALUE = 100;
    private static float Center_VALUE;

    private static final float LINE_RULER_MULTIPLE_SIZE = 4.5f;

    public RecyclerView rv_LastOrderSold;
    private List<OrderDatum> lastOrderPriceSoldList;
    LastOrderPriceSoldAdapter lastsixOrderAdapter;
    private GridLayoutManager lLayout;

    TextView txtFree, txtMakePrice, txtOfferPrice, txtLowPrice, txtHighPrice, txtMaxOfferPrice, txtNoData;
    ImageView ivProductImage;


    private Product mProduct;
    private String strCondtion, strIssue, strOtherissue, strSize, stPrice, stDisplaySize, stFees = "20";
    int middleValue;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_set);


        mProduct = (Product) getIntent().getSerializableExtra("searchProduct");
        strCondtion = getIntent().getStringExtra("box_condition");
        strIssue = getIntent().getStringExtra("issues");
        strOtherissue = getIntent().getStringExtra("other_issues");
        strSize = getIntent().getStringExtra("defaultSize");
        stDisplaySize = getIntent().getStringExtra("displaySize");

        toolBarAndDrawerInitilization();


        initialize();


        setLastSoldAdapter();

        Glide.with(this).load(mProduct.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);

        productSkuByServerAPI();


       /* Double cMin = (Double.parseDouble(mProduct.getProduct_price()) / 100) * 20;
        int iMin = cMin.intValue();
        int minPrice = Integer.parseInt(mProduct.getProduct_price()) - iMin;
        int maxPrice = Integer.parseInt(mProduct.getProduct_price()) + iMin;*/

        MIN_VALUE = (float) 25;
        MAX_VALUE = (float) 2000;
        middleValue = 25;
       /* if (mProduct.getProduct_price() != null) {
            middleValue = Integer.parseInt(mProduct.getProduct_price());
        } else {
            middleValue = 25;
        }*/


        myScrollingValuePicker.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
        myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
        myScrollingValuePicker.setValueTypeMultiple(50);
        myScrollingValuePicker.setInitValue(middleValue);
        txtOfferPrice.setText("$" + middleValue);
        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myScrollingValuePicker.getScrollView().startScrollerTask();
                }
                return false;
            }
        });

        stPrice = String.valueOf(middleValue);

        myScrollingValuePicker.setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {
            }

            @Override
            public void onScrollStopped(int l, int t) {
                txtOfferPrice.setText("$" +
                        DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView()
                                , l
                                , t
                                , MAX_VALUE
                                , MIN_VALUE
                                , myScrollingValuePicker.getViewMultipleSize()));

                stPrice = String.valueOf(DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView()
                        , l
                        , t
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize()));

                middleValue = DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView()
                        , l
                        , t
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize());
                int cal = middleValue - Integer.parseInt(stFees);
                txtMakePrice.setText("You will make $" + String.valueOf(cal));

            }
        });
        int cal = middleValue - Integer.parseInt(stFees);
        txtMakePrice.setText("You will make $" + String.valueOf(cal));


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText("S I Z E  " + stDisplaySize + " / " + strCondtion);
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.black));
    }

    private void initialize() {
        myScrollingValuePicker = (ScrollingValuePicker) findViewById(R.id.myScrollingValuePicker);
        rv_LastOrderSold = (RecyclerView) findViewById(R.id.rv_offerDialog);
        ivProductImage = (ImageView) findViewById(R.id.iv_productImag);

        // Offer Price Main
        txtOfferPrice = (TextView) findViewById(R.id.txt_offer_price);

        // Make Price
        txtMakePrice = (TextView) findViewById(R.id.txt_your_makePrice);
        txtFree = (TextView) findViewById(R.id.txt_free);


        // Price Set By lowest highest maxoffered
        txtLowPrice = (TextView) findViewById(R.id.txtLowSellPrice);
        txtHighPrice = (TextView) findViewById(R.id.txtHightPrice);
        txtMaxOfferPrice = (TextView) findViewById(R.id.txtmaxprice);
        txtNoData = (TextView) findViewById(R.id.txtNoData);


        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);


        txtFree.setText("Fee $" + stFees);
        lastOrderPriceSoldList = new ArrayList<>();


    }

    private void setLastSoldAdapter() {
        lLayout = new GridLayoutManager(getApplicationContext(), 2);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_LastOrderSold.getContext(), DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vertical_line));
        rv_LastOrderSold.addItemDecoration(dividerItemDecoration);
        rv_LastOrderSold.setHasFixedSize(true);
        rv_LastOrderSold.setLayoutManager(lLayout);
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
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                Intent intent
                        = new Intent(PriceSetActivity.this, ProductPicActivity.class);
                intent.putExtra("searchProduct", mProduct);
                intent.putExtra("box_condition", strCondtion);
                intent.putExtra("issues", strIssue);
                intent.putExtra("other_issues", strOtherissue);
                intent.putExtra("defaultSize", strSize);
                intent.putExtra("productPrice", stPrice);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }


    private class LastOrderPriceSoldAdapter extends RecyclerView.Adapter<LastOrderPriceSoldAdapter.DailogViewHolder> {
        Context activity;
        List<OrderDatum> lastOrderSoldsList;


        public LastOrderPriceSoldAdapter(Context applicationContext, List<OrderDatum> selllerProductList) {
            this.activity = applicationContext;
            lastOrderSoldsList = selllerProductList;
        }


        public class DailogViewHolder extends RecyclerView.ViewHolder {

            TextView txtLeftPrice, txtRightDate, txtRightPrice, txtLeftDate;
            LinearLayout llLeft, llRight;

            public DailogViewHolder(View itemView) {
                super(itemView);
                txtLeftPrice = (TextView) itemView.findViewById(R.id.txtLeftSoldPrice);
                txtLeftDate = itemView.findViewById(R.id.txtLeftSoldDate);
                txtRightPrice = (TextView) itemView.findViewById(R.id.txtRightSoldPrice);
                txtRightDate = itemView.findViewById(R.id.txtRightSoldDate);
                llLeft = itemView.findViewById(R.id.llSoldLeft);
                llRight = itemView.findViewById(R.id.llSoldRight);
            }
        }

        @Override
        public LastOrderPriceSoldAdapter.DailogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_lastprice_sold_layout, parent, false);

            LastOrderPriceSoldAdapter.DailogViewHolder rcv = new LastOrderPriceSoldAdapter.DailogViewHolder(view);
            return rcv;
        }

        @Override
        public void onBindViewHolder(LastOrderPriceSoldAdapter.DailogViewHolder holder, int position) {
            String date;
            if (position % 2 == 0) {
                holder.llLeft.setVisibility(View.VISIBLE);
                holder.llRight.setVisibility(View.GONE);
                date = createDateFormate(lastOrderSoldsList.get(position).getCreated());
                holder.txtLeftPrice.setText("$ " + lastOrderSoldsList.get(position).getTotal());
                holder.txtLeftDate.setText(date);
            } else {
                holder.llLeft.setVisibility(View.GONE);
                holder.llRight.setVisibility(View.VISIBLE);
                date = createDateFormate(lastOrderSoldsList.get(position).getCreated());
                holder.txtRightPrice.setText("$ " + lastOrderSoldsList.get(position).getTotal());
                holder.txtRightDate.setText(date);
            }

        }

        @Override
        public int getItemCount() {
            return lastOrderSoldsList.size();
        }
    }

    public String createDateFormate(String date) {
        try {
            SimpleDateFormat yyyymmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = yyyymmm.parse(date);
            SimpleDateFormat retriveDate = new SimpleDateFormat("yyyy-MM-dd");
            return retriveDate.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    private void productSkuByServerAPI() {

        APISearch service = ApiClient.getClient().create(APISearch.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("sku", mProduct.getProduct_skuno());
        credMap.put("size_id", strSize);


        Call<PriceModel> call = service.setMaxprice(credMap);
        call.enqueue(new Callback<PriceModel>() {
            @Override
            public void onResponse(Call<PriceModel> call, Response<PriceModel> response) {
                if (response.isSuccessful()) {
                    PriceModel priceModel = ((PriceModel) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (priceModel.getStatus() == 1) {
                        if (!priceModel.getLastprice().getMinPrice().toString().equals("")) {
                            txtLowPrice.setText("$ " + priceModel.getLastprice().getMinPrice());
                        } else {
                            txtLowPrice.setText("-");
                        }
                        if (!priceModel.getLastprice().getMaxPrice().toString().equals("")) {
                            txtHighPrice.setText("$ " + priceModel.getLastprice().getMaxPrice());
                        } else {
                            txtHighPrice.setText("-");
                        }
                        if (!priceModel.getLastprice().getUserMaxPrice().toString().equals("")) {
                            txtMaxOfferPrice.setText("$ " + priceModel.getLastprice().getUserMaxPrice());
                        } else {
                            txtMaxOfferPrice.setText("-");
                        }
                        lastOrderPriceSoldList = priceModel.getLastprice().getOrderDatumArrayList();
                        if (lastOrderPriceSoldList.size() > 0) {
                            txtNoData.setVisibility(View.GONE);
                            setDataInAdapter();
                        } else {
                            txtNoData.setVisibility(View.VISIBLE);
                        }

                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<PriceModel> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void setDataInAdapter() {
        lastsixOrderAdapter = new LastOrderPriceSoldAdapter(PriceSetActivity.this, lastOrderPriceSoldList);
        rv_LastOrderSold.setAdapter(lastsixOrderAdapter);
    }


}
