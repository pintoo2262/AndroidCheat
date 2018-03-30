package com.app.noan.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.PaymentActivity;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.VendorResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by smn on 20/9/17.
 */

public class PaypalFragment extends Fragment implements View.OnClickListener {
    EditText edtPaypal;
    TextView txtSave;
    String type, userId;
    RelativeLayout mRelativeLayout;
    APILogin service;
    Dialog pDialog;
    Activity mActivity;
    MyUtility myUtility;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paypal, container, false);

        service = ApiClient.getClient().create(APILogin.class);
        initilization(view);
        myUtility = new MyUtility(mActivity);
        type = MyUtility.getSavedPreferences(getActivity(), "sellerType");
        userId = MyUtility.getSavedPreferences(getActivity(), "id");


        if (type.equals("vendorseller")) {
            viewProfileByVendorSeller();
        } else {
            viewProfileByUserSeller();
        }

        return view;
    }

    private void initilization(View view) {

        mRelativeLayout = view.findViewById(R.id.loadItemsLayout_listView);
        edtPaypal = view.findViewById(R.id.edt_paypal);
        txtSave = view.findViewById(R.id.txtSave);
        txtSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSave:
                myUtility.hideKeyboard(mActivity);
                if (type.equals("vendorseller")) {
                    String edt = edtPaypal.getText().toString();
                    if (!edt.equals("")) {
                        chagePaypalEmailVendorSeller();
                    }
                } else {
                    chagePaypalEmailVendorSeller();
                }
//                paypal();


                break;
        }
    }

    private void paypal() {
        if (validate() == false) {
            return;
        } else {
            if (type.equals("vendorseller")) {
                String edt = edtPaypal.getText().toString();
                if (!edt.equals("")) {
                    chagePaypalEmailVendorSeller();
                }
            } else {
                chagePaypalEmailVendorSeller();
            }
        }
    }

    private void chagePaypalEmailVendorSeller() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtSave.setVisibility(View.GONE);

        String email = edtPaypal.getText().toString();
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("paypal_email", email);

        Call<VendorResponse> call = service.changeVendorPayPalEmail(credMap);
        call.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {

                if (response.isSuccessful()) {
                    VendorResponse vendorResponse = ((VendorResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtSave.setVisibility(View.VISIBLE);
                    if (vendorResponse.getStatus() == 1) {
                        MyUtility.savePreferences(mActivity, "payPalEmail", vendorResponse.getData().getPayPalEmail());
                        getActivity().onBackPressed();
                    } else {
                        Toast.makeText(mActivity, "" + vendorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                // Log error here since request failed
                mRelativeLayout.setVisibility(View.GONE);
                txtSave.setVisibility(View.VISIBLE);
                call.cancel();
            }
        });
    }


    private void viewProfileByVendorSeller() {
        pDialog = new Dialog(mActivity);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        Call<VendorResponse> vendorProfile = service.vendorProfile(credMap);
        vendorProfile.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    VendorResponse vendorResponse = ((VendorResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (vendorResponse.getStatus() == 1) {
                        edtPaypal.setText(vendorResponse.getData().getPayPalEmail());
                        edtPaypal.setSelection(edtPaypal.getText().length());
                    }
                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(mActivity, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


    private void viewProfileByUserSeller() {
        pDialog = new Dialog(mActivity);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("id", userId);
        Call<LoginResponse> detailResponseCall = service.userProfile(credMap);
        detailResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        edtPaypal.setText(loginResponse.getUser().getPaypalEmail());
                        edtPaypal.setSelection(edtPaypal.getText().length());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(mActivity, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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

    public void setArguments(PaymentActivity paymentActivity) {
        mActivity = paymentActivity;
    }

    public boolean validate() {
        boolean valid = true;
        if (edtPaypal.getText().toString().equals("")) {
            edtPaypal.setError(getString(R.string.valid_user_name));
            valid = false;
            requestFocus(edtPaypal);
        } else if (edtPaypal.getText().toString().contains("@")) {
            if (!myUtility.isValidEmail(edtPaypal.getText().toString())) {
                valid = false;
                edtPaypal.setError(getString(R.string.valid_email_address));
                requestFocus(edtPaypal);
            }
        }
        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
