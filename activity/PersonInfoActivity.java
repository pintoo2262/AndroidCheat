package com.app.noan.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PersonalActivity";
    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtDateofBirth;
    private Button mbtnContinue;
    private EditText edtFullName, edtMobileNo;
    private DatePickerDialog mDatePickerDialog;
    private int day, year, month;
    private String strFullName, strMoblieNo;
    private TextWatcher textWatcher;

    Calendar userAge;
    private String shFullName, shMoblieNumber, shBirthDate, userId;
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
        setContentView(R.layout.activity_person_info);


        toolBarAndDrawerInitilization();
        MyUtility.onResumeSellerScreenType = 2;
        // initilzation of xml componet
        initialization();

        shFullName = getIntent().getStringExtra("IntetFullName");
        shMoblieNumber = getIntent().getStringExtra("IntetMobile");
        shBirthDate = getIntent().getStringExtra("intentDateOfBirth");


        if (shFullName != null && !shFullName.equals("")) {
            edtFullName.setText(shFullName);
            edtFullName.setSelection(shFullName.length());
        }
        if (shMoblieNumber != null && !shMoblieNumber.equals("")) {
            edtMobileNo.setText(shMoblieNumber);
        }
        if (shBirthDate != null && !shBirthDate.equals("")) {
            txtDateofBirth.setText(shBirthDate);
        }

    }


    private void initialization() {
        edtFullName = (EditText) findViewById(R.id.edtSellerFullName);
        edtMobileNo = (EditText) findViewById(R.id.edtSellerMobileNo);
        txtDateofBirth = (TextView) findViewById(R.id.edtSellerDateofbirth);
        mbtnContinue = (Button) findViewById(R.id.btnPersoninfoContinue);
        txtDateofBirth.setOnClickListener(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        userId = MyUtility.getSavedPreferences(PersonInfoActivity.this, "id");
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
        edtFullName.addTextChangedListener(textWatcher);
        edtMobileNo.addTextChangedListener(textWatcher);


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.personalinfo));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.requestSell));
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.btn_bgwhite));

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
            case R.id.btnPersoninfoContinue:
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    dialog("You must be at least 18 years old");
                } else {
                    validation();
                }


                break;

            case R.id.edtSellerDateofbirth:
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                mDatePickerDialog = new DatePickerDialog(PersonInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        try {
                            userAge = new GregorianCalendar(year, month, day);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                            String dateInString = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            month = (monthOfYear + 1);
                            Date date = formatter.parse(dateInString);
                            shBirthDate = MyUtility.getConvertDate1(formatter.format(date).toString());
                            txtDateofBirth.setText(shBirthDate);
                            mDatePickerDialog.show();

                        } catch (Exception ex) {

                        }
                    }
                }, year, month, day);

                mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDatePickerDialog.show();
                mDatePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        mDatePickerDialog.dismiss();

                    }
                });
                break;

        }
    }


    private void checkFieldsForEmptyValues() {
        mbtnContinue.setOnClickListener(this);
        strFullName = edtFullName.getText().toString();
        strMoblieNo = edtMobileNo.getText().toString();
        if (strFullName.length() > 2 && strMoblieNo.length() > 2) {
            mbtnContinue.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            mbtnContinue.setEnabled(true);
            mbtnContinue.setVisibility(View.VISIBLE);

        } else {
            mbtnContinue.setEnabled(false);
            mbtnContinue.setVisibility(View.VISIBLE);
            mbtnContinue.setTextColor(getResources().getColor(R.color.btn_reqsell));
        }
    }

    public void dialog(String msg) {
        final TextView dialogMsg;
        final Button btnOk;
        final Dialog validationDialog = new Dialog(PersonInfoActivity.this);
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

    public void validation() {
        Log.d(TAG, "Personal");

        if (validate() == false) {
            return;
        } else {
            personalInfoByserver();

        }


    }


    public boolean validate() {
        boolean valid = true;
        if (edtFullName.getText().toString().equals("")) {
            edtFullName.setError(getString(R.string.valid_user_name));
            valid = false;

        } else if (edtMobileNo.getText().toString().equals("")) {
            edtMobileNo.setError(getString(R.string.valid_mobileno));
            valid = false;

        }

        return valid;
    }

    public boolean isValidMobile(String mobile) {
        String phone = "[0-9]{10}";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }


    private void personalInfoByserver() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        mbtnContinue.setVisibility(View.GONE);

        String fullname = edtFullName.getText().toString();
        String mobile = edtMobileNo.getText().toString();
        String dateofBirth = txtDateofBirth.getText().toString();


        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("termsagree", "");
        credMap.put("full_name", fullname);
        credMap.put("phone_number", mobile);
        credMap.put("birth_date", dateofBirth);
        credMap.put("address1", "");
        credMap.put("address2", "");
        credMap.put("seller_info", "");
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "personal_info");

        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    if (sellerModelResponse.getStatus() == 1) {
                        Intent intentMessage = new Intent();
                        // Set The Result in Intent
                        setResult(1, intentMessage);
                        // finish The activity
                        finish();
                    } else {
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                mRelativeLayout.setVisibility(View.GONE);
                mbtnContinue.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(PersonInfoActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}


