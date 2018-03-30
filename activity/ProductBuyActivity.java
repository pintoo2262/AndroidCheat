package com.app.noan.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.model.DeleteAddreesResponse;
import com.app.noan.model.EditAddressResponse;
import com.app.noan.model.OfferProduct;
import com.app.noan.model.OrderResponse;
import com.app.noan.model.SelllerProduct;
import com.app.noan.model.SizeModel;
import com.app.noan.model.StoreAddress;
import com.app.noan.model.StoreAddressResponse;
import com.app.noan.model.VendorResponse;
import com.app.noan.model.ViewAddressResponse;
import com.app.noan.retrofit_api.APIAddress;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.retrofit_api.ApiProduct;
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

public class ProductBuyActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG = ProductBuyActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtCancel;

    APIAddress service;

    // Intent Pass Data
    SelllerProduct mSelllerProduct;
    OfferProduct mOfferProduct;

    // Top
    TextView txtBuyProductTitle, txtBuyProductSize;
    ImageView ivProductImage, ivPaypal;

    // Display View
    private TextView tvReviewTotalAmt, txtShippedAddress, txtShipPayment, txtPurchase, txtDisplayAddress, txtDisplayPayment, tvShippingCharge;


    // ShipTo Recycleview list Address
    RecyclerView rvShipList;
    AddressAdapter mAddressAdapter;
    List<StoreAddress> storeAddressList;


    // Add new Address
    private EditText edtFullName, edtPhoneNo, edtAddress, edtApiUnit, edtCity, edtPost;
    String stFullname, stAddress, stEdtUnit, stCity, stPost, stState, stPhoneNo, stCountry;
    int billing_Address_ID;
    AppCompatSpinner spinner;
    CustomStateAdapter mSpinerAdapter;
    String[] countryNames = {"Afghanistan", "Australia", "Bangladesh", "Bhutan", "China", "Colombia", "Denmark", "Egypt", "Finland", "Germany", "Hong Kong", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Japan", "Kenya", "Libya", "Mexico", "Nepal", "Oman", "Pakistan", "Qatar", "Russia", "Singapore", "Thailand", "United Arab Emirates", "Venezuela", "Yemen", "Zimbabwe"};


    RelativeLayout mRelativeLayout;
    ProgressBar progressBar;
    BottomSheetDialog mListAddressDialog, mAddNewAddress, mAddressUpdate, mBottomSheetPaymentDialog, mTotalCostCaluDialog;


    String screenType, type, strPayPal;
    String strDisplayPrice;
    int grantTotal, defaultWalletBalance, walletBalance, bal, gto, usedWalletBalance;


    //Order Parameter
    String productId, userId, userSellerId, sellerId, exp_in, size, productPrice, shippingRate = "0";


    // Review Total Dialog Initilization

    TextView txtDone, txtSubTotal, txtShippingCost, txtTotalCost, txtWalletBalance;
    ImageView ivBack;
    CheckBox mCheckBoxWallet;
    Boolean isCheckbox = false;


    ProgressDialog pd;
    MyUtility myUtility;

    //size
    SharedPreferences prefs;
    public List<SizeModel> sizeModelList;
    String sizevalue, sizeId;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_product_buy);

        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        myUtility = new MyUtility(ProductBuyActivity.this);
        userId = MyUtility.getSavedPreferences(this, "id");
        type = MyUtility.getSavedPreferences(this, "sellerType");
        service = ApiClient.getClient().create(APIAddress.class);
        screenType = getIntent().getStringExtra("screenType");

        toolBarAndDrawerInitilization();


        // initilzation of xml componet
        initialization();

        storeAddressList = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(ProductBuyActivity.this);


        listAddressByServer();

        try {
            sizeModelList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // fetchin Seller Product Details

        if (screenType.equals("buynow")) {
            mSelllerProduct = (SelllerProduct) getIntent().getSerializableExtra("SelllerProductList");
            productId = mSelllerProduct.getProductId();
            exp_in = "0";
            productPrice = mSelllerProduct.getPrice();
            sellerId = mSelllerProduct.getSellerId();
            size = mSelllerProduct.getSize();
            userSellerId = mSelllerProduct.getUsersellerId();
            txtBuyProductTitle.setText(mSelllerProduct.getProductName());

            if (sizeModelList.size() > 0) {
                for (int i = 0; i < sizeModelList.size(); i++) {
                    if (mSelllerProduct.getSize().equals(sizeModelList.get(i).getSizeId())) {
                        sizevalue = sizeModelList.get(i).getSizeValue();
                    }
                }
            }


            txtBuyProductSize.setText("Size " + sizevalue);
            Glide.with(this).load(mSelllerProduct.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(ivProductImage);

        } else {
            mOfferProduct = (OfferProduct) getIntent().getSerializableExtra("OfferProduct");
            sizeId = getIntent().getStringExtra("OfferSizeId");


            productId = mOfferProduct.getId();
            exp_in = getIntent().getStringExtra("exp_in");
            productPrice = getIntent().getStringExtra("selectPrice");
            sellerId = "0";
            userSellerId = "0";
//            size = mOfferProduct.getSize();
            size = sizeId;
            txtBuyProductTitle.setText(mOfferProduct.getProductName());

            if (sizeModelList.size() > 0) {
                for (int i = 0; i < sizeModelList.size(); i++) {
                    if (sizeId.equals(sizeModelList.get(i).getSizeId())) {
                        sizevalue = sizeModelList.get(i).getSizeValue();
                    }
                }
            }


            txtBuyProductSize.setText("Size " + sizevalue);

            Glide.with(this).load(mOfferProduct.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(ivProductImage);

        }


    }


    private void initialization() {
        // Top
        ivProductImage = (ImageView) findViewById(R.id.iv_productbuyImage);
        txtBuyProductTitle = (TextView) findViewById(R.id.txt_buy_ProductTitle);
        txtBuyProductSize = (TextView) findViewById(R.id.txt_buy_Productsize);

        // Display
        txtShippedAddress = (TextView) findViewById(R.id.txt_shipAddres);
        txtShipPayment = (TextView) findViewById(R.id.txt_payment);
        tvShippingCharge = (TextView) findViewById(R.id.txt_shipping_charge);
        tvReviewTotalAmt = (TextView) findViewById(R.id.txt_review_total_price);


        // Middle
        txtDisplayAddress = (TextView) findViewById(R.id.txt_shipAddresDisplay);
        txtDisplayPayment = (TextView) findViewById(R.id.txt_shipPayMethodeDisplay);
        ivPaypal = (ImageView) findViewById(R.id.iv_paypal);


        //Button
        txtPurchase = (TextView) findViewById(R.id.txt_purchase);
        txtCancel = (TextView) findViewById(R.id.ib_cancel);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);

        // BottomSheet Dialog
        mListAddressDialog = new BottomSheetDialog(ProductBuyActivity.this);
        mAddNewAddress = new BottomSheetDialog(ProductBuyActivity.this);
        mAddressUpdate = new BottomSheetDialog(ProductBuyActivity.this);


        mBottomSheetPaymentDialog = new BottomSheetDialog(ProductBuyActivity.this);
        mTotalCostCaluDialog = new BottomSheetDialog(ProductBuyActivity.this);


        //click
        txtShippedAddress.setOnClickListener(this);
        txtShipPayment.setOnClickListener(this);
        txtPurchase.setOnClickListener(this);
        txtCancel.setOnClickListener(this);


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.help_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);

        if (screenType.equals("buynow")) {
            mtoolbarTitle.setText(getResources().getString(R.string.buyToolbar));
        } else {
            mtoolbarTitle.setText(getResources().getString(R.string.offer1));
        }


    }

    public void purchaseTransBtn() {
        txtPurchase.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtPurchase.setTextColor(getResources().getColor(R.color.black));
    }

    public void purchaseDarkBtn() {

        txtPurchase.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtPurchase.setTextColor(getResources().getColor(R.color.white));
    }


    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder("");
        spanTxt.append(" " + strDisplayPrice);
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(final View widget) {

                View sheetView = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_totalcost_bottom_sheet, null);
                txtDone = sheetView.findViewById(R.id.txtDone);
                txtSubTotal = sheetView.findViewById(R.id.txt_subtotal);
                txtShippingCost = sheetView.findViewById(R.id.txt_shippingCost);
                txtTotalCost = sheetView.findViewById(R.id.txtTotalcost);
                txtWalletBalance = sheetView.findViewById(R.id.txt_WalletBalance);
                mCheckBoxWallet = sheetView.findViewById(R.id.ch_Walletbalance);
                ivBack = sheetView.findViewById(R.id.iv_dialogBack);
                mTotalCostCaluDialog.setCancelable(false);

                txtSubTotal.setText("$ " + productPrice);
                txtShippingCost.setText("$ " + shippingRate);

                if (isCheckbox) {
                    mCheckBoxWallet.setChecked(true);
                    txtWalletBalance.setText("$ " + defaultWalletBalance);
                    txtTotalCost.setText("$" + String.valueOf(grantTotal));
                } else {
                    mCheckBoxWallet.setChecked(false);
                    txtWalletBalance.setText("$ " + defaultWalletBalance);
                    txtTotalCost.setText("$" + String.valueOf(grantTotal));
                }


                mCheckBoxWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            if (walletBalance >= Integer.parseInt(productPrice) + Integer.parseInt(shippingRate)) {
                                bal = walletBalance - (Integer.parseInt(productPrice) + Integer.parseInt(shippingRate));
//                                txtWalletBalance.setText("$ " + bal);
                                txtWalletBalance.setText("$ " + defaultWalletBalance);
                                txtTotalCost.setText("$" + "0");

                            } else {
                                gto = ((Integer.parseInt(productPrice) + Integer.parseInt(shippingRate)) - walletBalance);
//                                txtWalletBalance.setText("$ " + 0);
                                txtWalletBalance.setText("$ " + defaultWalletBalance);
                                txtTotalCost.setText("$" + String.valueOf(gto));

                            }
                        } else {
                            txtWalletBalance.setText("$ " + defaultWalletBalance);
                            int gdbal = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            txtTotalCost.setText("$" + String.valueOf(gdbal));
                            usedWalletBalance = 0;
                        }
                    }
                });


                txtDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTotalCostCaluDialog.dismiss();
                        if (mCheckBoxWallet.isChecked()) {
                            isCheckbox = true;
                            if (walletBalance >= Integer.parseInt(productPrice) + Integer.parseInt(shippingRate)) {
                                grantTotal = 0;
                                walletBalance = bal;
                                usedWalletBalance = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            } else {
                                grantTotal = gto;
                                walletBalance = 0;
                                usedWalletBalance = defaultWalletBalance;
                            }
                        } else {
                            isCheckbox = false;
                            grantTotal = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            walletBalance = defaultWalletBalance;
                            usedWalletBalance = 0;
                        }

                        strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                        customTextView(tvReviewTotalAmt);


                    }
                });
                ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTotalCostCaluDialog.dismiss();
                    }
                });


                mTotalCostCaluDialog.setContentView(sheetView);
                mTotalCostCaluDialog.show();


            }
        }, spanTxt.length() - strDisplayPrice.length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_shipAddres:

                if (storeAddressList.size() > 0) {
                    ImageView ivCloseDialog;
                    Button btnAddNewAddress;
                    View viewaddress = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_ship_addresslist, null);
                    ivCloseDialog = viewaddress.findViewById(R.id.iv_dialogBack);
                    btnAddNewAddress = viewaddress.findViewById(R.id.btnNewAddress);
                    rvShipList = viewaddress.findViewById(R.id.rv_ShipAddressList);
                    progressBar = (ProgressBar) viewaddress.findViewById(R.id.main_progress);
                    LinearLayoutManager horizontalLayoutManagaer
                            = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                    rvShipList.setLayoutManager(horizontalLayoutManagaer);
                    rvShipList.addItemDecoration(new SpacesItemDecoration_horizontal(14));
                    mListAddressDialog.setCancelable(false);
                    mListAddressDialog.setContentView(viewaddress);
                    mListAddressDialog.show();
                    rvShipList.setAdapter(mAddressAdapter);

                    ivCloseDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mListAddressDialog.dismiss();
                        }
                    });


                    btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AddAddress();
                            mAddNewAddress.show();
                        }
                    });
                } else {
                    AddAddress();
                    mAddNewAddress.show();
                }


                break;
            case R.id.txt_payment:
                payMentBottomSheetDialog();
                break;
            case R.id.txt_purchase:
                String add = txtDisplayAddress.getText().toString();
                String pay = txtDisplayPayment.getText().toString();
                if (!add.equals("")) {
                    if (!pay.equals("")) {
                        orderPlaceByServer();
                    } else {
                        dialog("Please make sure you've entered Paypal Email");
                    }
                } else {
                    dialog("Please make sure you've entered shipping address");
                }
                break;
            case R.id.ib_cancel:
                onBackPressed();
                break;
        }
    }

    void AddAddress() {
        Button btnDoneAddNewAddress;
        ImageView ivCloseAddNewAddress;
        View viewaddress = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_newaddress, null);
        mAddNewAddress.setCancelable(false);
        mAddNewAddress.setContentView(viewaddress);
        mAddNewAddress.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        btnDoneAddNewAddress = viewaddress.findViewById(R.id.btnDone);
        edtFullName = (EditText) viewaddress.findViewById(R.id.edtFullName);
        edtPhoneNo = (EditText) viewaddress.findViewById(R.id.edtPhono);
        edtAddress = (EditText) viewaddress.findViewById(R.id.edtAddress);
        edtApiUnit = (EditText) viewaddress.findViewById(R.id.edtApi);
        edtCity = (EditText) viewaddress.findViewById(R.id.edtCity);
        edtPost = (EditText) viewaddress.findViewById(R.id.edtpost);
        spinner = (AppCompatSpinner) viewaddress.findViewById(R.id.edtcountry);
        stState = "";
        mSpinerAdapter = new CustomStateAdapter(getApplicationContext(), countryNames);
        spinner.setAdapter(mSpinerAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(
                    AdapterView<?> adapterView, View view,
                    int i, long l) {
                stCountry = countryNames[i];
            }

            public void onNothingSelected(
                    AdapterView<?> adapterView) {

            }
        });

        ivCloseAddNewAddress = viewaddress.findViewById(R.id.iv_dialogBack);
        ivCloseAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddNewAddress.dismiss();
            }
        });
        btnDoneAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewAddressByServer(mAddNewAddress);
            }
        });
    }


    private void listAddressByServer() {
        pd = new ProgressDialog(ProductBuyActivity.this);
        pd.setMessage("Please wait Loading...");
        pd.setCancelable(false);
        pd.show();
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);


        Call<ViewAddressResponse> storeAddressResponsecall = service.viewAddress(credMap);
        storeAddressResponsecall.enqueue(new Callback<ViewAddressResponse>() {
            @Override
            public void onResponse(Call<ViewAddressResponse> call, Response<ViewAddressResponse> response) {
                ViewAddressResponse ViewAddressResponse = response.body();
                if (response.isSuccessful()) {
                    pd.dismiss();
                    if (ViewAddressResponse.getStatus() == 1) {
                        if (ViewAddressResponse.getStoreAddressList().size() > 0) {
                            // Save Response sh pre
                            MyUtility.savePreferences(ProductBuyActivity.this, "payPalEmail", ViewAddressResponse.getPaypalEmail());
                            MyUtility.savePreferences(ProductBuyActivity.this, "wallet_balance", ViewAddressResponse.getWallet());
                            defaultWalletBalance = Integer.parseInt(MyUtility.getSavedPreferences(ProductBuyActivity.this, "wallet_balance"));
                            strPayPal = MyUtility.getSavedPreferences(ProductBuyActivity.this, "payPalEmail");
                            if (!strPayPal.equals("")) {
                                txtDisplayPayment.setText("Paypal");
                                ivPaypal.setVisibility(View.VISIBLE);
                            }
                            walletBalance = defaultWalletBalance;

                            // set Data in Recycleview
                            storeAddressList = ViewAddressResponse.getStoreAddressList();
                            mAddressAdapter.clear();
                            mAddressAdapter.addAll(storeAddressList);
                            for (int i = 0; i < storeAddressList.size(); i++) {
                                if (storeAddressList.get(i).getDefault_addres().equals("1")) {
                                    billing_Address_ID = storeAddressList.get(i).getId();
                                    shippingRate = storeAddressList.get(i).getShippingRate();
                                    txtDisplayAddress.setText(storeAddressList.get(i).getApiUnit() + "," + storeAddressList.get(i).getAddress());
                                }
                            }
                            grantTotal = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                            customTextView(tvReviewTotalAmt);
                            tvShippingCharge.setText("Shipping Charge  $" + shippingRate);

                            if (!txtDisplayAddress.getText().toString().equals("") && !txtDisplayPayment.getText().toString().equals("")) {
                                purchaseDarkBtn();
                            } else {
                                purchaseTransBtn();
                            }

                        } else {
                            MyUtility.savePreferences(ProductBuyActivity.this, "payPalEmail", ViewAddressResponse.getPaypalEmail());
                            MyUtility.savePreferences(ProductBuyActivity.this, "wallet_balance", ViewAddressResponse.getWallet());
                            defaultWalletBalance = Integer.parseInt(MyUtility.getSavedPreferences(ProductBuyActivity.this, "wallet_balance"));
                            strPayPal = MyUtility.getSavedPreferences(ProductBuyActivity.this, "payPalEmail");
                            if (!strPayPal.equals("")) {
                                txtDisplayPayment.setText("Paypal");
                                ivPaypal.setVisibility(View.VISIBLE);
                            }
                            walletBalance = defaultWalletBalance;


                            grantTotal = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                            customTextView(tvReviewTotalAmt);
                            tvShippingCharge.setText("Shipping Charge  $" + shippingRate);

                            if (!txtDisplayAddress.getText().toString().equals("") && !txtDisplayPayment.getText().toString().equals("")) {
                                purchaseDarkBtn();
                            } else {
                                purchaseTransBtn();
                            }

                        }


                    } else {

                        grantTotal = Integer.parseInt(productPrice);
                        shippingRate = "0";
                        tvShippingCharge.setText("Shipping Charge  $" + shippingRate);
                        strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                        customTextView(tvReviewTotalAmt);
                        MyUtility.savePreferences(ProductBuyActivity.this, "payPalEmail", ViewAddressResponse.getPaypalEmail());
                        strPayPal = MyUtility.getSavedPreferences(ProductBuyActivity.this, "payPalEmail");
                        if (!strPayPal.equals("")) {
                            txtDisplayPayment.setText("Paypal");
                            ivPaypal.setVisibility(View.VISIBLE);
                        }
                        if (!txtDisplayAddress.getText().toString().equals("") && !txtDisplayPayment.getText().toString().equals("")) {
                            purchaseDarkBtn();
                        } else {
                            purchaseTransBtn();
                        }


                    }
                }
            }

            @Override
            public void onFailure(Call<ViewAddressResponse> call, Throwable t) {
                call.cancel();
                pd.dismiss();
                Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void AddNewAddressByServer(BottomSheetDialog mBottomSheetDialog) {
        stFullname = edtFullName.getText().toString();
        stPhoneNo = edtPhoneNo.getText().toString();
        stAddress = edtAddress.getText().toString();
        stEdtUnit = edtApiUnit.getText().toString();
        stCity = edtCity.getText().toString();
        stPost = edtPost.getText().toString();

        if (stFullname.equals("")) {
            dialog(getString(R.string.pls_enter));
        } else if (stPhoneNo.equals("")) {
            dialog(getString(R.string.pls_enter_phono));
        } else if (stAddress.equals("")) {
            dialog(getString(R.string.pls_enter_address));
        } else if (stEdtUnit.equals("")) {
            dialog(getString(R.string.pls_enter_unit));
        } else if (stCity.equals("")) {
            dialog(getString(R.string.pls_enter_city));
        } else if (stPost.equals("")) {
            dialog(getString(R.string.pls_enter_postcode));
        } else {
            Map<String, String> credMap = new HashMap<>();
            credMap.put("user_id", String.valueOf(userId));
            credMap.put("full_name", stFullname);
            credMap.put("phone", stPhoneNo);
            credMap.put("address", stAddress);
            credMap.put("apt_unit", stEdtUnit);
            credMap.put("city", stCity);
            credMap.put("state", stState);
            credMap.put("country", stCountry);
            credMap.put("postal_code", stPost);

            Call<StoreAddressResponse> storeAddressResponsecall = service.storeAddress(credMap);
            storeAddressResponsecall.enqueue(new Callback<StoreAddressResponse>() {
                @Override
                public void onResponse(Call<StoreAddressResponse> call, Response<StoreAddressResponse> response) {
                    StoreAddressResponse storeAddressResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d("onResponse", "" + response.body().getMessage());
                        storeAddressList.clear();
                        storeAddressList = storeAddressResponse.getStoreAddressList();
                        // Neww
                        for (int i = 0; i < storeAddressList.size(); i++) {
                            if (storeAddressList.get(i).getDefault_addres().equals("1")) {
                                billing_Address_ID = storeAddressList.get(i).getId();
                                shippingRate = storeAddressList.get(i).getShippingRate();
                                txtDisplayAddress.setText(storeAddressList.get(i).getApiUnit() + "," + storeAddressList.get(i).getAddress());

                            }
                            grantTotal = Integer.parseInt(productPrice) + Integer.parseInt(shippingRate);
                            strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                            customTextView(tvReviewTotalAmt);
                            tvShippingCharge.setText("Shipping Charge  $" + shippingRate);
                        }
                        //
                        if (!txtDisplayAddress.getText().toString().equals("") && !txtDisplayPayment.getText().toString().equals("")) {
                            purchaseDarkBtn();
                        } else {
                            purchaseTransBtn();
                        }

                        mAddressAdapter.clear();
                        mAddressAdapter.addAll(storeAddressList);

                    }
                }

                @Override
                public void onFailure(Call<StoreAddressResponse> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("onFailure", t.toString());
                }
            });
            mBottomSheetDialog.dismiss();
        }
    }

    private void selectDefaultAddressByServer(int addId) {
        Map<String, String> credMap = new HashMap<>();
        credMap.put("address_id", String.valueOf(addId));
        credMap.put("user_id", userId);

        Call<ViewAddressResponse> storeAddressResponsecall = service.setDefaultAddress(credMap);
        storeAddressResponsecall.enqueue(new Callback<ViewAddressResponse>() {
            @Override
            public void onResponse(Call<ViewAddressResponse> call, Response<ViewAddressResponse> response) {
                ViewAddressResponse ViewAddressResponse = response.body();
                if (response.isSuccessful()) {
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (ViewAddressResponse.getStatus() == 1) {
                        if (ViewAddressResponse.getStoreAddressList().size() > 0) {
                            storeAddressList = ViewAddressResponse.getStoreAddressList();
                            for (int i = 0; i < storeAddressList.size(); i++) {
                                if (storeAddressList.get(i).getDefault_addres().equals("1")) {
                                    billing_Address_ID = storeAddressList.get(i).getId();
                                    txtDisplayAddress.setText(storeAddressList.get(i).getApiUnit() + "," + storeAddressList.get(i).getAddress());
                                    if (!txtDisplayAddress.getText().toString().equals("")) {
                                        purchaseDarkBtn();
                                    }
                                }
                            }
                            mAddressAdapter.clear();
                            mAddressAdapter.isEmpty();
                            mAddressAdapter.addAll(storeAddressList);

                        }
                    } else {
                        Toast.makeText(ProductBuyActivity.this, "" + ViewAddressResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ViewAddressResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }

    private void deleteAddressByServer(int addId) {
        Map<String, String> credMap = new HashMap<>();
        credMap.put("address_id", String.valueOf(addId));

        Call<DeleteAddreesResponse> deletAddress = service.deletAddress(credMap);
        deletAddress.enqueue(new Callback<DeleteAddreesResponse>() {
            @Override
            public void onResponse(Call<DeleteAddreesResponse> call, Response<DeleteAddreesResponse> response) {
                DeleteAddreesResponse addreesResponse = response.body();
                if (response.isSuccessful()) {
                    storeAddressList.clear();
                    storeAddressList = addreesResponse.getStoreAddressList();
                    if (storeAddressList.size() == 0) {
                        txtDisplayAddress.setText("");
                        shippingRate = "0";
                        grantTotal = Integer.parseInt(productPrice);
                        tvShippingCharge.setText("Shipping Charge  $" + 0);
                        grantTotal = Integer.parseInt(productPrice);
                        strDisplayPrice = "$ " + String.valueOf(grantTotal) + " USD (With Shipping)";
                        customTextView(tvReviewTotalAmt);
                        if (txtDisplayAddress.getText().toString().equals("")) {
                            purchaseTransBtn();
                        }

                    } else {
                        for (int i = 0; i < storeAddressList.size(); i++) {
                            if (storeAddressList.get(i).getDefault_addres().equals("1")) {
                                billing_Address_ID = storeAddressList.get(i).getId();
                            } else {
                                txtDisplayAddress.setText("");
                                if (txtDisplayAddress.getText().toString().equals("")) {
                                    purchaseTransBtn();
                                }
                            }
                        }
                    }
                    mAddressAdapter.clear();
                    mAddressAdapter.addAll(storeAddressList);
                }
            }

            @Override
            public void onFailure(Call<DeleteAddreesResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


    private void editAddressByserver(String addId, String full_name, String phone,
                                     final String address, final String apt_unit
            , String city, String state, String country, String postal_code) {
        Map<String, String> credMap = new HashMap<>();
        credMap.put("address_id", String.valueOf(addId));
        credMap.put("full_name", full_name);
        credMap.put("phone", phone);
        credMap.put("address", address);
        credMap.put("apt_unit", apt_unit);
        credMap.put("city", city);
        credMap.put("state", state);
        credMap.put("country", country);
        credMap.put("postal_code", postal_code);

        Call<EditAddressResponse> deletAddress = service.editAddress(credMap);
        mRelativeLayout.setVisibility(View.VISIBLE);

        deletAddress.enqueue(new Callback<EditAddressResponse>() {
            @Override
            public void onResponse(Call<EditAddressResponse> call, Response<EditAddressResponse> response) {
                EditAddressResponse addreesResponse = response.body();
                if (response.isSuccessful()) {
                    mRelativeLayout.setVisibility(View.GONE);
                    storeAddressList.clear();
                    storeAddressList = addreesResponse.getStoreAddressList();
                    mAddressAdapter.clear();
                    mAddressAdapter.addAll(storeAddressList);
                    txtDisplayAddress.setText(apt_unit + "," + address);

//                    listAddressByServer();
                }
            }

            @Override
            public void onFailure(Call<EditAddressResponse> call, Throwable t) {
                call.cancel();
                mRelativeLayout.setVisibility(View.GONE);
                Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


    private void payMentBottomSheetDialog() {
        ImageView ivBackPressCloseDialog;
        final EditText edtPayPalEmail;
        final TextView txtSavePayment;
        final RelativeLayout mRelativeLayoutPayment;
        Button btnCreditCard;
        View viewPayment = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_ship_payment, null);
        edtPayPalEmail = viewPayment.findViewById(R.id.edt_paypal);
        mRelativeLayoutPayment = viewPayment.findViewById(R.id.loadItemsLayout_listView);
        txtSavePayment = viewPayment.findViewById(R.id.txtSave);


        btnCreditCard = viewPayment.findViewById(R.id.btnCreditCard);
        ivBackPressCloseDialog = viewPayment.findViewById(R.id.iv_dialogBack);
        mBottomSheetPaymentDialog.setCancelable(false);
        mBottomSheetPaymentDialog.setContentView(viewPayment);
        mBottomSheetPaymentDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        ivBackPressCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetPaymentDialog.dismiss();
            }
        });

        String edt = MyUtility.getSavedPreferences(ProductBuyActivity.this, "payPalEmail");
        edtPayPalEmail.setText(edt);
        edtPayPalEmail.setSelection(edt.length());
        txtSavePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUtility.hideKeyboard(ProductBuyActivity.this);
                if (type.equals("vendorseller")) {
                    String edt = edtPayPalEmail.getText().toString();
                    if (!edt.equals("")) {
                        chagePaypalEmailVendorSeller(txtSavePayment, mRelativeLayoutPayment, edtPayPalEmail);
                    }
                } else {
                    chagePaypalEmailVendorSeller(txtSavePayment, mRelativeLayoutPayment, edtPayPalEmail);
                }
            }
        });

      /*  btnCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView ivCloseDialog;
                Button btnBillAddress, btnDone;
                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ProductBuyActivity.this);
                View viewaddress = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_card_address, null);
                ivCloseDialog = viewaddress.findViewById(R.id.iv_dialogBack);
                btnBillAddress = viewaddress.findViewById(R.id.btnBillingAddress);
                btnDone = viewaddress.findViewById(R.id.btnDone);
                mBottomSheetDialog.setCancelable(false);
                mBottomSheetDialog.setContentView(viewaddress);
                ivCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                    }
                });
                btnDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mBottomSheetDialog.dismiss();
                    }
                });
                btnBillAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageView ivCloseDialog;
                        Button btnClose;
                        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ProductBuyActivity.this);
                        View viewaddress = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_billing_address, null);
                        ivCloseDialog = viewaddress.findViewById(R.id.iv_dialogBack);
                        btnClose = viewaddress.findViewById(R.id.btnDone);
                        mBottomSheetDialog.setCancelable(false);
                        mBottomSheetDialog.setContentView(viewaddress);
                        ivCloseDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBottomSheetDialog.dismiss();
                            }
                        });
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mBottomSheetDialog.dismiss();
                            }
                        });
                        mBottomSheetDialog.show();
                    }
                });
                mBottomSheetDialog.show();
            }
        });*/

        mBottomSheetPaymentDialog.show();

    }


    private void orderPlaceByServer() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtPurchase.setVisibility(View.INVISIBLE);


        ApiProduct service1 = ApiClient.getClient().create(ApiProduct.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("product_id", productId);
        credMap.put("user_id", userId);
        credMap.put("user_seller_id", userSellerId);
        credMap.put("seller_id", sellerId);
        credMap.put("billing_add_id", String.valueOf(billing_Address_ID));
        credMap.put("shipping_add_id", String.valueOf(billing_Address_ID));
        credMap.put("total", productPrice);
        credMap.put("shipping_rate", shippingRate);
        credMap.put("used_wallet_balance", String.valueOf(usedWalletBalance));
        credMap.put("grand_total", String.valueOf(grantTotal));
        credMap.put("size_id", size);
        credMap.put("exp_in", exp_in);
        credMap.put("payment_method", "paypal");


        Call<OrderResponse> call = service1.PrdouctOrder(credMap);
        call.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {

                if (response.isSuccessful()) {
                    OrderResponse orderResponse = ((OrderResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtPurchase.setVisibility(View.VISIBLE);
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (orderResponse.getStatus() == 1) {
                        MyUtility.savePreferences(ProductBuyActivity.this, "wallet_balance", orderResponse.getUserWallletBalance());
                        Toast.makeText(ProductBuyActivity.this, "" + orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent
                                = new Intent(ProductBuyActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    } else {
                        Toast.makeText(ProductBuyActivity.this, "" + orderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                mRelativeLayout.setVisibility(View.GONE);
                txtPurchase.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(ProductBuyActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

    public void custome_deleteDialog(final int id) {
        final TextView dialogMsg;
        final Button btnYes, btnNo;
        final Dialog validationDialog = new Dialog(ProductBuyActivity.this);
        validationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        validationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        validationDialog.setContentView(R.layout.custom_dialog_delete_address);
        validationDialog.setCanceledOnTouchOutside(true);
        validationDialog.setCancelable(false);
        btnNo = validationDialog.findViewById(R.id.btnNo);
        btnYes = validationDialog.findViewById(R.id.btnYes);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationDialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddressByServer(id);
                validationDialog.dismiss();

            }
        });
        validationDialog.show();
    }


    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {


        public List<StoreAddress> addressList;
        private Context context;


        public AddressAdapter(ProductBuyActivity productBuyActivity) {
            this.context = productBuyActivity;
            this.addressList = new ArrayList<>();
        }


        @Override
        public AddressAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dialog_rv_addresslist, parent, false);
            AddressAdapter.MyViewHolder myViewHolder = new AddressAdapter.MyViewHolder(view);

            return myViewHolder;


        }

        @Override
        public void onBindViewHolder(AddressAdapter.MyViewHolder holder, final int position) {
            final StoreAddress storeAddress = addressList.get(position);
            holder.txtFulllName.setText(storeAddress.getFullName());
            holder.txtAddress.setText(storeAddress.getAddress());
            holder.txtAddress1.setText(storeAddress.getCity() + "," + storeAddress.getCountry() + "," + storeAddress.getPostal_code());
            if (storeAddress.getDefault_addres().toString().equals("1")) {
                holder.ivCheck.setVisibility(View.VISIBLE);
            } else {
                holder.ivCheck.setVisibility(View.INVISIBLE);
            }

            holder.rlSetDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectDefaultAddressByServer(storeAddressList.get(position).getId());
                    mListAddressDialog.dismiss();
                }
            });
            holder.lldelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    custome_deleteDialog(storeAddress.getId());
                }
            });

            holder.llEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int address_id = addressList.get(position).getId();
                    Button btnDone;
                    ImageView ivCloseDialog;
                    View viewaddress = ProductBuyActivity.this.getLayoutInflater().inflate(R.layout.dialog_newaddress, null);
                    mAddressUpdate.setCancelable(false);
                    mAddressUpdate.setContentView(viewaddress);
                    btnDone = viewaddress.findViewById(R.id.btnDone);
                    edtFullName = (EditText) viewaddress.findViewById(R.id.edtFullName);
                    edtPhoneNo = (EditText) viewaddress.findViewById(R.id.edtPhono);
                    edtAddress = (EditText) viewaddress.findViewById(R.id.edtAddress);
                    edtApiUnit = (EditText) viewaddress.findViewById(R.id.edtApi);
                    edtCity = (EditText) viewaddress.findViewById(R.id.edtCity);
                    edtPost = (EditText) viewaddress.findViewById(R.id.edtpost);
                    spinner = (AppCompatSpinner) viewaddress.findViewById(R.id.edtcountry);
                    stState = "";
                    mSpinerAdapter = new CustomStateAdapter(getApplicationContext(), countryNames);
                    spinner.setAdapter(mSpinerAdapter);
                    for (int i = 0; i < countryNames.length; i++) {
                        if (addressList.get(position).getCountry().equals(countryNames[i])) {
                            spinner.setSelection(i);
                            stCountry = countryNames[i];
                        }
                    }

                    edtFullName.setText(addressList.get(position).getFullName());
                    edtPhoneNo.setText(addressList.get(position).getPhoneNo());
                    edtAddress.setText(addressList.get(position).getAddress());
                    edtApiUnit.setText(addressList.get(position).getApiUnit());
                    edtCity.setText(addressList.get(position).getCity());
                    edtPost.setText(addressList.get(position).getPostal_code());

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> adapterView, View view,
                                int i, long l) {
                            stCountry = spinner.getItemAtPosition(i).toString();
                        }

                        public void onNothingSelected(
                                AdapterView<?> adapterView) {

                        }
                    });

                    ivCloseDialog = viewaddress.findViewById(R.id.iv_dialogBack);
                    ivCloseDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAddressUpdate.dismiss();
                        }
                    });
                    btnDone.setText("Update");
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            editAddressByserver(String.valueOf(addressList.get(position).getId()),
                                    edtFullName.getText().toString(),
                                    edtPhoneNo.getText().toString(),
                                    edtAddress.getText().toString(),
                                    edtApiUnit.getText().toString(),
                                    edtCity.getText().toString(),
                                    "",
                                    stCountry,
                                    edtPost.getText().toString()
                            );
                            mAddressUpdate.dismiss();

                        }
                    });
                    mAddressUpdate.show();
                }
            });

        }


        @Override
        public int getItemCount() {
            return addressList.size();
        }



   /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

        public void add(StoreAddress address) {
            addressList.add(address);
            notifyItemInserted(addressList.size() - 1);
        }

        public void addAll(List<StoreAddress> storeAddresses) {
            for (StoreAddress result : storeAddresses) {
                add(result);
            }
        }

        public void remove(StoreAddress r) {
            int position = addressList.indexOf(r);
            if (position > -1) {
                addressList.remove(position);
                notifyItemRemoved(position);
            }
        }

        public void clear() {
            while (getItemCount() > 0) {
                remove(getItem(0));
            }
        }

        public boolean isEmpty() {
            return getItemCount() == 0;
        }


        public StoreAddress getItem(int position) {
            return addressList.get(position);
        }

        /**
         * Displays Pagination retry footer view along with appropriate errorMsg
         *
         * @param show
         * @param errorMsg to display if page load fails
         */


        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView txtFulllName, txtAddress, txtAddress1;
            public ImageView ivCheck;
            public LinearLayout rlSetDefault;
            public LinearLayout llEdit, lldelete;

            public MyViewHolder(View itemView) {
                super(itemView);
                txtFulllName = (TextView) itemView.findViewById(R.id.txtFullName);
                txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
                txtAddress1 = (TextView) itemView.findViewById(R.id.txtAddress1);

                ivCheck = itemView.findViewById(R.id.iv_check);
                rlSetDefault = itemView.findViewById(R.id.rlcheck);
                llEdit = itemView.findViewById(R.id.lledit);
                lldelete = itemView.findViewById(R.id.lldelete);

            }
        }


    }

    private class CustomStateAdapter extends BaseAdapter {
        Context context;
        String[] countryNames;

        public CustomStateAdapter(Context applicationContext, String[] countryNames) {
            this.context = applicationContext;
            this.countryNames = countryNames;
        }

        @Override
        public int getCount() {
            return countryNames.length;
        }

        @Override
        public Object getItem(int position) {
            return countryNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.state_adapter_layout, null);

            TextView textView = convertView.findViewById(R.id.txtSpnStateName);
            textView.setText(countryNames[position]);

            return convertView;
        }
    }

    public void dialog(String msg) {
        final TextView dialogMsg;
        final Button btnOk;
        final Dialog validationDialog = new Dialog(ProductBuyActivity.this);
        validationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        validationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        validationDialog.setContentView(R.layout.custom_dialog_validation);
        validationDialog.setCanceledOnTouchOutside(true);
        validationDialog.setCancelable(false);
        dialogMsg = validationDialog.findViewById(R.id.txtMessage);
        btnOk = validationDialog.findViewById(R.id.btn);
        dialogMsg.setText(msg);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validationDialog.dismiss();
            }
        });
        validationDialog.show();
    }

    public boolean validate(EditText edtPayPalEmail) {
        boolean valid = true;
        if (edtPayPalEmail.getText().toString().equals("")) {
            edtPayPalEmail.setError(getString(R.string.valid_user_name));
            valid = false;
            requestFocus(edtPayPalEmail);
        } else if (edtPayPalEmail.getText().toString().contains("@")) {
            if (!myUtility.isValidEmail(edtPayPalEmail.getText().toString())) {
                valid = false;
                edtPayPalEmail.setError(getString(R.string.valid_email_address));
                requestFocus(edtPayPalEmail);
            }
        }
        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    // vendor

    private void chagePaypalEmailVendorSeller(final TextView txtSavePayment, final RelativeLayout mRelativeLayoutPayment, EditText edtPayPalEmail) {
        mRelativeLayoutPayment.setVisibility(View.VISIBLE);
        txtSavePayment.setVisibility(View.GONE);

        String email = edtPayPalEmail.getText().toString();
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("paypal_email", email);

        Call<VendorResponse> call = service.changeVendorPayPalEmail(credMap);
        call.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {

                if (response.isSuccessful()) {
                    VendorResponse vendorResponse = ((VendorResponse) response.body());
                    mRelativeLayoutPayment.setVisibility(View.GONE);
                    txtSavePayment.setVisibility(View.VISIBLE);
                    if (vendorResponse.getStatus() == 1) {
                        MyUtility.savePreferences(ProductBuyActivity.this, "payPalEmail", vendorResponse.getData().getPayPalEmail());
                        strPayPal = MyUtility.getSavedPreferences(ProductBuyActivity.this, "payPalEmail");
                        if (!strPayPal.equals("")) {
                            txtDisplayPayment.setText("Paypal");
                            ivPaypal.setVisibility(View.VISIBLE);
                        } else {
                            txtDisplayPayment.setText("");
                            ivPaypal.setVisibility(View.GONE);
                        }

                        if (!txtDisplayAddress.getText().toString().equals("") && !txtDisplayPayment.getText().toString().equals("")) {
                            purchaseDarkBtn();
                        } else {
                            purchaseTransBtn();
                        }


                        mBottomSheetPaymentDialog.dismiss();
                    } else {
                        Toast.makeText(ProductBuyActivity.this, "" + vendorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                // Log error here since request failed
                mRelativeLayoutPayment.setVisibility(View.GONE);
                txtSavePayment.setVisibility(View.VISIBLE);
                call.cancel();
            }
        });
    }


}
