package com.app.noan.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.SizeModel;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
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

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtCancelOrder, txtProductName, txtSkuNo, txtStatus, txtPlaceOn, txtShipped, txtDeliverd, txtAmount, txtPaid, txtSoldbye, txtProductPrice, txtShippingPrice, txtNoanWalletBalance, txtLableSold, txtOfferExpiron, txtofferjustInfo;
    View mViewExpire;
    private ImageView ivProductImage;
    private String strStatus, strOrderId, strOfferId;
    NeedToConfirmModel mOrderDetails;

    APIRequestSell service;
    Boolean isABoolean = false;
    RelativeLayout mRelativeLayout;
    String type;
    public List<SizeModel> sizeModelList;
    String sizevalue;
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
        setContentView(R.layout.activity_order_detail);

        mOrderDetails = (NeedToConfirmModel) getIntent().getSerializableExtra("NeedConfirmModel");
        strStatus = mOrderDetails.getStatus();
        strOrderId = mOrderDetails.getId();
        strOfferId = mOrderDetails.getOfferId();


        type = MyUtility.getSavedPreferences(OrderDetailActivity.this, "sellerType");
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);

        toolBarAndDrawerInitilization();
        // initilzation of xml componet
        initialization();
        service = ApiClient.getClient().create(APIRequestSell.class);

        try {
            sizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sizeModelList.size() > 0) {
            for (int i = 0; i < sizeModelList.size(); i++) {
                if (mOrderDetails.getSizeId().equals(sizeModelList.get(i).getSizeId())) {
                    sizevalue = sizeModelList.get(i).getSizeValue();
                }
            }
        }

        txtProductName.setText("SZ " + sizevalue + " " + mOrderDetails.getProductName());
        txtSkuNo.setText(mOrderDetails.getProductSku());

        if (!mOrderDetails.getTransactionId().equals("")) {
            txtofferjustInfo.setVisibility(View.VISIBLE);
            txtOfferExpiron.setVisibility(View.VISIBLE);

            txtOfferExpiron.setText(mOrderDetails.getOffer_expiryDate());
        } else {
            txtofferjustInfo.setHeight(0);
            txtOfferExpiron.setHeight(0);
            ViewGroup.LayoutParams params = mViewExpire.getLayoutParams();
            params.height = 0;
            params.width = 0;
            mViewExpire.setLayoutParams(params);

        }

        if (strStatus.equals("delivered")) {
            txtStatus.setText("Your item has been delivered");
            txtCancelOrder.setText("Return");
            txtCancelOrder.setVisibility(View.GONE);
        } else if (strStatus.equals("pending")) {
            if (!mOrderDetails.getTransactionId().equals("")) {
                txtStatus.setText("Your offer not accepted yet !");
            } else {
                txtStatus.setText("Order Approval Pending");
            }
        } else if (strStatus.equals("expired")) {
            txtStatus.setText("Your offer expired !");
        } else if (strStatus.equals("shipped")) {
            txtStatus.setText("Seller has shipped your order");
        } else if (strStatus.equals("shipped")) {
            txtStatus.setText("Seller has shipped your order");
        } else if (strStatus.equals("confirmed")) {
            txtStatus.setText("Seller has processed your order");
        } else if (strStatus.equals("cancel_by_admin")) {
            txtStatus.setText("Your order has been cancelled by admin");
        } else if (strStatus.equals("cancel_by_user")) {
            txtStatus.setText("Your order has been cancelled");
            txtCancelOrder.setText("Cancelled");
        } else if (strStatus.equals("cancel_by_seller")) {
            txtStatus.setText("Your order has been cancelled by seller");
            txtCancelOrder.setText("Cancelled");
        } else if (strStatus.equals("cancel")) {
            txtStatus.setText("Your order has been cancelled");
            txtCancelOrder.setText("Cancelled");
        }


//        String pDate = MyUtility.getConvertDate(mOrderDetails.getCreated());
        String pDate = mOrderDetails.getCreated();
        String sDate, dDate;
        if (mOrderDetails.getShippedDate().equals("")) {
            sDate = "";
        } else {
//            sDate = MyUtility.getConvertDate(mOrderDetails.getShippedDate());
            sDate = mOrderDetails.getShippedDate();
        }
        if (mOrderDetails.getDeliveredDate().equals("")) {
            dDate = "";
        } else {
//            dDate = MyUtility.getConvertDate(mOrderDetails.getDeliveredDate());
            dDate = mOrderDetails.getDeliveredDate();
        }
        txtPlaceOn.setText(pDate);
        txtShipped.setText(sDate);
        txtDeliverd.setText(dDate);
        txtProductPrice.setText("$ " + mOrderDetails.getTotal());
        txtShippingPrice.setText("$ " + mOrderDetails.getShippingRate());
        txtNoanWalletBalance.setText("$ " + mOrderDetails.getUsedWalletBalance());
        txtAmount.setText("$" + mOrderDetails.getGrandTotal());
        txtPaid.setText(mOrderDetails.getPaymentMethod());
        if (mOrderDetails.getSellerName() != null) {
            if (!mOrderDetails.getSellerName().equals("") && mOrderDetails.getSellerName() != null) {
                txtSoldbye.setText(mOrderDetails.getSellerName());
            } else {
                txtSoldbye.setText(mOrderDetails.getUsersellerName());
            }
        } else {
            if (!mOrderDetails.getUsername().equals("") && mOrderDetails.getUsername() != null) {
                txtSoldbye.setText(mOrderDetails.getUsername());
            } else {
                txtSoldbye.setText(mOrderDetails.getVendorName());
            }
            txtLableSold.setText(R.string.SoldTo);

        }

        Glide.with(OrderDetailActivity.this).

                load(mOrderDetails.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivProductImage);


    }


    private void initialization() {
        ivProductImage = (ImageView) findViewById(R.id.iv_OrderDetails_images);
        txtSkuNo = (TextView) findViewById(R.id.txt_OrderDetails_skuno);
        txtProductName = (TextView) findViewById(R.id.txt_OrderDetails_company);
        txtStatus = (TextView) findViewById(R.id.txt_OrderDetails_Status);
        txtPlaceOn = (TextView) findViewById(R.id.txt_OrderDetails_PlaceOn);
        txtShipped = (TextView) findViewById(R.id.txt_OrderDetails_ShippedOn);
        txtDeliverd = (TextView) findViewById(R.id.txt_OrderDetails_Delivered);
        txtAmount = (TextView) findViewById(R.id.txt_OrderDetails_Amount);
        txtPaid = (TextView) findViewById(R.id.txt_OrderDetails_paid);
        txtSoldbye = (TextView) findViewById(R.id.txt_OrderDetails_Soldby);
        txtCancelOrder = (TextView) findViewById(R.id.txt_cancel_order);
        txtProductPrice = (TextView) findViewById(R.id.txt_ProudctPrice);
        txtShippingPrice = (TextView) findViewById(R.id.txt_ShippingPrice);
        txtNoanWalletBalance = (TextView) findViewById(R.id.txt_NoanWalletBalance);
        txtLableSold = (TextView) findViewById(R.id.txt_just_sold);

        txtofferjustInfo = (TextView) findViewById(R.id.txt_just_offerExpired);
        txtOfferExpiron = (TextView) findViewById(R.id.txt_Offer_ExipredOn);
        mViewExpire = findViewById(R.id.viewLine13);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        txtCancelOrder.setOnClickListener(this);


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
        if (!mOrderDetails.getTransactionId().equals("")) {
            if (mOrderDetails.getStatus().equals("pending")) {
                mtoolbarTitle.setText("Offer #" + strOfferId);
            } else if (mOrderDetails.getStatus().equals("expired")) {
                mtoolbarTitle.setText("Offer #" + strOfferId);
            } else {
                mtoolbarTitle.setText("Order #" + strOrderId);
            }
        } else {
            mtoolbarTitle.setText("Order #" + strOrderId);
        }


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
            case R.id.txt_cancel_order:
             /*   if (txtCancelOrder.getText().toString().equals("Return")) {
                    Intent intent = new Intent(OrderDetailActivity.this, OrderReturnActivity.class);
                    intent.putExtra("NeedConfirmModel", (Serializable) mOrderDetails);
                    startActivity(intent);
                } else if (!txtCancelOrder.getText().toString().equals("Cancelled")) {
                    cancelOrderStatus();
                }*/
                break;
        }
    }


    private void cancelOrderStatus() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtCancelOrder.setVisibility(View.INVISIBLE);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("order_id", strOrderId);
        if (type.equals("vendorseller")) {
            credMap.put("status", "cancel_by_seller");
        } else {
            credMap.put("status", "cancel_by_user");
        }

        Call<OrderResponse> call = service.orderStatusChanges(credMap);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    mRelativeLayout.setVisibility(View.GONE);
                    txtCancelOrder.setVisibility(View.VISIBLE);
                    OrderResponse loginResponse = ((OrderResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        isABoolean = true;
                        finish();
                        Toast.makeText(OrderDetailActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderDetailActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // error case
                    mRelativeLayout.setVisibility(View.GONE);
                    txtCancelOrder.setVisibility(View.VISIBLE);
                    switch (response.code()) {
                        case 404:
                            Toast.makeText(OrderDetailActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
                            Toast.makeText(OrderDetailActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(OrderDetailActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log error here since request failed
                mRelativeLayout.setVisibility(View.GONE);
                txtCancelOrder.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(OrderDetailActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


}
