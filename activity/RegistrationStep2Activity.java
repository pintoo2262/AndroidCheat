package com.app.noan.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.noan.R;
import com.app.noan.helper.App;
import com.steelkiwi.instagramhelper.InstagramHelper;
import com.steelkiwi.instagramhelper.InstagramHelperConstants;
import com.steelkiwi.instagramhelper.model.InstagramUser;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationStep2Activity extends AppCompatActivity implements View.OnClickListener {
    String TAG = RegistrationStep2Activity.class.getSimpleName();

    private Toolbar mToolbar;
    private TextView mToolbarTitle, txtWithoutLink, txtStepThree;
    private ImageView ivInstagram, ivTiwtter;

    ProgressDialog progress;

    //This is your KEY and SECRET
    String profileImage, userName, firstName, lastName;
    //Twitter Login Button
    TwitterLoginButton twitterLoginButton;
    String email = null, password = null, mobile = null;


    InstagramHelper instagramHelper;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Initializing TwitterAuthConfig, these two line will also added automatically while configuration we did
        setContentView(R.layout.activity_registration_step2);
        instagramHelper = App.getInstagramHelper();

//        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            password = extras.getString("password");
            mobile = extras.getString("mobile");
        }

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_button);
        //Adding callback to the button
        twitterLoginButton.setCallback(new Callback() {
            @Override
            public void success(Result result) {
                //If login succeeds passing the Calling the login method and passing Result object
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                //If failure occurs while login handle it here
//                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


        // set custom link in textview
        customTextView(txtWithoutLink);



    }

    private void initialization() {
        txtWithoutLink = (TextView) findViewById(R.id.txt_withoutlink);
        ivInstagram = (ImageView) findViewById(R.id.iv_instagram);
        ivTiwtter = (ImageView) findViewById(R.id.iv_twitter);
        txtStepThree = (TextView) findViewById(R.id.txtStep3);
        txtStepThree.setOnClickListener(this);
        ivInstagram.setOnClickListener(this);
        ivTiwtter.setOnClickListener(this);
        progress = new ProgressDialog(RegistrationStep2Activity.this);
        progress.setMessage("");
        progress.setIndeterminate(false);
        progress.setCancelable(false);


    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mToolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mToolbarTitle.setText(getResources().getString(R.string.step2));

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
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void customTextView(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder(
                getResources().getString(R.string.linkwithouttxt));
        spanTxt.append(" " + getResources().getString(R.string.linkwithouttxt1));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
//                visibleStep3();
                saveToPassingData();

            }


        }, spanTxt.length() - getResources().getString(R.string.linkwithouttxt1).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);
    }

    private void visibleStep3() {
        txtStepThree.setVisibility(View.VISIBLE);
        txtStepThree.setBackgroundColor(getResources().getColor(R.color.btn_bgdark));
        txtStepThree.setTextColor(getResources().getColor(R.color.btn_bgwhite));

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txtStep3:
                saveToPassingData();
                break;
            case R.id.iv_instagram:
                instagramHelper.loginFromActivity(RegistrationStep2Activity.this);
                break;
            case R.id.iv_twitter:
                twitterLoginButton.performClick();
                break;
        }

    }

    private void saveToPassingData() {
        Intent intent
                = new Intent(RegistrationStep2Activity.this, RegistrationCompleteActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("mobile", mobile);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveToPassingDataTwitter() {
        Intent intent
                = new Intent(RegistrationStep2Activity.this, RegistrationCompleteActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("mobile", mobile);
        bundle.putString("image", profileImage);
        bundle.putString("username", userName);

        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void saveToPassingDataIntagram() {
        Intent intent
                = new Intent(RegistrationStep2Activity.this, RegistrationCompleteActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("mobile", mobile);
        bundle.putString("image", profileImage);
        bundle.putString("username", userName);
        bundle.putString("firstname", firstName);
        bundle.putString("lastName", lastName);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Adding the login result back to the button
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);

        if (requestCode == InstagramHelperConstants.INSTA_LOGIN && resultCode == RESULT_OK) {
            InstagramUser user = instagramHelper.getInstagramUser(this);
            Log.d("USER_ID", user.getData().getId());
            Log.d("USER_Name", user.getData().getUsername());
            Log.d("USER_FullName", user.getData().getFullName());
            Log.d("USER_Bio", user.getData().getBio());
            Log.d("USER_profilePicture", user.getData().getProfilePicture());
            Log.d("USER_count", String.valueOf(user.getData().getCounts()));
            Log.d("USER_website", String.valueOf(user.getData().getWebsite()));

            //Getting the profile image url

            userName = user.getData().getUsername();
            profileImage = user.getData().getProfilePicture();
            String[] arr = user.getData().getFullName().split(" ");

            firstName = arr[0];
            lastName = arr[1];


            saveToPassingDataIntagram();

        } else {
//            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
        }
    }

    //The login function accepting the result object

    public void login(Result result) {
        //Creating a twitter session with result’s data
        TwitterSession session = (TwitterSession) result.data;
        //Getting the username from session
        userName = session.getUserName();
        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                    }

                    @Override
                    public void success(Result userResult) {
                        //If it succeeds creating a User object from userResult.data
                        User user = (User) userResult.data;
                        //Getting the profile image url
                        userName = user.name;
                        profileImage = user.profileImageUrl.replace("_normal", "");

                        saveToPassingDataTwitter();
                    }
                });
    }


}
