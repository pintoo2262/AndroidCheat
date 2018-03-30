package com.app.noan.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.LoginResponse;
import com.app.noan.model.VendorResponse;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private EditText edtUserName, edtPassword, edtFullname, edtEmail;
    private TextView txtChangImagae, txtChangePassword;

    //Image
    private CircleImageView ciProifile;


    private Bitmap bitmap, createBitmpaImage;
    byte[] imageBytes;
    private String imageName, mediaPath;

    MyUtility myUtility;
    private int REQUEST_CAMERA = 0;


    Dialog pDialog, pDialog1;
    APILogin apiLogin;


    String userId, userEmail, userMobile, type;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_edit_profile);
        apiLogin = ApiClient.getClient().create(APILogin.class);

        toolBarAndDrawerInitilization();
        userId = MyUtility.getSavedPreferences(EditProfileActivity.this, "id");

        // initilzation of xml componet
        initialization();
        type = MyUtility.getSavedPreferences(EditProfileActivity.this, "sellerType");

        if (type.equals("vendorseller")) {
            userEmail = MyUtility.getSavedPreferences(EditProfileActivity.this, "email");
            viewProfileByVendorSeller();
        } else {
            userEmail = MyUtility.getSavedPreferences(EditProfileActivity.this, "email");
            userMobile = MyUtility.getSavedPreferences(EditProfileActivity.this, "mobile");
            viewProfileByUserSeller();
        }


        customTextView1(txtChangePassword);

    }

    private void initialization() {
        edtUserName = (EditText) findViewById(R.id.edt_userName);
        edtFullname = (EditText) findViewById(R.id.edt_fullname);
        edtEmail = (EditText) findViewById(R.id.edt_Email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        txtChangImagae = (TextView) findViewById(R.id.txt_chage_image);
        ciProifile = (CircleImageView) findViewById(R.id.cv_Profile);
        txtChangePassword = (TextView) findViewById(R.id.txt_changePassword);
        txtChangImagae.setOnClickListener(this);


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
        mtoolbarTitle.setText(getResources().getString(R.string.edit_profile));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.btnSave:
                if (type.equals("vendorseller")) {
                    editProfileByVendorServer();
                } else {
                    editProfileByUserSellerServer();
                }

                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void customTextView1(TextView view) {
        SpannableStringBuilder spanTxt = new SpannableStringBuilder();
        spanTxt.append(" " + getResources().getString(R.string.changePassword));
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view1) {
                Intent intent = new Intent(EditProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        }, spanTxt.length() - getResources().getString(R.string.changePassword).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_chage_image:
                selectImage();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void viewProfileByUserSeller() {
        pDialog = new Dialog(EditProfileActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("id", userId);
        Call<LoginResponse> detailResponseCall = apiLogin.userProfile(credMap);
        detailResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (loginResponse.getStatus() == 1) {
                        edtEmail.setText(response.body().getUser().getEmail());
                        edtFullname.setText(response.body().getUser().getFirstName() + " " +
                                response.body().getUser().getLastName());
                        edtUserName.setText(response.body().getUser().getMiddleName());
                        edtPassword.setText(response.body().getUser().getPassword());
                        mediaPath = response.body().getUser().getImage();

                        MyUtility.savePreferences(EditProfileActivity.this, "middle_name", edtUserName.getText().toString());
                        MyUtility.savePreferences(EditProfileActivity.this, "image", mediaPath);

                        if (mediaPath != null && !mediaPath.equals("") && !mediaPath.equals(null)) {
                            Glide.with(EditProfileActivity.this)
                                    .load(mediaPath)
                                    .asBitmap()
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.noimage)
                                    .into(ciProifile);
                        } else {
                            Glide.with(EditProfileActivity.this)
                                    .load(mediaPath)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.noimage)
                                    .into(ciProifile);
                        }


                    } else {
                        Toast.makeText(EditProfileActivity.this, "" + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(EditProfileActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }

    private void viewProfileByVendorSeller() {
        pDialog = new Dialog(EditProfileActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        Call<VendorResponse> vendorProfile = apiLogin.vendorProfile(credMap);
        vendorProfile.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    VendorResponse vendorResponse = ((VendorResponse) response.body());
                    Log.d("onResponse", "" + response.body().getMessage());
                    if (vendorResponse.getStatus() == 1) {
                        edtPassword.setText("1121");
                        edtEmail.setText(response.body().getData().getEmail());
                        edtFullname.setText(response.body().getData().getName());
                        edtUserName.setText(response.body().getData().getUsername());
                        mediaPath = response.body().getData().getImage();
                        if (mediaPath != null && !mediaPath.equals("") && !mediaPath.equals(null)) {
                            Glide.with(EditProfileActivity.this)
                                    .load(mediaPath)
                                    .asBitmap()
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.noimage)
                                    .into(ciProifile);
                        } else {
                            Glide.with(EditProfileActivity.this)
                                    .load(mediaPath)
                                    .placeholder(R.drawable.loading)
                                    .error(R.drawable.noimage)
                                    .into(ciProifile);
                        }


                    } else {
                        Toast.makeText(EditProfileActivity.this, "" + vendorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(EditProfileActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });

    }


    private void editProfileByUserSellerServer() {
        pDialog1 = new Dialog(EditProfileActivity.this);
        pDialog1.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog1.setCancelable(false);
        showDialog1();


        String str = edtFullname.getText().toString();
        String[] splited = str.split("\\s+");
        String split_one = splited[0];
        String split_second = splited[1];
        String strUserName = edtUserName.getText().toString();
        RequestBody reqUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody reqMobile = RequestBody.create(MediaType.parse("text/plain"), userMobile);
        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody reqFirstName = RequestBody.create(MediaType.parse("text/plain"), split_one);
        RequestBody reqMiddleName = RequestBody.create(MediaType.parse("text/plain"), strUserName);
        RequestBody reqLastName = RequestBody.create(MediaType.parse("text/plain"), split_second);
        MultipartBody.Part fileToUpload = null;
        if (mediaPath != null && !mediaPath.equals("") && !mediaPath.equals(null)) {
            if (bitmap != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                fileToUpload = MultipartBody.Part.createFormData("image", imageName, requestFile);
            } else {
                String url = "projects-beta.com/noan_new1/webservices/user/edit_user_detail";
                createBitmpaImage = ((BitmapDrawable) ciProifile.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageName = mediaPath.substring(url.lastIndexOf('/') + 1);
                createBitmpaImage.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                imageBytes = bos.toByteArray();
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                fileToUpload = MultipartBody.Part.createFormData("image", imageName, requestFile);
            }
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg*"), "");
            fileToUpload = MultipartBody.Part.createFormData("image", "", requestBody);
        }


        Call<LoginResponse> userCall = apiLogin.edtitProfile(reqUserId, reqMobile, reqEmail,
                reqFirstName, reqMiddleName, reqLastName, fileToUpload);

        userCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                hideDialog1();
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = ((LoginResponse) response.body());
                    if (loginResponse.getStatus() == 1) {
                        viewProfileByUserSeller();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(EditProfileActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });


    }

    private void editProfileByVendorServer() {
        pDialog1 = new Dialog(EditProfileActivity.this);
        pDialog1.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog1.setCancelable(false);
        showDialog1();


        String str = edtFullname.getText().toString();
        String strUserName = edtUserName.getText().toString();

        RequestBody reqUserId = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody reqname = RequestBody.create(MediaType.parse("text/plain"), str);
        RequestBody reqEmail = RequestBody.create(MediaType.parse("text/plain"), userEmail);
        RequestBody reqUserName = RequestBody.create(MediaType.parse("text/plain"), strUserName);

        MultipartBody.Part fileToUpload = null;
        if (mediaPath != null && !mediaPath.equals("") && !mediaPath.equals(null)) {

            if (bitmap != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                fileToUpload = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
            } else {
                String url = "projects-beta.com/noan_new/webservices/seller/edit_seller_detail";
                createBitmpaImage = ((BitmapDrawable) ciProifile.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                imageName = mediaPath.substring(url.lastIndexOf('/') + 1);
                createBitmpaImage.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                imageBytes = bos.toByteArray();
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
                fileToUpload = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
            }


        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg*"), "");
            fileToUpload = MultipartBody.Part.createFormData("image", "", requestBody);
        }


        Call<VendorResponse> userCall = apiLogin.vendoreProfileEdit(reqUserId, reqname, reqEmail, reqUserName, fileToUpload);

        userCall.enqueue(new Callback<VendorResponse>() {
            @Override
            public void onResponse(Call<VendorResponse> call, Response<VendorResponse> response) {
                hideDialog1();
                if (response.isSuccessful()) {
                    VendorResponse vendorResponse = ((VendorResponse) response.body());
                    if (vendorResponse.getStatus() == 1) {
                        viewProfileByVendorSeller();
                    }
                }
            }

            @Override
            public void onFailure(Call<VendorResponse> call, Throwable t) {
                call.cancel();
                Toast.makeText(EditProfileActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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

    public void showDialog1() {
        if (pDialog1 != null && !pDialog1.isShowing())
            pDialog1.show();
    }

    public void hideDialog1() {

        if (pDialog1 != null && pDialog1.isShowing())
            pDialog1.dismiss();
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = myUtility.checkPermission(EditProfileActivity.this);
                if (items[item].equals("Take Photo")) {
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        imageName = timeStamp + ".jpg"; //
        mediaPath = String.valueOf(destination);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        imageBytes = baos.toByteArray();


//        ciProfileImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
        Glide.with(EditProfileActivity.this)
                .load(mediaPath)
                .into(ciProifile);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        // Get the Image from data
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        imageName = timeStamp + ".jpg"; //
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        mediaPath = cursor.getString(columnIndex);
        bitmap = BitmapFactory.decodeFile(mediaPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
        imageBytes = baos.toByteArray();
        Glide.with(EditProfileActivity.this)
                .load(mediaPath)
                .into(ciProifile);

        cursor.close();


    }


}
