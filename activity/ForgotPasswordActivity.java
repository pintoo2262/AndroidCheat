package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;

    EditText edtEmailAddress;
    TextView txtSubmit;
    TextWatcher textWatcher;
    private String strMobile;
    MyUtility myUtility;
    RelativeLayout mRelativeLayout;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_forgot);
        myUtility = new MyUtility(ForgotPasswordActivity.this);
        toolBarAndDrawerInitilization();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        edtEmailAddress = (EditText) findViewById(R.id.edt_Email);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);


        /*//TextWatcher visiblity handling
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
        edtEmailAddress.addTextChangedListener(textWatcher);*/
        txtSubmit.setOnClickListener(this);
        forgotDark();


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
        mtoolbarTitle.setText(getResources().getString(R.string.forogt_toolbar));
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

    public void forgotPassword() {
        if (validate() == false) {

            return;
        }
        otpByServer();


    }

    private void checkFieldsForEmptyValues() {
        txtSubmit.setOnClickListener(this);
        strMobile = edtEmailAddress.getText().toString();
        if (strMobile != null) {
            forgotTrans();
        } else {
            forgotDark();
        }
    }

    public boolean validate() {
        boolean valid = true;
        if (!myUtility.isValidEmail(edtEmailAddress.getText().toString())) {
            edtEmailAddress.setError(getString(R.string.valid_email_address));
            valid = false;
            requestFocus(edtEmailAddress);
        }


        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void forgotTrans() {
        txtSubmit.setEnabled(false);
        txtSubmit.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtSubmit.setTextColor(getResources().getColor(R.color.black));
    }

    public void forgotDark() {
        txtSubmit.setEnabled(true);
        txtSubmit.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtSubmit.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSubmit:
                forgotPassword();
                MyUtility.hideKeyboard(ForgotPasswordActivity.this);
                break;
        }
    }

    private void otpByServer() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtSubmit.setVisibility(View.GONE);

        String email = edtEmailAddress.getText().toString();

        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("email", email);

        Call<LoginResponse> call = service.forgotpasswordOtpgenerate(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtSubmit.setVisibility(View.VISIBLE);
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(ForgotPasswordActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (loginResponse.getUser() != null) {
                            Intent i = new Intent(ForgotPasswordActivity.this, Activity_OTP.class);
                            i.putExtra("OTP", loginResponse.getUser().getOtp());
                            i.putExtra("Email", edtEmailAddress.getText().toString());
                            i.putExtra("id", loginResponse.getUser().getId());
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                mRelativeLayout.setVisibility(View.GONE);
                txtSubmit.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(ForgotPasswordActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }

}
