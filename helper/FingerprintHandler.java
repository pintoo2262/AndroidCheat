package com.app.noan.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.model.FingurePrintAuthentication;
import com.app.noan.model.LoginResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanjay on 27/9/17.
 */

@SuppressLint("NewApi")
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Activity context;
    static String Tocken;
    MyUtility myUtility;
    Dialog pDialog, customDialog;

    // Constructor
    public FingerprintHandler(Activity mContext) {
        context = mContext;
        myUtility = new MyUtility(context);
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    public void update(String e, Boolean success) {
        Intent intent;
        if (success) {

         /*   if (customDialog != null) {
                if (customDialog.isShowing()) {
                    customDialog.dismiss();
                }
            }
*/

            FingurePrintAuthentication mAuthentication = new FingurePrintAuthentication();
            mAuthentication = MyUtility.getSavedObjectFromPreference(context, "mPreference", "mObjectKey", FingurePrintAuthentication.class);
            if (mAuthentication != null) {
                if (mAuthentication.getEnableFignure().equals(false)) {
                    Toast.makeText(context, "You have not enabled fingerprint authentication for this Application", Toast.LENGTH_SHORT).show();
                } else {
                    loginByServer(mAuthentication.getEmail(), mAuthentication.getPassword());
                }

            } else {
                Toast.makeText(context, "You have not enabled fingerprint authentication for this Application", Toast.LENGTH_SHORT).show();
            }


        } else {
//            hideAuthenticationDialog();
            Toast.makeText(context, "Fingerprint Authentication failed.", Toast.LENGTH_SHORT).show();
//            authenticationFailed();
        }
    }

    private void loginByServer(String email, String password) {
        pDialog = new Dialog(context);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


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
                    hideDialog();
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        Toast.makeText(context, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if (loginResponse.getUser() != null) {
                            String userId = loginResponse.getUser().getId();
                            String[] parts = userId.split("-");
                            String type = parts[0];
                            if (type.equals("cs")) {
                                if (loginResponse.getUser().getVerifiedSaller().equals("approved")) {
                                    MyUtility.savePreferences(context, "sellerType", "isUserSell");
                                } else if (loginResponse.getUser().getVerifiedSaller().equals("unapprove")) {
                                    MyUtility.savePreferences(context, "sellerType", "unapprove");
                                } else {
                                    MyUtility.savePreferences(context, "sellerType", "unapproved");
                                }

                                MyUtility.savePreferences(context, "id", String.valueOf(loginResponse.getUser().getId()));
                                MyUtility.savePreferences(context, "email", loginResponse.getUser().getEmail());
                                MyUtility.savePreferences(context, "mobile", loginResponse.getUser().getMobile());
                                MyUtility.savePreferences(context, "first_name", loginResponse.getUser().getFirstName());
                                MyUtility.savePreferences(context, "middle_name", loginResponse.getUser().getMiddleName());
                                MyUtility.savePreferences(context, "last_name", loginResponse.getUser().getLastName());
                                MyUtility.savePreferences(context, "image", loginResponse.getUser().getImage());
                                MyUtility.savePreferences(context, "default_size", loginResponse.getUser().getDefaultSize());

                                MyUtility.savePreferences(context, "wallet_balance", loginResponse.getUser().getWallet());
                                MyUtility.savePreferences(context, "payPalEmail", loginResponse.getUser().getPaypalEmail());

                            } else {
                                MyUtility.savePreferences(context, "sellerType", "vendorseller");


                                MyUtility.savePreferences(context, "id", String.valueOf(loginResponse.getUser().getId()));
                                MyUtility.savePreferences(context, "email", loginResponse.getUser().getEmail());


                                MyUtility.savePreferences(context, "name", loginResponse.getUser().getName());
                                MyUtility.savePreferences(context, "image", loginResponse.getUser().getImage());
                                MyUtility.savePreferences(context, "payPalEmail", loginResponse.getUser().getPaypalEmail());

                            }


                            Intent i = new Intent(context, HomeActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);
                            context.finish();
                        }
                    } else {
                        Toast.makeText(context, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Log error here since request failed
                call.cancel();
                hideDialog();
                Toast.makeText(context, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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

    public void authenticationFailed() {
        TextView dialogTitle, dialogMsg;
        final Button btnCancel;
        customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.setContentView(R.layout.custom_dialog_msg);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        dialogTitle = customDialog.findViewById(R.id.txt_dialogTitle);
        dialogMsg = customDialog.findViewById(R.id.txt_dialogMsg);
        btnCancel = customDialog.findViewById(R.id.btnCancel);
        dialogTitle.setText("Authentication Failed");
        dialogMsg.setText("Authentication was canceled by the user");
        btnCancel.setText("Ok");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnCancel.getText().toString().equals("Ok")) {
                    hideAuthenticationDialog();
                }

            }
        });
        showAuthenticationDialog();
    }

    public void showAuthenticationDialog() {
        if (customDialog != null && !customDialog.isShowing()) {
            customDialog.show();
        }

    }

    public void hideAuthenticationDialog() {
        if (customDialog != null && customDialog.isShowing()) {
            customDialog.dismiss();
        }

    }
}