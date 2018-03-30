package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.CheckEmailResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationStep1Activity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = RegistrationStep1Activity.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView txtCondition, mtoolbarTitle, txtStep2;
    private RadioButton rbCheck;
    private EditText edtEmailAddress, edtPassword, edtConfirmPassword, edtMobileNo;
    private String strEmail, strPassword, strConfitmPassword, strMobileNo;
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
        setContentView(R.layout.activity_registration);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();


        // set custom link in textview
        customTextView(txtCondition);


    }

    private void initialization() {
        myUtility = new MyUtility(RegistrationStep1Activity.this);
        txtCondition = (TextView) findViewById(R.id.txt_condition);
        rbCheck = (RadioButton) findViewById(R.id.rb_condition);
        edtEmailAddress = (EditText) findViewById(R.id.edt_Email);
        txtStep2 = (TextView) findViewById(R.id.txtStep2);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm);
        edtMobileNo = (EditText) findViewById(R.id.edt_mobile);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);


        TextWatcher textWatcher = new TextWatcher() {
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
        edtEmailAddress.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);
        edtConfirmPassword.addTextChangedListener(textWatcher);
        edtMobileNo.addTextChangedListener(textWatcher);

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
        mtoolbarTitle.setText(getResources().getString(R.string.registrationToolbar));

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
        myUtility.hideKeyboard(this);
        checkEmail();


    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getResources().getString(R.string.termcondition));
        spanTxt.append(" " + getResources().getString(R.string.termcondition1));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent
                        = new Intent(RegistrationStep1Activity.this, TermAndConditionsActivity.class);
                startActivity(intent);
            }
        }, spanTxt.length() - getResources().getString(R.string.termcondition1).length(), spanTxt.length(), 0);
        spanTxt.append(" " + getResources().getString(R.string.termcondition2));
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
        spanTxt.append(" " + getResources().getString(R.string.termcondition3));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(RegistrationStep1Activity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }

        }, spanTxt.length() - getResources().getString(R.string.termcondition3).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    private void checkFieldsForEmptyValues() {
        txtStep2.setOnClickListener(this);
        strEmail = edtEmailAddress.getText().toString();
        strPassword = edtPassword.getText().toString();
        strConfitmPassword = edtConfirmPassword.getText().toString();
        strMobileNo = edtMobileNo.getText().toString();

        if (strEmail.length() > 4 && strPassword.length() > 4 && strConfitmPassword.length() > 4 && strMobileNo.length() > 4) {
            singupDark();
        } else {
            singupTrans();
        }
    }


    public void signup() {
        Log.d(TAG, "Signup");

        if (validate() == false) {
//            onSignupFailed();
            return;
        }

        saveToPassingData();

    }

    private void saveToPassingData() {
        Intent intent
                = new Intent(this, RegistrationStep2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", edtEmailAddress.getText().toString());
        bundle.putString("password", edtPassword.getText().toString());
        bundle.putString("mobile", edtMobileNo.getText().toString());
        intent.putExtras(bundle);
        MyUtility.savePreferences(RegistrationStep1Activity.this, "password", edtPassword.getText().toString());
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        String email = edtEmailAddress.getText().toString();
        String password = edtPassword.getText().toString();
        String reEnterPassword = edtConfirmPassword.getText().toString();
        String mobile = edtMobileNo.getText().toString();


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailAddress.setError(getString(R.string.valid_email_address));
            valid = false;
        } else {
            edtEmailAddress.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtPassword.setError(getString(R.string.valid_password));
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        if (!password.isEmpty()) {
            if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
                edtConfirmPassword.setError(getString(R.string.valid_paswordmatch));
                valid = false;
            } else {
                edtConfirmPassword.setError(null);
            }
        }


        if (mobile.isEmpty()) {
            edtMobileNo.setError(getString(R.string.valid_mobileno));
            valid = false;
        } else {
            edtPassword.setError(null);
        }
        if (!rbCheck.isChecked()) {
            valid = false;
            Toast.makeText(this, "You must agree with the Terms and Conditions", Toast.LENGTH_SHORT).show();
        } else {

        }

        return valid;
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
        txtStep2.setEnabled(true);
    }

    // Validation
    public boolean isValidMobile(String mobile) {
        String phone = "[0-9]{10}";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public void singupTrans() {
        txtStep2.setEnabled(false);
        txtStep2.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtStep2.setTextColor(getResources().getColor(R.color.black));
    }

    public void singupDark() {
        txtStep2.setEnabled(true);
        txtStep2.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtStep2.setTextColor(getResources().getColor(R.color.white));
    }

    private void checkEmail() {

        mRelativeLayout.setVisibility(View.VISIBLE);
        txtStep2.setVisibility(View.GONE);

        String email = edtEmailAddress.getText().toString();

        APILogin service = ApiClient.getClient().create(APILogin.class);


        Call<CheckEmailResponse> call = service.userCheckEmail(email);
        call.enqueue(new Callback<CheckEmailResponse>() {
            @Override
            public void onResponse(Call<CheckEmailResponse> call, Response<CheckEmailResponse> response) {

                if (response.isSuccessful()) {
                    CheckEmailResponse checkEmailResponse = ((CheckEmailResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtStep2.setVisibility(View.VISIBLE);
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (checkEmailResponse.getStatus() == 1) {
                        signup();
                    } else {
                        Toast.makeText(RegistrationStep1Activity.this, "" + checkEmailResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<CheckEmailResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                mRelativeLayout.setVisibility(View.GONE);
                txtStep2.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(RegistrationStep1Activity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


}
