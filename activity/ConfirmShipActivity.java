package com.app.noan.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.APIError;
import com.app.noan.model.NeedToConfirmModel;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConfirmShipActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;

    private Button btnGeneratLable, btnMarkShipped, btnShipYes, btnShipNo;

    private Dialog camera;

    private TextView tvCameraCancel;
    private CircleImageView ciCamera, ciGallery;
    ImageView ivProductImage;
    String type, imageName;

    NeedToConfirmModel needToConfirmModel;
    TextView txtOrderDate, txtOrderNumber, txtName, txtSku, txtSize, txtBox, txtDefect, txtProductPrice, txtShippingPrice, txtUsedWallet, txtTotalAmount, txtConfirmNote, txtBuyerName;
    String imagePath;
    Dialog pDialog;

    private int GALLERY = 1, CAMERA = 2;
    byte[] imageBytes;
    View view;
    LinearLayout llDefect, llBox;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ship);

        needToConfirmModel = (NeedToConfirmModel) getIntent().getSerializableExtra("NeedToConfirmModel");
        type = MyUtility.getSavedPreferences(ConfirmShipActivity.this, "sellerType");
        toolBarAndDrawerInitilization();

        initialize();
    }

    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.ship));
    }

    private void initialize() {
        btnGeneratLable = (Button) findViewById(R.id.btn_UPS_Lable);
        btnMarkShipped = (Button) findViewById(R.id.btn_mark_shipped);
        ivProductImage = (ImageView) findViewById(R.id.ivProducTImage);

        txtOrderDate = (TextView) findViewById(R.id.txtOrderDateTime);
        txtOrderNumber = (TextView) findViewById(R.id.txtOrderNo);
        txtName = (TextView) findViewById(R.id.txtProductName);
        txtSku = (TextView) findViewById(R.id.txtProductSku);
        txtSize = (TextView) findViewById(R.id.txtProductSize);
        txtBox = (TextView) findViewById(R.id.txtBoxCondtion);
        txtDefect = (TextView) findViewById(R.id.txtDefect);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtShippingPrice = (TextView) findViewById(R.id.txtShippingPrice);
        txtUsedWallet = (TextView) findViewById(R.id.txtUsedWallet);
        txtTotalAmount = (TextView) findViewById(R.id.txtorderTotal);
        txtConfirmNote = (TextView) findViewById(R.id.tv_congratulations_note);
        txtBuyerName = (TextView) findViewById(R.id.txtBuyerUserName);
        txtConfirmNote.setText("If you have shipped the product, Update status here");

        view = (View) findViewById(R.id.lastBottomView);
        llBox = (LinearLayout) findViewById(R.id.llBox);
        llDefect = (LinearLayout) findViewById(R.id.llDefect);

        btnGeneratLable.setOnClickListener(this);
        btnMarkShipped.setOnClickListener(this);
//        String date = MyUtility.getConvertDate(needToConfirmModel.getCreated());
        String date = needToConfirmModel.getCreated();
        txtOrderDate.setText(date);
        txtOrderNumber.setText(needToConfirmModel.getId());
        txtName.setText(needToConfirmModel.getProductName());
        txtSku.setText(needToConfirmModel.getProductSku());
        txtSize.setText(needToConfirmModel.getSValue());
        txtProductPrice.setText("$ " + needToConfirmModel.getTotal());
        txtShippingPrice.setText("$ " + needToConfirmModel.getShippingRate());
        txtUsedWallet.setText("$ " + needToConfirmModel.getUsedWalletBalance());
        txtTotalAmount.setText("$ " + needToConfirmModel.getGrandTotal());
        txtBox.setText(needToConfirmModel.getBoxCondition());
        txtDefect.setText(needToConfirmModel.getIssues());
        txtBuyerName.setText(needToConfirmModel.getBuyerUserName());

        if (type.equals("vendorseller")) {
            llBox.setVisibility(View.GONE);
            llDefect.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else {
            txtBox.setText(needToConfirmModel.getBoxCondition());
            txtDefect.setText(needToConfirmModel.getIssues());
        }

        Glide.with(this).load(needToConfirmModel.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_UPS_Lable:


                break;

            case R.id.btn_mark_shipped:
                camera = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
                camera.setContentView(R.layout.layout_camera);
                camera.setCancelable(false);
                imagePath = "";

                tvCameraCancel = (TextView) camera.findViewById(R.id.tv_cancel);
                tvCameraCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        camera.dismiss();
                    }
                });

                btnShipYes = (Button) camera.findViewById(R.id.btn_ship_yes);
                btnShipNo = (Button) camera.findViewById(R.id.btn_ship_no);
                ciCamera = camera.findViewById(R.id.iv_camera);
                ciGallery = camera.findViewById(R.id.iv_gallery);
                btnShipYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (imagePath != null && !imagePath.equals("")) {
                            needToShip_ByServer();
                        } else {
                            Toast.makeText(ConfirmShipActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                        }
                        camera.dismiss();
                    }
                });

                btnShipNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        camera.dismiss();
                    }
                });
                ciCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA);

                    }
                });
                ciGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(galleryIntent, GALLERY);
                    }
                });

                camera.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.help, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_help:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
                    imageBytes = baos.toByteArray();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);

                    Glide.with(ConfirmShipActivity.this).load(R.drawable.camera).into(ciCamera);
                    ciGallery.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 75, baos); //bm is the bitmap object
            imageBytes = baos.toByteArray();

            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");

            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(baos.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imagePath = String.valueOf(destination);

            Glide.with(ConfirmShipActivity.this).load(R.drawable.gallery).into(ciGallery);
            ciCamera.setImageBitmap(thumbnail);
        }
    }


    private void needToShip_ByServer() {
        pDialog = new Dialog(ConfirmShipActivity.this);
        pDialog.getWindow().setContentView(R.layout.custom_progresssdialog);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pDialog.setCancelable(false);
        showDialog();


        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        RequestBody reqOrderId = RequestBody.create(MediaType.parse("text/plain"), needToConfirmModel.getId());
        RequestBody reqOrderStatus = RequestBody.create(MediaType.parse("text/plain"), "shipped");
        MultipartBody.Part fileToUpload = null;
        if (imageBytes != null) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            imageName = timeStamp + ".jpg"; //
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
            fileToUpload = MultipartBody.Part.createFormData("mark_as_shipped", imageName, requestFile);
        }


        Call<APIError> userCall = service.needToShipProduct(reqOrderId, reqOrderStatus, fileToUpload);

        userCall.enqueue(new Callback<APIError>() {
            @Override
            public void onResponse(Call<APIError> call, Response<APIError> response) {
                if (response.isSuccessful()) {
                    hideDialog();
                    APIError apiError = (APIError) response.body();
                    if (apiError.getStatusCode() == 1) {
                        Toast.makeText(ConfirmShipActivity.this, "" + apiError.getMessage(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ConfirmShipActivity.this, "" + apiError.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(ConfirmShipActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.putExtra("moveScreenType", "SellerFragment");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<APIError> call, Throwable t) {
                call.cancel();
                hideDialog();
                Toast.makeText(ConfirmShipActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
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


}
