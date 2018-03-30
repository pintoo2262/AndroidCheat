package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.model.OrderResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class DeliverActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = DeliverActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtTile;
    Button btnConfirm;
    ImageView ivProductImage;
    NeedToConfirmModel needToConfirmModel;
    TextView txtOrderDate, txtOrderNumber, txtAmountMode, txtName, txtSku, txtSize, txtBox, txtDefect, txtProductPrice, txtShippingPrice, txtUsedWallet, txtTotalAmount, txtNote, txtBuyerName;
    String type;

    View view;
    LinearLayout llDefect, llBox;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_solde);
        needToConfirmModel = (NeedToConfirmModel) getIntent().getSerializableExtra("NeedToConfirmModel");
        type = MyUtility.getSavedPreferences(DeliverActivity.this, "sellerType");
        initilization();

        if (type.equals("isUserSell")) {
            btnConfirm.setVisibility(View.GONE);
        }

        toolBarAndDrawerInitilization();

    }

    private void initilization() {
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setText("deliver");
        btnConfirm.setAllCaps(true);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        txtTile = (TextView) findViewById(R.id.tv_sold_title);
        txtTile.setText("Sandals Shipped");

        txtOrderDate = (TextView) findViewById(R.id.txtOrderDateTime);
        txtOrderNumber = (TextView) findViewById(R.id.txtOrderNo);
        txtAmountMode = (TextView) findViewById(R.id.txtProductPrice);
        txtName = (TextView) findViewById(R.id.txtProductName);
        txtSku = (TextView) findViewById(R.id.txtProductSku);
        txtSize = (TextView) findViewById(R.id.txtProductSize);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtShippingPrice = (TextView) findViewById(R.id.txtShippingPrice);
        txtUsedWallet = (TextView) findViewById(R.id.txtUsedWallet);
        txtTotalAmount = (TextView) findViewById(R.id.txtorderTotal);
        txtBox = (TextView) findViewById(R.id.txtBoxCondtion);
        txtDefect = (TextView) findViewById(R.id.txtDefect);
        txtNote = (TextView) findViewById(R.id.tv_congratulations_note);
        txtNote.setText("If the product is delivered, update status here");
        view = (View) findViewById(R.id.lastBottomView);
        llBox = (LinearLayout) findViewById(R.id.llBox);
        llDefect = (LinearLayout) findViewById(R.id.llDefect);
        txtBuyerName = (TextView) findViewById(R.id.txtBuyerUserName);

        btnConfirm.setOnClickListener(this);

//        String date = MyUtility.getConvertDate(needToConfirmModel.getCreated());
        String date = needToConfirmModel.getCreated();

        txtOrderDate.setText(date);
        txtOrderNumber.setText(needToConfirmModel.getId());
        txtAmountMode.setText(needToConfirmModel.getTotal());
        txtName.setText(needToConfirmModel.getProductName());
        txtSku.setText(needToConfirmModel.getProductSku());
        txtSize.setText(needToConfirmModel.getSValue());
        txtProductPrice.setText("$ " + needToConfirmModel.getTotal());
        txtShippingPrice.setText("$ " + needToConfirmModel.getShippingRate());
        txtUsedWallet.setText("$ " + needToConfirmModel.getUsedWalletBalance());
        txtTotalAmount.setText("$ " + needToConfirmModel.getGrandTotal());
        txtBox.setText(needToConfirmModel.getBoxCondition());
        txtDefect.setText(needToConfirmModel.getIssues());
        txtBuyerName.setText(needToConfirmModel.getBuyerUserName());

        if (type.equals("vendorseller")) {
            llBox.setVisibility(View.GONE);
            llDefect.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            txtBox.setText(needToConfirmModel.getBoxCondition());
            txtDefect.setText(needToConfirmModel.getIssues());
        }

        Glide.with(this).load(needToConfirmModel.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);
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
        mtoolbarTitle.setText("S h i p p e d");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_help:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                changeOrderStatus();
                break;
        }
    }

    private void changeOrderStatus() {

        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("order_id", needToConfirmModel.getId());
        credMap.put("status", "delivered");

        Call<OrderResponse> call = service.orderStatusChanges(credMap);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.isSuccessful()) {
                    OrderResponse loginResponse = ((OrderResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        Intent intent = new Intent(DeliverActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("moveScreenType", "SellerFragment");
                        startActivity(intent);


                        Toast.makeText(DeliverActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DeliverActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();

                Toast.makeText(DeliverActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


}
