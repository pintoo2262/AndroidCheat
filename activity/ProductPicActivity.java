package com.app.noan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.model.Product;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductPicActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private Button btnNext, btnReTake;

    private Product mProduct;
    private String strCondtion, strIssue, strOtherissue, strSize, strPrice;
    private ImageView ivProductListImage;
    MyUtility myUtility;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1777;
    private String userChoosenTask;
    CircleImageView ci_product1, ivProduct2, ivProduct3, ivProduct4, ivProduct5, ivProduct6, ivProduct7, ivBox1, ivBox2;
    static int pos = 1;
    String mediaPath;
    String ivPath1, ivPath2, ivPath3, ivPath4, ivPath5, ivPath6, ivPath7, ivBoxPath1, ivBoxPath2;
    ArrayList<String> productImageList;
    ArrayList<String> boxImageList;
    TextView txtProductImageCount, txtBoxImageCount;
    ArrayList<Integer> staticBox = new ArrayList<>();
    ArrayList<Integer> staticProduct = new ArrayList<>();
    int boxclick = 0;
    int productClick = 0;

    @Override

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pic);


        mProduct = (Product) getIntent().getSerializableExtra("searchProduct");
        strCondtion = getIntent().getStringExtra("box_condition");
        strIssue = getIntent().getStringExtra("issues");
        strOtherissue = getIntent().getStringExtra("other_issues");
        strSize = getIntent().getStringExtra("defaultSize");
        strPrice = getIntent().getStringExtra("productPrice");

        toolBarAndDrawerInitilization();

        initialize();
        Glide.with(this).load(mProduct.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductListImage);

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
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.black));
        mtoolbarTitle.setText(getResources().getString(R.string.product_listing));
    }

    private void initialize() {
        myUtility = new MyUtility(ProductPicActivity.this);
        ivProductListImage = (ImageView) findViewById(R.id.ivProductlistiImage);
        ci_product1 = (CircleImageView) findViewById(R.id.iv_p1);
        ivProduct2 = (CircleImageView) findViewById(R.id.iv_p2);
        ivProduct3 = (CircleImageView) findViewById(R.id.iv_p3);
        ivProduct4 = (CircleImageView) findViewById(R.id.iv_p4);
        ivProduct5 = (CircleImageView) findViewById(R.id.iv_p5);
        ivProduct6 = (CircleImageView) findViewById(R.id.iv_p6);
        ivProduct7 = (CircleImageView) findViewById(R.id.iv_p7);
        ivBox1 = (CircleImageView) findViewById(R.id.iv_b1);
        ivBox2 = (CircleImageView) findViewById(R.id.iv_b2);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ci_product1);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct2);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct3);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct4);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct5);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct6);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct7);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivBox1);
        Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivBox2);
        txtProductImageCount = (TextView) findViewById(R.id.productImageCount);
        txtBoxImageCount = (TextView) findViewById(R.id.boxImageCount);
        btnReTake = (Button) findViewById(R.id.btn_retake);


        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        ci_product1.setOnClickListener(this);
        ivProduct2.setOnClickListener(this);
        ivProduct3.setOnClickListener(this);
        ivProduct4.setOnClickListener(this);
        ivProduct5.setOnClickListener(this);
        ivProduct6.setOnClickListener(this);
        ivProduct7.setOnClickListener(this);
        ivBox1.setOnClickListener(this);
        ivBox2.setOnClickListener(this);
        btnReTake.setOnClickListener(this);

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
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                if (ivPath1 == null) {
                    Toast.makeText(this, "Please Select product image 1", Toast.LENGTH_SHORT).show();
                } else if (ivPath2 == null) {
                    Toast.makeText(this, "Please Select product image 2", Toast.LENGTH_SHORT).show();
                } else if (ivPath3 == null) {
                    Toast.makeText(this, "Please Select product image 3", Toast.LENGTH_SHORT).show();
                } else if (ivPath4 == null) {
                    Toast.makeText(this, "Please Select product image 4", Toast.LENGTH_SHORT).show();
                } else if (ivPath5 == null) {
                    Toast.makeText(this, "Please Select product image 5", Toast.LENGTH_SHORT).show();
                } else if (ivPath6 == null) {
                    Toast.makeText(this, "Please Select product image 6", Toast.LENGTH_SHORT).show();
                } else if (ivPath7 == null) {
                    Toast.makeText(this, "Please Select product image 7", Toast.LENGTH_SHORT).show();
                } else if (ivBoxPath1 == null) {
                    Toast.makeText(this, "Please Select BoxImage 1", Toast.LENGTH_SHORT).show();
                } else if (ivBoxPath2 == null) {
                    Toast.makeText(this, "Please Select BoxImage 2", Toast.LENGTH_SHORT).show();
                } else {
                    productImageList = new ArrayList<>();
                    boxImageList = new ArrayList<>();
                    productImageList.add(ivPath1);
                    productImageList.add(ivPath2);
                    productImageList.add(ivPath3);
                    productImageList.add(ivPath4);
                    productImageList.add(ivPath5);
                    productImageList.add(ivPath6);
                    productImageList.add(ivPath7);
                    boxImageList.add(ivBoxPath1);
                    boxImageList.add(ivBoxPath2);
                    Intent intent
                            = new Intent(ProductPicActivity.this, SubmitListingActivity.class);
                    intent.putExtra("searchProduct", mProduct);
                    intent.putExtra("box_condition", strCondtion);
                    intent.putExtra("issues", strIssue);
                    intent.putExtra("other_issues", strOtherissue);
                    intent.putExtra("defaultSize", strSize);
                    intent.putExtra("productPrice", strPrice);
                    intent.putExtra("productImage", productImageList);
                    intent.putExtra("boxlistImage", boxImageList);

                   /* //
                    intent.putExtra("productListImage1", ivPath1);
                    intent.putExtra("productListImage2", ivPath2);
                    intent.putExtra("productListImage3", ivPath3);
                    intent.putExtra("productListImage4", ivPath4);
                    intent.putExtra("productListImage5", ivPath5);
                    intent.putExtra("productListImage6", ivPath6);
                    intent.putExtra("productListImage7", ivPath7);
                    intent.putExtra("boxlistImage1", ivBoxPath1);
                    intent.putExtra("boxlistImage2", ivBoxPath2);*/
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }

                break;
            case R.id.btn_retake:
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ci_product1);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct2);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct3);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct4);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct5);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct6);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivProduct7);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivBox1);
                Glide.with(ProductPicActivity.this).load(R.drawable.camera_icon).into(ivBox2);
                boxclick = 0;
                productClick = 0;
                ivPath1 = null;
                ivPath2 = null;
                ivPath3 = null;
                ivPath4 = null;
                ivPath5 = null;
                ivPath6 = null;
                ivPath7 = null;
                ivBoxPath1 = null;
                ivBoxPath2 = null;
                staticBox.clear();
                staticProduct.clear();
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");
                txtBoxImageCount.setText(staticBox.size() + "/" + "2");

                break;
            case R.id.iv_p1:
                selectImage();
                pos = 1;
                break;
            case R.id.iv_p2:
                selectImage();
                pos = 2;
                break;
            case R.id.iv_p3:
                selectImage();
                pos = 3;
                break;
            case R.id.iv_p4:
                selectImage();
                pos = 4;
                break;
            case R.id.iv_p5:
                selectImage();
                pos = 5;
                break;
            case R.id.iv_p6:
                selectImage();
                pos = 6;
                break;
            case R.id.iv_p7:
                selectImage();
                pos = 7;
                break;
            case R.id.iv_b1:
                pos = 8;
                selectImage();
                break;
            case R.id.iv_b2:
                pos = 9;
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductPicActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = myUtility.checkPermission(ProductPicActivity.this);
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
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP

            Uri tempUri = getImageUri(getApplicationContext(), photo);

            String path = getRealPathFromURI(tempUri, this);

            if (pos == 1) {
                ci_product1.setImageBitmap(photo);
                ivPath1 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(1)) {
                    } else {
                        staticProduct.add(1);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 2) {
                ivProduct2.setImageBitmap(photo);
                ivPath2 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(2)) {
                    } else {
                        staticProduct.add(2);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 3) {
                ivProduct3.setImageBitmap(photo);
                ivPath3 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(3)) {
                    } else {
                        staticProduct.add(3);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 4) {
                ivProduct4.setImageBitmap(photo);
                ivPath4 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(4)) {
                    } else {
                        staticProduct.add(4);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 5) {
                ivProduct5.setImageBitmap(photo);
                ivPath5 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(5)) {
                    } else {
                        staticProduct.add(5);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 6) {
                ivProduct6.setImageBitmap(photo);
                ivPath6 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(6)) {
                    } else {
                        staticProduct.add(6);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 7) {
                ivProduct7.setImageBitmap(photo);
                ivPath7 = path;
                if (productClick != pos) {
                    if (staticProduct.contains(7)) {
                    } else {
                        staticProduct.add(7);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 8) {
                ivBox1.setImageBitmap(photo);
                ivBoxPath1 = path;
                if (boxclick != pos) {
                    if (staticBox.contains(1)) {
                    } else {
                        staticBox.add(1);
                    }
                    boxclick = pos;
                }
                txtBoxImageCount.setText(staticBox.size() + "/" + 2);

            } else if (pos == 9) {
                ivBox2.setImageBitmap(photo);
                ivBoxPath2 = path;
                if (boxclick != pos) {
                    if (staticBox.contains(2)) {
                    } else {
                        staticBox.add(2);
                    }
                    boxclick = pos;
                }
                txtBoxImageCount.setText(staticBox.size() + "/" + 2);
            } else {
                ci_product1.setImageBitmap(photo);
                ivPath1 = path;
            }


        } else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();

            String[] filePath = {MediaStore.Images.Media.DATA};

            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);

            c.moveToFirst();

            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            mediaPath = picturePath;
            // Set the Image in ImageView for Previewing the Media


            if (pos == 1) {
                ivPath1 = mediaPath;
                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ci_product1);
                if (productClick != pos) {
                    if (staticProduct.contains(1)) {
                    } else {
                        staticProduct.add(1);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 2) {
                ivPath2 = mediaPath;

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct2);
                if (productClick != pos) {
                    if (staticProduct.contains(2)) {
                    } else {
                        staticProduct.add(2);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 3) {
                ivPath3 = mediaPath;

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct3);
                if (productClick != pos) {
                    if (staticProduct.contains(3)) {
                    } else {
                        staticProduct.add(3);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 4) {
                ivPath4 = mediaPath;
                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct4);

                if (productClick != pos) {
                    if (staticProduct.contains(4)) {
                    } else {
                        staticProduct.add(4);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");


            } else if (pos == 5) {
                ivPath5 = mediaPath;

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct5);

                if (productClick != pos) {
                    if (staticProduct.contains(5)) {
                    } else {
                        staticProduct.add(5);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");
            } else if (pos == 6) {
                ivPath6 = mediaPath;
                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct6);
                if (productClick != pos) {
                    if (staticProduct.contains(6)) {
                    } else {
                        staticProduct.add(6);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 7) {
                ivPath7 = mediaPath;

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivProduct7);

                if (productClick != pos) {
                    if (staticProduct.contains(7)) {
                    } else {
                        staticProduct.add(7);
                    }
                    productClick = pos;
                }
                txtProductImageCount.setText(staticProduct.size() + "/" + "7");

            } else if (pos == 8) {
                ivBoxPath1 = mediaPath;

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivBox1);

                if (boxclick != pos) {
                    if (staticBox.contains(1)) {

                    } else {
                        staticBox.add(1);
                    }
                    boxclick = pos;
                }
                txtBoxImageCount.setText(staticBox.size() + "/" + 2);

            } else if (pos == 9) {

                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ivBox2);
                ivBoxPath2 = mediaPath;
                if (boxclick != pos) {
                    if (staticBox.contains(2)) {

                    } else {
                        staticBox.add(2);
                    }

                    boxclick = pos;
                }
                txtBoxImageCount.setText(staticBox.size() + "/" + 2);
            } else {
                Glide.with(ProductPicActivity.this)
                        .load(mediaPath)
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(ci_product1);
                ivPath1 = mediaPath;
            }
            c.close();
        } else {

        }


    }


    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }


}
