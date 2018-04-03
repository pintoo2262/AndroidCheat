package com.example.jay.myappbasic.activity;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jay.myappbasic.R;
import com.example.jay.myappbasic.utils.MyUtility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = RegistrationActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView txtCondition, mtoolbarTitle, txtStep2;
    private RadioButton rbCheck;
    private EditText edtEmailAddress, edtPassword, edtConfirmPassword, edtMobileNo;
    private String strEmail, strPassword, strConfitmPassword, strMobileNo;
    MyUtility myUtility;



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
        myUtility = new MyUtility(RegistrationActivity.this);
        txtCondition = (TextView) findViewById(R.id.txt_condition);
        rbCheck = (RadioButton) findViewById(R.id.rb_condition);
        edtEmailAddress = (EditText) findViewById(R.id.edt_Email);
        txtStep2 = (TextView) findViewById(R.id.txtStep2);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtConfirmPassword = (EditText) findViewById(R.id.edt_confirm);
        edtMobileNo = (EditText) findViewById(R.id.edt_mobile);



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
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);
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
                /*Intent intent
                        = new Intent(RegistrationActivity.this, TermAndConditionsActivity.class);
                startActivity(intent);*/
            }
        }, spanTxt.length() - getResources().getString(R.string.termcondition1).length(), spanTxt.length(), 0);
        spanTxt.append(" " + getResources().getString(R.string.termcondition2));
        spanTxt.setSpan(new ForegroundColorSpan(Color.BLACK), 32, spanTxt.length(), 0);
        spanTxt.append(" " + getResources().getString(R.string.termcondition3));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                /*Intent intent
                        = new Intent(RegistrationActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);*/
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
       /* Intent intent
                = new Intent(this, RegistrationStep2Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("email", edtEmailAddress.getText().toString());
        bundle.putString("password", edtPassword.getText().toString());
        bundle.putString("mobile", edtMobileNo.getText().toString());
        intent.putExtras(bundle);
        MyUtility.savePreferences(RegistrationActivity.this, "password", edtPassword.getText().toString());
        startActivity(intent);*/
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

       /* mRelativeLayout.setVisibility(View.VISIBLE);
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
                        Toast.makeText(RegistrationActivity.this, "" + checkEmailResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistrationActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });*/
    }


//    {
//        mRelativeLayout.setVisibility(View.VISIBLE);
//        txtCompleteAccount.setVisibility(View.GONE);
//        String token = FirebaseInstanceId.getInstance().getToken();
//        Tocken = String.valueOf(token);
//
//
//        APILogin service = ApiClient.getClient().create(APILogin.class);
//        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), email);
//        RequestBody reqPassword = RequestBody.create(MediaType.parse("text/plain"), password);
//        RequestBody reqFirstname = RequestBody.create(MediaType.parse("text/plain"), firstname);
//        RequestBody reqMidlename = RequestBody.create(MediaType.parse("text/plain"), username);
//        RequestBody reqLastname = RequestBody.create(MediaType.parse("text/plain"), lastname);
//        RequestBody reqMobileNo = RequestBody.create(MediaType.parse("text/plain"), mobile);
//        RequestBody reqDefaultsize = RequestBody.create(MediaType.parse("text/plain"), defaultSize);
//        RequestBody reqDeviceType = RequestBody.create(MediaType.parse("text/plain"), "android");
//        RequestBody reqDeviceTocken = RequestBody.create(MediaType.parse("text/plain"), token);
//        MultipartBody.Part fileToUpload = null;
//        if (mediaPath != null) {
//
//          /*  File file = new File(mediaPath);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);*/
//
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                    .format(new Date());
//            String imageName = timeStamp + ".jpg";
//            Bitmap bm = BitmapFactory.decodeFile(mediaPath);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//            byte[] imageBytes = baos.toByteArray();
//
//
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
//            fileToUpload = MultipartBody.Part.createFormData("image", imageName, requestFile);
//
//
//        } else {
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
//            fileToUpload = MultipartBody.Part.createFormData("image", "", requestBody);
//        }
//
//
//        Call<LoginResponse> userCall = service.registerUser(reqEmail,
//                reqPassword, reqFirstname, reqMidlename, reqLastname, reqMobileNo, reqDefaultsize, reqDeviceType, reqDeviceTocken, fileToUpload);
//
//        userCall.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
//                if (response.isSuccessful()) {
//                    LoginResponse loginResponse = ((LoginResponse) response.body());
//                    mRelativeLayout.setVisibility(View.GONE);
//                    txtCompleteAccount.setVisibility(View.VISIBLE);
//                    if (loginResponse.getStatus() == 1) {
//                        Toast.makeText(CompleteAccountActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                        if (loginResponse.getUser() != null) {
//
//
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "id", loginResponse.getUser().getId());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "email", loginResponse.getUser().getEmail());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "mobile", loginResponse.getUser().getMobile());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "first_name", loginResponse.getUser().getFirstName());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "middle_name", loginResponse.getUser().getMiddleName());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "last_name", loginResponse.getUser().getLastName());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "image", loginResponse.getUser().getImage());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "default_size", loginResponse.getUser().getDefaultSize());
//
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "wallet_balance", loginResponse.getUser().getWallet());
//                            MyUtility.savePreferences(CompleteAccountActivity.this, "sellerType", loginResponse.getUser().getVerifiedSaller());
//                            FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
//                            mAuthentication.setEmail(email);
//                            mAuthentication.setPassword(password);
//                            mAuthentication.setEnableFignure(false);
//                            MyUtility.saveObjectToSharedPreference(CompleteAccountActivity.this, "mPreference", "mObjectKey", mAuthentication);
//
//                            Log.d(TAG, "response" + response);
//                            Log.d(TAG, "id" + loginResponse.getUser().getId());
//                            Log.d(TAG, "email" + loginResponse.getUser().getEmail());
//                            Log.d(TAG, "mobile" + loginResponse.getUser().getMobile());
//                            Log.d(TAG, "firstname" + loginResponse.getUser().getFirstName());
//                            Log.d(TAG, "userName" + loginResponse.getUser().getMiddleName());
//                            Log.d(TAG, "lastname" + loginResponse.getUser().getLastName());
//                            Log.d(TAG, "image" + loginResponse.getUser().getImage());
//                            Log.d(TAG, "defaultsize" + loginResponse.getUser().getDefaultSize());
//                            Log.d(TAG, "wallet_balance" + loginResponse.getUser().getWallet());
//                            Intent intent = new Intent(CompleteAccountActivity.this, HomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } else {
//                        Toast.makeText(CompleteAccountActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                mRelativeLayout.setVisibility(View.GONE);
//                txtCompleteAccount.setVisibility(View.VISIBLE);
//                call.cancel();
//                Toast.makeText(CompleteAccountActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
//                Log.d("onFailure", t.toString());
//            }
//        });

//    }


}
