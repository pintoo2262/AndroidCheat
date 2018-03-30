package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.PersonInfoActivity;
import com.app.noan.activity.PersonInfoReturnaddressActivity;
import com.app.noan.activity.SellerActivity;
import com.app.noan.activity.SocialAccountActivity;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.model.SellerModel;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanjay on 1/1/18.
 */

@SuppressLint("ValidFragment")
public class Request_sell_Fragment extends Fragment implements View.OnClickListener {

    String Tag = "ReuestToSell";
    private TextView txtPersonInfo, txtPersonAddrees, txtSeller, testSocialLink;
    private CheckBox chkRequestSell;
    private Button btnSubmit;
    RelativeLayout mRelativeLayout;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    String intentFullName, intentMobileNo, intentDateofBirth, intentAddress1, intentAddress2, intentSeller, intentFblink, intentInlink, intentTwlink, userId;
    Activity activity;
    FragmentActivity fragmentActivity;
    String isUserSeller, type;

    public Request_sell_Fragment() {
    }

    public Request_sell_Fragment(Activity activity, FragmentActivity activity1) {
        this.activity = activity;
        fragmentActivity = activity1;
    }


    public static Fragment newInstance() {
        return new RequestToSellFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.request_to_seller, container, false);
        type = MyUtility.getSavedPreferences(getActivity(), "sellerType");
        initalization(view);
        if (type.equals("unapproved")) {
            linearLayout.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.GONE);
        } else if (type.equals("unapprove")) {
            linearLayout.setVisibility(View.GONE);
            constraintLayout.setVisibility(View.VISIBLE);
        }


        txtPersonInfo.setOnClickListener(this);

        Log.d(Tag, "oncreate");


        return view;
    }


    private void initalization(View view) {

        constraintLayout = view.findViewById(R.id.constrianlayoutRequesttoSell);
        linearLayout = view.findViewById(R.id.linearLayout);

        txtPersonInfo = (TextView) view.findViewById(R.id.txt_Persion_info);
        txtPersonAddrees = (TextView) view.findViewById(R.id.txt_Persion_addres);
        txtSeller = (TextView) view.findViewById(R.id.txt_Persion_sell);
        testSocialLink = (TextView) view.findViewById(R.id.txt_Persion_sociallink);
        btnSubmit = (Button) view.findViewById(R.id.btn_request_sumbitsell);
        chkRequestSell = (CheckBox) view.findViewById(R.id.ch_requestSell);
        mRelativeLayout = (RelativeLayout) view.findViewById(R.id.loadItemsLayout_listView);


        chkRequestSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (chkRequestSell.isChecked()) {
                    MyUtility.onResumeSellerScreenType = 2;
                    checkIAgreeByServer();
                    txtPersonInfo.setTextColor(getResources().getColor(R.color.btn_bgwhite));
                    chkRequestSell.setEnabled(false);
                    txtPersonInfo.setEnabled(true);
                    txtPersonInfo.setClickable(true);
                    txtPersonInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);
                }
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_Persion_info:
                Intent intent
                        = new Intent(getActivity(), PersonInfoActivity.class);
                intent.putExtra("IntetFullName", intentFullName);
                intent.putExtra("IntetMobile", intentMobileNo);
                intent.putExtra("intentDateOfBirth", intentDateofBirth);
                startActivityForResult(intent, 1);
                break;

            case R.id.txt_Persion_addres:
                Intent intent1
                        = new Intent(getActivity(), PersonInfoReturnaddressActivity.class);
                intent1.putExtra("IntetAddress1", intentAddress1);
                intent1.putExtra("IntetAddress2", intentAddress2);
                startActivityForResult(intent1, 2);
                break;
            case R.id.txt_Persion_sell:
                Intent intent2
                        = new Intent(getActivity(), SellerActivity.class);
                intent2.putExtra("IntetSeller", intentSeller);
                startActivityForResult(intent2, 3);
                break;

            case R.id.txt_Persion_sociallink:
                Intent intent4
                        = new Intent(getActivity(), SocialAccountActivity.class);
                intent4.putExtra("fbLink", intentFblink);
                intent4.putExtra("inLink", intentInlink);
                intent4.putExtra("twLink", intentTwlink);
                startActivityForResult(intent4, 4);
                break;
            case R.id.btn_request_sumbitsell:
                submitByServer();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (MyUtility.onResumeScreenType == 3) {
            if (MyUtility.onResumeSellerScreenType == 2) {
                personalInfoRequestByServer();
                Log.d(Tag, "onResume");
            }
        }
    }


    private void checkIAgreeByServer() {
        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("termsagree", "true");
        credMap.put("full_name", "");
        credMap.put("phone_number", "");
        credMap.put("birth_date", "");
        credMap.put("address", "");
        credMap.put("seller_info", "");
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "terms_agree");

        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {

                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    txtPersonInfo.setClickable(true);
                }
            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(Tag, t.toString());
                call.cancel();
                Toast.makeText(getActivity(), "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

    private void submitByServer() {
        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", MyUtility.getSavedPreferences(getActivity(), "id"));
        credMap.put("termsagree", "");
        credMap.put("full_name", "");
        credMap.put("phone_number", "");
        credMap.put("birth_date", "");
        credMap.put("address", "");
        credMap.put("seller_info", "");
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "final_submit");

        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    if (sellerModelResponse.getStatus() == 1) {
                        MyUtility.onResumeSellerScreenType = 2;
                        if (sellerModelResponse.getData().getVerifiedSaller().equals("unapproved")) {
                            linearLayout.setVisibility(View.VISIBLE);
                            constraintLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(Tag, t.toString());
                call.cancel();
                Toast.makeText(getActivity(), "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


    private void personalInfoRequestByServer() {
        final Dialog pDialog = new Dialog(activity);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        if (pDialog != null && !pDialog.isShowing())
            pDialog.show();

        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("termsagree", "");
        credMap.put("full_name", "");
        credMap.put("phone_number", "");
        credMap.put("birth_date", "");
        credMap.put("address1", "");
        credMap.put("address2", "");
        credMap.put("seller_info", "");
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "");

        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    if (sellerModelResponse.getStatus() == 1) {
                        SellerModel sellerModel = sellerModelResponse.getData();
                        if (sellerModel != null) {
                            if (sellerModel.getVerifiedSaller() != null) {
                                if (sellerModel.getVerifiedSaller().equals("approved")) {
                                    MyUtility.savePreferences(activity, "sellerType", "isUserSell");
                                    isUserSeller = "isUserSell";
                                    SellerFragment sellerFragment = new SellerFragment();
                                    sellerFragment.setArguments(userId, isUserSeller);
                                    FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frameRequestToSell, sellerFragment);
                                    fragmentTransaction.commit();
                                } else if (sellerModel.getVerifiedSaller().equals("unapproved")) {
                                    MyUtility.savePreferences(activity, "sellerType", "unapproved");
                                    linearLayout.setVisibility(View.VISIBLE);
                                    constraintLayout.setVisibility(View.GONE);
                                } else {
                                    selectionPerformclick(sellerModel);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(Tag, t.toString());
                call.cancel();
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
                Toast.makeText(getActivity(), "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


    private void selectionPerformclick(SellerModel sellerModel) {

        if (sellerModel.getTermsAgree() != null && !sellerModel.getTermsAgree().equals("")) {
            chkRequestSell.setChecked(true);
            txtPersonInfo.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            txtPersonInfo.setOnClickListener(this);
            txtPersonInfo.setClickable(true);
            txtPersonInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);

        }
        if (sellerModel.getFullName() != null && !sellerModel.getFullName().equals("")) {
            intentFullName = sellerModel.getFullName();
            intentMobileNo = sellerModel.getPhoneNumber();
            intentDateofBirth = sellerModel.getBirthDate();
            txtPersonAddrees.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            txtPersonAddrees.setOnClickListener(this);

            txtPersonInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
            txtPersonAddrees.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);
        }
        if (sellerModel.getAddress1() != null && !sellerModel.getAddress1().equals("")) {
            intentAddress1 = sellerModel.getAddress1();
            intentAddress2 = sellerModel.getAddress2();
            txtSeller.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            txtSeller.setOnClickListener(this);

            txtPersonAddrees.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
            txtSeller.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);


        }
        if (sellerModel.getSellerInfo() != null && !sellerModel.getSellerInfo().equals("")) {
            intentSeller = sellerModel.getSellerInfo();
            testSocialLink.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            testSocialLink.setOnClickListener(this);
            txtSeller.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
            testSocialLink.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);

        }


        if (sellerModel.getFbLink() != null && !sellerModel.getFbLink().equals("") || sellerModel.getTwLink() != null && !sellerModel.getTwLink().equals("") || sellerModel.getInLink() != null && !sellerModel.getInLink().equals("")) {

            intentFblink = sellerModel.getFbLink();
            intentInlink = sellerModel.getInLink();
            intentTwlink = sellerModel.getTwLink();

            btnSubmit.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            btnSubmit.setOnClickListener(this);
            testSocialLink.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);

        }
    }


    public void setUserVisible() {
        Log.d(Tag, "visible");
        personalInfoRequestByServer();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            if (null != data) {
                txtPersonAddrees.setOnClickListener(this);
                txtPersonAddrees.setTextColor(getResources().getColor(R.color.btn_bgwhite));

                txtPersonInfo.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
                txtPersonAddrees.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);
            }

        } else if (requestCode == 2) {
            if (null != data) {
                // fetch the message String
                txtSeller.setOnClickListener(this);
                txtSeller.setTextColor(getResources().getColor(R.color.btn_bgwhite));

                txtPersonAddrees.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
                txtSeller.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);
            }

        } else if (requestCode == 3) {
            testSocialLink.setOnClickListener(this);
            testSocialLink.setTextColor(getResources().getColor(R.color.btn_bgwhite));


            txtSeller.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);
            testSocialLink.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.plush_btn, 0);

        } else if (requestCode == 4) {
            btnSubmit.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            btnSubmit.setOnClickListener(this);
            testSocialLink.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tickmark, 0);

        }
    }

    public void setArguments(String userId) {
        this.userId = userId;
    }
}

