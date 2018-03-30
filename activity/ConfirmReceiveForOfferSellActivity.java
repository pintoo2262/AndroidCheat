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
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.ReceiveOfferForSellListModel;
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


public class ConfirmReceiveForOfferSellActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ConfirmReceiveForOfferSellActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    Button btnConfirm;
    ImageView ivProductImage;
    ReceiveOfferForSellListModel receiveOfferForSellListModel;
    TextView txtOfferNumber, txtProductName, txtProductSku, txtProductSize, txtOfferDate, txtOfferExpiryDate, txtOfferPrice,
            txtShippingPrice, txtUsedWallet, txtTotalAmount, txtBuyerUserName, txtNote;

    String type;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_receiveforsell);
        receiveOfferForSellListModel = (ReceiveOfferForSellListModel) getIntent().getSerializableExtra("ReceiveOfferForSellListModel");

        type = MyUtility.getSavedPreferences(ConfirmReceiveForOfferSellActivity.this, "sellerType");

        initilization();


        toolBarAndDrawerInitilization();

    }

    private void initilization() {
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        btnConfirm.setOnClickListener(this);

        txtOfferNumber = (TextView) findViewById(R.id.txtOfferNumber);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductSku = (TextView) findViewById(R.id.txtProductSku);
        txtProductSize = (TextView) findViewById(R.id.txtProductSize);
        txtOfferDate = (TextView) findViewById(R.id.txtOfferDate);
        txtOfferExpiryDate = (TextView) findViewById(R.id.txtOfferExpiryDate);
        txtOfferPrice = (TextView) findViewById(R.id.txtOfferPrice);
        txtShippingPrice = (TextView) findViewById(R.id.txtShippingPrice);
        txtUsedWallet = (TextView) findViewById(R.id.txtUsedWallet);
        txtTotalAmount = (TextView) findViewById(R.id.txtorderTotal);
        txtBuyerUserName = (TextView) findViewById(R.id.txtBuyerUserName);
        txtNote = (TextView) findViewById(R.id.tv_congratulations_note);
        txtNote.setText("Accept the Offer");

        txtOfferNumber.setText(receiveOfferForSellListModel.getOfferId());
        txtProductName.setText(receiveOfferForSellListModel.getProductName());
        txtProductSku.setText(receiveOfferForSellListModel.getSku());


        txtProductSize.setText(receiveOfferForSellListModel.getSize());
//        String date = MyUtility.getConvertDate(receiveOfferForSellListModel.getOfferDate());
        String date = receiveOfferForSellListModel.getOfferDate();
        txtOfferDate.setText(date);
        String expriydate = receiveOfferForSellListModel.getOfferExpiryDate();
        txtOfferExpiryDate.setText(expriydate);
        txtOfferPrice.setText("$ " + receiveOfferForSellListModel.getOfferPrice());
        txtShippingPrice.setText("$ " + receiveOfferForSellListModel.getShippingRate());
        txtUsedWallet.setText("$ " + receiveOfferForSellListModel.getUsedWalletBalance());
        txtTotalAmount.setText("$ " + receiveOfferForSellListModel.getGrandTotal());
        txtBuyerUserName.setText(receiveOfferForSellListModel.getBuyerUserName());
        Glide.with(this).load(receiveOfferForSellListModel.getImage())
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
        mtoolbarTitle.setText(getResources().getString(R.string.offer1));
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
                if (!receiveOfferForSellListModel.getCurrentStock().equals("0")) {
                    changeOrderStatus();
                } else {
                    Toast.makeText(this, "No stock available for this product. Please add stock first", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void changeOrderStatus() {

        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_seller_id", MyUtility.getSavedPreferences(ConfirmReceiveForOfferSellActivity.this, "id"));
        credMap.put("offer_id", receiveOfferForSellListModel.getOfferId());
        credMap.put("status", "accepted");

        Call<OrderResponse> call = service.receivedConfirmedOrder(credMap);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.isSuccessful()) {
                    OrderResponse loginResponse = ((OrderResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        Intent intent = new Intent(ConfirmReceiveForOfferSellActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.putExtra("moveScreenType", "SellerFragment");
                        startActivity(intent);
                        Toast.makeText(ConfirmReceiveForOfferSellActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ConfirmReceiveForOfferSellActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                call.cancel();

                Toast.makeText(ConfirmReceiveForOfferSellActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


}
