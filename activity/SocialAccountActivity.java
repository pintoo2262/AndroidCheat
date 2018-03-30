package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.helper.App;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SocialAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle, txtFb, txtTwitter, txtInstagram;
    private Button btnSocialContinue;

    private String intentFb, intentTw, intentIn, userId, userName, fbLink = "", twitterLink = "", instagramLink = "";

    // twitter
    TwitterLoginButton twitterLoginButton;
    TwitterAuthConfig authConfig;


    // facebook
    private LoginButton btn_fblogin;
    /* Facebook initialize*/
    private CallbackManager callbackManager;


    // instagram
    InstagramHelper instagramHelper;
    AccessToken accessToken;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_social_account);
        callbackManager = CallbackManager.Factory.create();
        instagramHelper = App.getInstagramHelper();
        authConfig = App.getTiwtterHelper();

        toolBarAndDrawerInitilization();
        MyUtility.onResumeSellerScreenType = 2;


        // initilzation of xml componet
        initialization();
        userId = MyUtility.getSavedPreferences(SocialAccountActivity.this, "id");
        intentFb = getIntent().getStringExtra("fbLink");
        intentIn = getIntent().getStringExtra("inLink");
        intentTw = getIntent().getStringExtra("twLink");


        if (intentFb != null && !intentFb.equals("")) {
            fbLink = intentFb;
            int index = intentFb.lastIndexOf('/');
            String fbName = intentFb.substring(index + 1);
            txtFb.setText(fbName);
            accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                getUserName();
            }
            btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
        }
        if (intentIn != null && !intentIn.equals("")) {
            instagramLink = intentIn;
            int index = intentIn.lastIndexOf('/');
            String instName = intentIn.substring(index + 1);
            txtInstagram.setText(instName);
            btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
        }
        if (intentTw != null && !intentTw.equals("")) {
            twitterLink = intentTw;
            int index = intentTw.lastIndexOf('/');
            String twname = intentTw.substring(index + 1);
            txtTwitter.setText(twname);
            btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
        }


        // FaceBook
        btn_fblogin = (LoginButton) findViewById(R.id.btn_fblogin);
        btn_fblogin.setReadPermissions("email");
        btn_fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        // Tiwtter
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        //Adding callback to the button
        twitterLoginButton.setCallback(new com.twitter.sdk.android.core.Callback() {
            @Override
            public void success(Result result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


    }

    private void initialization() {
        txtFb = (TextView) findViewById(R.id.txt_Social_facebook);
        txtTwitter = (TextView) findViewById(R.id.txt_Social_twitter);
        txtInstagram = (TextView) findViewById(R.id.txt_Social_Instagram);
        btnSocialContinue = (Button) findViewById(R.id.btnSocialContinue);


        txtFb.setOnClickListener(this);
        txtTwitter.setOnClickListener(this);
        txtFb.setOnClickListener(this);
        txtInstagram.setOnClickListener(this);
        btnSocialContinue.setOnClickListener(this);
        btnSocialContinue.setTextColor(getResources().getColor(R.color.btn_reqsell));


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
        mtoolbarTitle.setText(getResources().getString(R.string.linksociakaccount));
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
            case R.id.txt_Social_facebook:
                btn_fblogin.performClick();
                break;
            case R.id.txt_Social_twitter:
                twitterLoginButton.performClick();
                break;
            case R.id.txt_Social_Instagram:
                instagramHelper.loginFromActivity(SocialAccountActivity.this);
                break;
            case R.id.btnSocialContinue:
                if (fbLink != null && !fbLink.equals("") || twitterLink != null && !twitterLink.equals("") || instagramLink != null && !instagramLink.equals("")) {
                    if (fbLink.equals("null")) {
                        fbLink = "";
                    } else if (twitterLink.equals("null")) {
                        twitterLink = "";
                    } else if (instagramLink.equals("null")) {
                        instagramLink = "";
                    }
                    PersonalReturnaddressInfoByserver();
                    finish();
                } else {
                    dialog("please select at least one social media accounts");

                }


                break;
        }
    }

    private void PersonalReturnaddressInfoByserver() {
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
        credMap.put("facebook_link", fbLink);
        credMap.put("twitter_link", twitterLink);
        credMap.put("insta_link", instagramLink);
        credMap.put("submit_type", "link_account");
        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    if (sellerModelResponse.getStatus() == 1) {
                        Intent intentMessage = new Intent();
                        // put the message in Intent
                        // Set The Result in Intent
                        setResult(4, intentMessage);
                        // finish The activity
                        finish();
                    } else {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }



     /* Faceboook */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 64206) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == 140) {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
            InstagramUser user = instagramHelper.getInstagramUser(this);
            if (user != null) {
                txtInstagram.setText(user.getData().getUsername());
                instagramLink = "https://www.instagram.com/" + user.getData().getUsername();
                btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
//            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();`
        }


    }

    public void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            String name = json_object.getString("name");
                            String id = json_object.getString("id");
                            txtFb.setText(name);
                            fbLink = "https://www.facebook.com/" + id + "/" + name;
                            btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    protected void getUserName() {

        GraphRequest data_request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        try {
                            String name = json_object.getString("name");
                            String id = json_object.getString("id");
                            txtFb.setText(name);
                            fbLink = "https://www.facebook.com/" + id + "/" + name;
                            btn_fblogin.setEnabled(false);
                            btnSocialContinue.setTextColor(getResources().getColor(R.color.white));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();


    }

    public void login(Result result) {
        //Creating a twitter session with result’s data
        TwitterSession session = (TwitterSession) result.data;
        //Getting the username from session
        userName = session.getUserName();
        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new com.twitter.sdk.android.core.Callback() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                    }

                    @Override
                    public void success(Result userResult) {
                        //If it succeeds creating a User object from userResult.data
                        User user = (User) userResult.data;
                        //Getting the profile image url
                        userName = user.screenName;
                        txtTwitter.setText(userName);
                        twitterLink = "https://twitter.com/" + userName;
                        btnSocialContinue.setTextColor(getResources().getColor(R.color.white));

                    }
                });
    }

    public void dialog(String msg) {
        final TextView dialogMsg;
        final Button btnOk;
        final Dialog validationDialog = new Dialog(SocialAccountActivity.this);
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

}
