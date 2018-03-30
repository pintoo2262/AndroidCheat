package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.LoginResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Activity_OTP extends AppCompatActivity implements View.OnClickListener {


    private EditText edtOtp;
    private TextView tvTimer, tvResend, txtVerifyOtp;
    private String otp, email, id, strEnterOtp;
    TextWatcher textWatcher;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        initialization();
        timer();

        // Intent getting Id & Otp
        id = getIntent().getStringExtra("id");
        otp = getIntent().getStringExtra("OTP");
        email = getIntent().getStringExtra("Email");

        edtOtp.setText(otp);


    }

    private void initialization() {
        //Bind
        edtOtp = (EditText) findViewById(R.id.et_Otp);
        tvTimer = (TextView) findViewById(R.id.tv_Otp_timer);
        tvResend = (TextView) findViewById(R.id.tv_Otp_Resend);
        txtVerifyOtp = (TextView) findViewById(R.id.txtVerfiy);
        tvResend.setEnabled(false);
        txtVerifyOtp.setOnClickListener(this);

        //Click listener
        tvResend.setOnClickListener(this);
        //TextWatcher visiblity handling
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        //set listeners
        edtOtp.addTextChangedListener(textWatcher);


    }

    public void timer() {
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                tvTimer.setText(String.format("%02d", minutes) + " : " + String.format("%02d", seconds));
                //here you can have your logic to set text to edittext
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00" + " : " + "00");
                tvResend.setEnabled(true);
                edtOtp.setText("");
            }

        }.start();
    }

    private void checkFieldsForEmptyValues() {
        txtVerifyOtp.setOnClickListener(this);
        strEnterOtp = edtOtp.getText().toString();
        if (strEnterOtp.length() != 6) {
            otpTrans();
        } else {
            otpDark();
        }
    }


    public void otpTrans() {
        txtVerifyOtp.setEnabled(false);
        txtVerifyOtp.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtVerifyOtp.setTextColor(getResources().getColor(R.color.black));
    }

    public void otpDark() {
        txtVerifyOtp.setEnabled(true);
        txtVerifyOtp.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtVerifyOtp.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_Otp_Resend:
                edtOtp.setText("");
                otpTrans();
                timer();
                otpByServer();
                break;
            case R.id.txtVerfiy:
                MyUtility.hideKeyboard(Activity_OTP.this);
                verfiyOtpByServer();
                break;
        }
    }

    private void otpByServer() {

        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("email", email);

        Call<LoginResponse> call = service.forgotpasswordOtpgenerate(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(Activity_OTP.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (loginResponse.getUser() != null) {
                            otp = loginResponse.getUser().getOtp();
                            edtOtp.setText(otp);
                            edtOtp.setSelection(edtOtp.length());

                        }
                    } else {
                        Toast.makeText(Activity_OTP.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed

                call.cancel();
                Toast.makeText(Activity_OTP.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


    private void verfiyOtpByServer() {

        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("id", id);
        credMap.put("otp", otp);

        Call<LoginResponse> call = service.checkOtp(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(Activity_OTP.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Activity_OTP.this, AddNewPasswordActivity.class);
                        i.putExtra("id", loginResponse.getUser().getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(Activity_OTP.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed

                call.cancel();
                Toast.makeText(Activity_OTP.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


}