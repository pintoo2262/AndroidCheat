package com.app.noan.activity;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.helper.FingerprintHandler;
import com.app.noan.model.FingurePrintAuthentication;
import com.app.noan.model.LoginResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private String TAG = LoginActivity.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtForgotPass;
    private EditText edtEmail, edtPassword;
    private TextView txtLogin, txt_LoginCondition;
    private ImageView ivFigureprint;
    private String strEmail, strPasword;
    private TextWatcher textWatcher;


    //Authentication
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "login";
    private Cipher cipher;
    KeyguardManager keyguardManager;
    FingerprintManager fingerprintManager;


    // Dialog
    RelativeLayout mRelativeLayout;
    MyUtility myUtility;
    static String Tocken;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_login);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();


        // set custom link in textview
        customTextView(txt_LoginCondition);


        customTextView1(txtForgotPass);


    }


    private void initialization() {
        myUtility = new MyUtility(LoginActivity.this);
        edtEmail = (EditText) findViewById(R.id.edt_Email_Login);
        edtPassword = (EditText) findViewById(R.id.edt_password_Login);
        txtLogin = (TextView) findViewById(R.id.txtLogin);
        txt_LoginCondition = (TextView) findViewById(R.id.txt_LoginCondition);
        txtForgotPass = (TextView) findViewById(R.id.txt_Forgot);
        ivFigureprint = (ImageView) findViewById(R.id.iv_fignerprint);


        ivFigureprint.setOnClickListener(this);
        edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edtPassword.setTypeface(Typeface.DEFAULT);

        //

        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        // Initializing both Android Keyguard Manager and Fingerprint Manager
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

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
        edtEmail.addTextChangedListener(textWatcher);
        edtPassword.addTextChangedListener(textWatcher);

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
        mtoolbarTitle.setText(getResources().getString(R.string.log_in));

    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getResources().getString(R.string.termcondition));
        spanTxt.append(" " + getResources().getString(R.string.termcondition1));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent
                        = new Intent(LoginActivity.this, TermAndConditionsActivity.class);
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
                        = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
            }

        }, spanTxt.length() - getResources().getString(R.string.termcondition3).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }


    private void customTextView1(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder();
        spanTxt.append(" " + getResources().getString(R.string.forogt));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view1) {
                Intent intent2 = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent2);
            }
        }, spanTxt.length() - getResources().getString(R.string.forogt).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);

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
            case R.id.txtLogin:
                myUtility.hideKeyboard(this);
                login();
                break;

            case R.id.iv_fignerprint:
                String title = getResources().getString(R.string.touch);
                String msg = getResources().getString(R.string.pls_touchmsg);
                MyUtility.showCustomMeassge(LoginActivity.this, "Cancel", title, msg);
                // Check whether the device has a Fingerprint sensor.
                if (!fingerprintManager.isHardwareDetected()) {
                    /**
                     * An error message will be displayed if the device does not contain the fingerprint hardware.
                     * However if you plan to implement a default authentication method,
                     * you can redirect the user to a default authentication activity from here.
                     * ProductForSellResponse:
                     * Intent intent = new Intent(this, DefaultAuthenticationActivity.class);
                     * startActivity(intent);
                     */
//            Toast.makeText(getApplicationContext(), "Your Device does not have a Fingerprint Sensor", Toast.LENGTH_SHORT).show();
                } else {
                    // Checks whether fingerprint permission is set on manifest
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getApplicationContext(), "Fingerprint authentication permission not enabled", Toast.LENGTH_SHORT).show();
                    } else {
                        // Check whether at least one fingerprint is registered
                        if (!fingerprintManager.hasEnrolledFingerprints()) {
//                    Toast.makeText(getApplicationContext(), "Register at least one fingerprint in Settings", Toast.LENGTH_SHORT).show();
                        } else {
                            // Checks whether lock screen security is enabled or not
                            if (!keyguardManager.isKeyguardSecure()) {
                                Toast.makeText(getApplicationContext(), "Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                            } else {
                                generateKey();
                                if (cipherInit()) {
                                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                                    FingerprintHandler helper = new FingerprintHandler(LoginActivity.this);
                                    helper.startAuth(fingerprintManager, cryptoObject);
                                }
                            }
                        }
                    }
                }

                break;

        }

    }


    private void checkFieldsForEmptyValues() {
        txtLogin.setOnClickListener(this);
        strEmail = edtEmail.getText().toString();
        strPasword = edtPassword.getText().toString();
        if (strEmail.length() > 4 && strPasword.length() > 4) {
            loginDark();
        } else {
            loginTrans();
        }
    }


    public void login() {
        Log.d(TAG, "Login");

        if (validate() == false) {
            onLoginFailed();
            return;
        }
        loginByServer();
    }

    public void loginTrans() {
        txtLogin.setEnabled(false);
        txtLogin.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtLogin.setTextColor(getResources().getColor(R.color.black));
    }

    public void loginDark() {
        txtLogin.setEnabled(true);
        txtLogin.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtLogin.setTextColor(getResources().getColor(R.color.white));
    }

    private void loginByServer() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        txtLogin.setVisibility(View.GONE);

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();
        Tocken = String.valueOf(token);

        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("email", email);
        credMap.put("password", password);
        credMap.put("mobile_type", "android");
        credMap.put("device_token", Tocken);
        Log.d("device_tocken", Tocken);

        Call<LoginResponse> call = service.userLogIn(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    txtLogin.setVisibility(View.VISIBLE);
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(LoginActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (loginResponse.getUser() != null) {
                            FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
                            mAuthentication = MyUtility.getSavedObjectFromPreference(LoginActivity.this, "mPreference", "mObjectKey", FingurePrintAuthentication.class);
                            if (mAuthentication == null) {
                                FingurePrintAuthentication mAuthentication1 = new FingurePrintAuthentication();
                                mAuthentication1.setEmail(edtEmail.getText().toString());
                                mAuthentication1.setPassword(edtPassword.getText().toString());
                                mAuthentication1.setEnableFignure(false);
                                MyUtility.saveObjectToSharedPreference(LoginActivity.this, "mPreference", "mObjectKey", mAuthentication1);
                            } else {
                                if (!mAuthentication.getEmail().equals(edtEmail.getText().toString())) {
                                    mAuthentication.setEmail(edtEmail.getText().toString());
                                    mAuthentication.setPassword(edtPassword.getText().toString());
                                    mAuthentication.setEnableFignure(false);
                                    MyUtility.saveObjectToSharedPreference(LoginActivity.this, "mPreference", "mObjectKey", mAuthentication);
                                }
                            }


                            String userId = loginResponse.getUser().getId();
                            String[] parts = userId.split("-");
                            String type = parts[0];
                            if (type.equals("cs")) {
                                if (loginResponse.getUser().getVerifiedSaller().equals("approved")) {
                                    MyUtility.savePreferences(LoginActivity.this, "sellerType", "isUserSell");
                                } else if (loginResponse.getUser().getVerifiedSaller().equals("unapprove")) {
                                    MyUtility.savePreferences(LoginActivity.this, "sellerType", "unapprove");
                                } else {
                                    MyUtility.savePreferences(LoginActivity.this, "sellerType", "unapproved");
                                }

                                MyUtility.savePreferences(LoginActivity.this, "id", String.valueOf(loginResponse.getUser().getId()));
                                MyUtility.savePreferences(LoginActivity.this, "email", loginResponse.getUser().getEmail());
                                MyUtility.savePreferences(LoginActivity.this, "password", edtPassword.getText().toString());
                                MyUtility.savePreferences(LoginActivity.this, "mobile", loginResponse.getUser().getMobile());
                                MyUtility.savePreferences(LoginActivity.this, "first_name", loginResponse.getUser().getFirstName());
                                MyUtility.savePreferences(LoginActivity.this, "middle_name", loginResponse.getUser().getMiddleName());
                                MyUtility.savePreferences(LoginActivity.this, "last_name", loginResponse.getUser().getLastName());
                                MyUtility.savePreferences(LoginActivity.this, "image", loginResponse.getUser().getImage());
                                MyUtility.savePreferences(LoginActivity.this, "default_size", loginResponse.getUser().getDefaultSize());

                                MyUtility.savePreferences(LoginActivity.this, "wallet_balance", loginResponse.getUser().getWallet());
                                MyUtility.savePreferences(LoginActivity.this, "payPalEmail", loginResponse.getUser().getPaypalEmail());

                            } else {
                                MyUtility.savePreferences(LoginActivity.this, "sellerType", "vendorseller");


                                MyUtility.savePreferences(LoginActivity.this, "id", String.valueOf(loginResponse.getUser().getId()));
                                MyUtility.savePreferences(LoginActivity.this, "email", loginResponse.getUser().getEmail());
                                MyUtility.savePreferences(LoginActivity.this, "password", edtPassword.getText().toString());

                                MyUtility.savePreferences(LoginActivity.this, "name", loginResponse.getUser().getName());
                                MyUtility.savePreferences(LoginActivity.this, "image", loginResponse.getUser().getImage());
                                MyUtility.savePreferences(LoginActivity.this, "payPalEmail", loginResponse.getUser().getPaypalEmail());

                            }


                            Log.d(TAG, "response" + response);
                            Log.d(TAG, "id" + loginResponse.getUser().getId());
                            Log.d(TAG, "email" + loginResponse.getUser().getEmail());
                            Log.d(TAG, "mobile" + loginResponse.getUser().getMobile());
                            Log.d(TAG, "firstname" + loginResponse.getUser().getFirstName());
                            Log.d(TAG, "middlename" + loginResponse.getUser().getMiddleName());
                            Log.d(TAG, "lastname" + loginResponse.getUser().getLastName());
                            Log.d(TAG, "image" + loginResponse.getUser().getImage());
                            Log.d(TAG, "defaultsize" + loginResponse.getUser().getDefaultSize());
                            Log.d(TAG, "wallet_balance" + loginResponse.getUser().getWallet());


                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                mRelativeLayout.setVisibility(View.GONE);
                txtLogin.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(LoginActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }


    public boolean validate() {
        boolean valid = true;


        if (edtEmail.getText().toString().equals("")) {
            edtEmail.setError(getString(R.string.valid_user_name));
            valid = false;
            requestFocus(edtEmail);

        } else if (edtEmail.getText().toString().contains("@")) {
            if (!myUtility.isValidEmail(edtEmail.getText().toString())) {
                valid = false;
                edtEmail.setError(getString(R.string.valid_email_address));
                requestFocus(edtEmail);
            } else if (edtPassword.getText().toString().equals("")) {
                edtPassword.setError(getString(R.string.valid_password));
                valid = false;
                requestFocus(edtPassword);
            } else {

            }
        } else if (edtEmail.getText().toString().length() == 10) {
            if (!myUtility.isValidMobile(edtEmail.getText().toString())) {
                edtEmail.setError(getString(R.string.valid_mobileno));
                valid = false;
                requestFocus(edtEmail);
            } else if (edtPassword.getText().toString().equals("")) {
                edtPassword.setError(getString(R.string.valid_password));
                valid = false;
                requestFocus(edtPassword);
            } else {
                // Get token
            }
        } else {
            String ch = edtEmail.getText().toString();
            if (isValidMobile(ch)) {
                edtEmail.setError(getString(R.string.valid_mobileno));
                valid = false;
            } else {
                edtEmail.setError(getString(R.string.valid_email_address));
                valid = false;
            }
        }


        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        txtLogin.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }


        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get KeyGenerator instance", e);
        }


        try {
            keyStore.load(null);
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException |
                InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }
        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}