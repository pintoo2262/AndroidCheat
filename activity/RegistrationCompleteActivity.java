package com.app.noan.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.retrofit_api.APILogin;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationCompleteActivity extends AppCompatActivity implements View.OnClickListener {


    private Toolbar mToolbar;
    private TextView txtRegister, mtoolbarTitle;
    private EditText edtUserName, edtFirstName, edtLastName;
    private CircleImageView ciProfileImage;
    public String userName, firstName, lastName, mediaPath;
    TextWatcher textWatcher;
    MyUtility myUtility;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    String email = null, password = null, mobile = null, image = null, username = null, fname = null, lname = null;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_registration_complete);

        toolBarAndDrawerInitilization();

        // initilzation of xml componet
        initialization();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("email");
            password = extras.getString("password");
            mobile = extras.getString("mobile");
            image = extras.getString("image");
            username = extras.getString("username");
            fname = extras.getString("firstname");
            lname = extras.getString("lastName");
            edtUserName.setText(username);
            edtFirstName.setText(fname);
            edtLastName.setText(lname);
            Glide.with(RegistrationCompleteActivity.this)
                    .load(image)
                    .into(ciProfileImage);
            download(image);
        }
        Glide.with(RegistrationCompleteActivity.this)
                .load(R.drawable.iv_profile)
                .into(ciProfileImage);
    }

    private void initialization() {
        myUtility = new MyUtility(this);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        edtUserName = (EditText) findViewById(R.id.edt_user);
        edtFirstName = (EditText) findViewById(R.id.edt_first);
        edtLastName = (EditText) findViewById(R.id.edt_last);
        ciProfileImage = (CircleImageView) findViewById(R.id.iv_ProfileImage);
        ciProfileImage.setOnClickListener(this);

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
        edtUserName.addTextChangedListener(textWatcher);
        edtFirstName.addTextChangedListener(textWatcher);
        edtLastName.addTextChangedListener(textWatcher);

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
        mtoolbarTitle.setText(getResources().getString(R.string.registrationcomplete));

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtRegister:
                if (validate() == false) {
                    return;
                }
                saveToPassingData();

                break;
            case R.id.iv_ProfileImage:
                selectImage();
                break;
        }


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

    private void saveToPassingData() {
        Intent intent
                = new Intent(this, CompleteAccountActivity.class);


        Bundle bundle = new Bundle();
        bundle.putString("email", email);
        bundle.putString("password", password);
        bundle.putString("mobile", mobile);
        bundle.putString("username", edtUserName.getText().toString());
        bundle.putString("firstname", edtFirstName.getText().toString());
        bundle.putString("lastname", edtLastName.getText().toString());
        bundle.putString("mediaPath", mediaPath);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    public boolean validate() {
        boolean valid = true;
        String userName = edtUserName.getText().toString();
        String firstname = edtFirstName.getText().toString();
        String lastname = edtLastName.getText().toString();

        if (userName.isEmpty()) {
            edtUserName.setError(getString(R.string.v_username));
            valid = false;
            requestFocus(edtUserName);
        } else if (firstname.isEmpty()) {
            valid = false;
            edtFirstName.setError(getString(R.string.v_firstname));
            requestFocus(edtFirstName);
        } else if (lastname.isEmpty()) {
            valid = false;
            edtLastName.setError(getString(R.string.v_lastname));
            requestFocus(edtLastName);
        }
        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    private void checkFieldsForEmptyValues() {
        txtRegister.setOnClickListener(this);
        userName = edtUserName.getText().toString();
        firstName = edtFirstName.getText().toString();
        lastName = edtLastName.getText().toString();

        if (userName.length() > 2 && firstName.length() > 1 && lastName.length() > 2) {
            loginDark();
        } else {
            loginTrans();
        }
    }

    public void loginTrans() {
        txtRegister.setEnabled(false);
        txtRegister.setBackground(getResources().getDrawable(R.drawable.ed_border_clear));
        txtRegister.setTextColor(getResources().getColor(R.color.black));
    }

    public void loginDark() {
        txtRegister.setEnabled(true);
        txtRegister.setBackgroundColor(getResources().getColor(R.color.tab_bottom));
        txtRegister.setTextColor(getResources().getColor(R.color.white));
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationCompleteActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = myUtility.checkPermission(RegistrationCompleteActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MyUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent
        startActivityForResult(galleryIntent, 1);


     /*   Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/
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

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

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
        mediaPath = String.valueOf(destination);
//        ciProfileImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));


        ciProfileImage.setImageBitmap(thumbnail);


//        Glide.with(this).load("file://" + mediaPath).
//                thumbnail(Glide.with(this).load(R.drawable.loading)).crossFade().into(ciProfileImage);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        // Get the Image from data
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};


        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        mediaPath = cursor.getString(columnIndex);
        // Set the Image in ImageView for Previewing the Media
//        ciProfileImage.setImageBitmap(BitmapFactory.decodeFile(mediaPath));

      /*  Glide.with(this).load("file://" + mediaPath).
                thumbnail(Glide.with(this).load(R.drawable.loading)).crossFade().into(ciProfileImage);*/
        Glide.with(RegistrationCompleteActivity.this)
                .load(mediaPath)
                .asBitmap()
                .placeholder(R.drawable.loading)
                .error(R.drawable.noimage)
                .into(ciProfileImage);


        cursor.close();
    }


    public void download(final String mediaPath1) {
        APILogin api = ApiClient.getClient().create(APILogin.class);
        api.downlload(mediaPath1).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    // display the image data in a ImageView or save it
                    Bitmap thumbnail = BitmapFactory.decodeStream(response.body().byteStream());

                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

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

                    mediaPath = String.valueOf(destination);
                    if (mediaPath != null) {


                        ciProfileImage.setImageBitmap(thumbnail);

//                        Glide.with(RegistrationCompleteActivity.this).load("file://" + mediaPath).
//                                thumbnail(Glide.with(RegistrationCompleteActivity.this).load(R.drawable.loading)).into(ciProfileImage);
                    }

                } else {
                    // TODO
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(RegistrationCompleteActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
