package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.FingurePrintAuthentication;
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

public class AddNewPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtSave;
    EditText edtNewPassword, edtConfirmPasword;
    TextInputLayout textInputLayout;
    APILogin mAPApiLogin;
    String id;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_add_newpassword);
        mAPApiLogin = ApiClient.getClient().create(APILogin.class);
        toolBarAndDrawerInitilization();
        id = getIntent().getStringExtra("id");
        // initilzation of xml componet
        initialization();


    }

    private void initialization() {
        textInputLayout = (TextInputLayout) findViewById(R.id.currentPassword);
        final boolean[] VISIBLE_PASSWORD = {false};  //declare as global variable befor onCreate()
        edtNewPassword = (EditText) findViewById(R.id.edtNewpassword);
        edtConfirmPasword = (EditText) findViewById(R.id.edtConfirmPassword);
        txtSave = (TextView) findViewById(R.id.txtSubmitFogot);
        txtSave.setOnClickListener(this);

        edtNewPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtNewPassword.getRight() - edtNewPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        //Helper.toast(LoginActivity.this, "Toggle visibility");
                        if (VISIBLE_PASSWORD[0]) {
                            VISIBLE_PASSWORD[0] = false;
                            edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                            edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        } else {
                            VISIBLE_PASSWORD[0] = true;
                            edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                            edtNewPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_outline_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                        }
                        return false;
                    }
                }
                return false;
            }
        });


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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtSubmitFogot:
                hideKeyboard();
                if (validate() == false) {
                    return;
                }
                addNewPassword();
                break;
        }
    }


    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validate() {
        boolean valid = true;
        String password = edtNewPassword.getText().toString();
        String reEnterPassword = edtConfirmPasword.getText().toString();


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            edtNewPassword.setError(getString(R.string.valid_password));
            valid = false;
        } else {
            edtNewPassword.setError(null);
        }
        if (!password.isEmpty()) {
            if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
                edtConfirmPasword.setError(getString(R.string.valid_paswordmatch));
                valid = false;
            } else {
                edtConfirmPasword.setError(null);
            }
        }


        return valid;
    }

    private void addNewPassword() {

        APILogin service = ApiClient.getClient().create(APILogin.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("id", id);
        credMap.put("password", edtNewPassword.getText().toString());

        Call<LoginResponse> call = service.addNewPassword(credMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
                        mAuthentication = MyUtility.getSavedObjectFromPreference(AddNewPasswordActivity.this, "mPreference", "mObjectKey", FingurePrintAuthentication.class);
                        mAuthentication.setPassword(edtConfirmPasword.getText().toString());
                        MyUtility.saveObjectToSharedPreference(AddNewPasswordActivity.this, "mPreference", "mObjectKey", mAuthentication);


                        Toast.makeText(AddNewPasswordActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddNewPasswordActivity.this, SplashActivity2.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(AddNewPasswordActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed

                call.cancel();
                Toast.makeText(AddNewPasswordActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }

}
