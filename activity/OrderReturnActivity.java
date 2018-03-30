package com.app.noan.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.model.NeedToConfirmModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderReturnActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    NeedToConfirmModel mOrderDetails;
    private String strOrderId;
    ImageView ivReturnProductImage;
    TextView txtReturnProductSKu, txtReturnProductAmount, txtReturnProductName, txtReturnProdocutStatus, txtSumbit;
    EditText edtReason;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_order_return);

        mOrderDetails = (NeedToConfirmModel) getIntent().getSerializableExtra("NeedConfirmModel");
        strOrderId = mOrderDetails.getId();

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();

    }

    private void initialization() {
        txtReturnProductSKu = (TextView) findViewById(R.id.txt_OrderRetrun_skuno);
        txtReturnProductName = (TextView) findViewById(R.id.txt_OrderRetrun_descr);
        txtReturnProductAmount = (TextView) findViewById(R.id.txt_OrderRetrun_Amount);
        ivReturnProductImage = (ImageView) findViewById(R.id.iv_OrderRetrun_image);
        txtReturnProdocutStatus = (TextView) findViewById(R.id.txt_OrderRetrun_Status);
        edtReason = (EditText) findViewById(R.id.edt_OrderRetrun_reason);

        txtReturnProductSKu.setText("SKU: " + mOrderDetails.getProductSku());
        txtReturnProductName.setText(mOrderDetails.getProductName());
        txtReturnProdocutStatus.setText(mOrderDetails.getOrderStatus());
        txtReturnProductAmount.setText(mOrderDetails.getTotal());
        Glide.with(OrderReturnActivity.this).load(mOrderDetails.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivReturnProductImage);
        txtSumbit = (TextView) findViewById(R.id.txt_OrderRetrun_submit);
        txtSumbit.setOnClickListener(this);
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
        mtoolbarTitle.setText("Order #" + strOrderId);


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

    }

/*
    private void returnOrderStatus() {

        Map<String, String> credMap = new HashMap<>();
        credMap.put("order_id", strOrderId);
        credMap.put("status", "return");

        Call<OrderResponse> call = service.orderStatusChanges(credMap);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.isSuccessful()) {
                    OrderResponse loginResponse = ((OrderResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        Intent intent = new Intent(OrderDetailActivity.this, SellActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(OrderDetailActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(OrderDetailActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();

                Toast.makeText(OrderDetailActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }
*/


}
