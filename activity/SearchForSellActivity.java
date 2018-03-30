package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.USSizeAdapter;
import com.app.noan.helper.ObjectSerializer;
import com.app.noan.helper.SpacesItemDecoration_horizontal;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.Product;
import com.app.noan.model.SizeModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchForSellActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Toolbar mToolbar;
    private TextView mtoolbarTitle;

    private RecyclerView rvSaleCondtion;
    private List<SizeModel> sizeModelArrayList;

    private Button btnNext;
    private Product mProduct;
    TextView txtProductName;
    ImageView ivSearchProduct;

    private USSizeAdapter usSizeAdapter;
    private String stDefaultSize, stBoxcondtion = "", strIssue, stOtherIssue, stDisplaySize;
    private Switch swGoodCondtion, swMissingLtd, swDamaged, swOrignal,
            sw1, sw2, sw3, sw4, sw5, sw6;
    private EditText edtOtherIssue;
    private TextView txtGoodCondtion, txtMissLtd, txtDamaged, txtNoOriganl,
            txtIssue1, txtIssue2, txtIssue3, txtIssue4, txtIssue5, txtIssue6;

    private List<String> mIssueList;
    SharedPreferences prefs;


    @Override

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_sell);

        toolBarAndDrawerInitilization();
        prefs = this.getSharedPreferences("yourPrefsKey", Context.MODE_PRIVATE);
        mProduct = (Product) getIntent().getSerializableExtra("searchProduct");

        initilization();

        setSizeRecyclerview();
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
        mtoolbarTitle.setText(getResources().getString(R.string.sell_for));
    }

    private void initilization() {
        rvSaleCondtion = (RecyclerView) findViewById(R.id.rv_product_size);
        ivSearchProduct = (ImageView) findViewById(R.id.iv_SearchProduct);
        txtProductName = (TextView) findViewById(R.id.tv_sell_item_title);
        edtOtherIssue = (EditText) findViewById(R.id.otherIssue);
        sizeModelArrayList = new ArrayList<>();
        try {
            sizeModelArrayList = (ArrayList) ObjectSerializer.deserialize(prefs.getString("SizeList", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }


        mIssueList = new ArrayList<>();
        swGoodCondtion = (Switch) findViewById(R.id.switchGoodCondtion);
        swMissingLtd = (Switch) findViewById(R.id.switch_missingltd);
        swDamaged = (Switch) findViewById(R.id.switch_damagedbox);
        swOrignal = (Switch) findViewById(R.id.swich_duplicatebox);

        sw1 = (Switch) findViewById(R.id.switch_hasorder);
        sw2 = (Switch) findViewById(R.id.switch_discoloration);
        sw3 = (Switch) findViewById(R.id.switch_missinginsoles);
        sw4 = (Switch) findViewById(R.id.swich_marking);
        sw5 = (Switch) findViewById(R.id.swich_bgrade);
        sw6 = (Switch) findViewById(R.id.swich_cuttear);

        txtGoodCondtion = (TextView) findViewById(R.id.txtGoodCondtion);
        txtMissLtd = (TextView) findViewById(R.id.txtMissLtd);
        txtDamaged = (TextView) findViewById(R.id.txtDamaged);
        txtNoOriganl = (TextView) findViewById(R.id.txtNoOrignal);


        txtIssue1 = (TextView) findViewById(R.id.txtHasOrder);
        txtIssue2 = (TextView) findViewById(R.id.txtDiscoloratration);
        txtIssue3 = (TextView) findViewById(R.id.txtMissing);
        txtIssue4 = (TextView) findViewById(R.id.txtMarking);
        txtIssue5 = (TextView) findViewById(R.id.txtBGrade);
        txtIssue6 = (TextView) findViewById(R.id.txtcutOrTear);


        swGoodCondtion.setOnCheckedChangeListener(this);
        swMissingLtd.setOnCheckedChangeListener(this);
        swDamaged.setOnCheckedChangeListener(this);
        swOrignal.setOnCheckedChangeListener(this);

        sw1.setOnCheckedChangeListener(this);
        sw2.setOnCheckedChangeListener(this);
        sw3.setOnCheckedChangeListener(this);
        sw4.setOnCheckedChangeListener(this);
        sw5.setOnCheckedChangeListener(this);
        sw6.setOnCheckedChangeListener(this);

        txtProductName.setText(mProduct.getProduct_name());
        Glide.with(this).load(mProduct.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivSearchProduct);

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
    }


    private void setSizeRecyclerview() {
        rvSaleCondtion.setHasFixedSize(true);
        usSizeAdapter = new USSizeAdapter(SearchForSellActivity.this, sizeModelArrayList);
        rvSaleCondtion.setLayoutManager(new LinearLayoutManager(SearchForSellActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvSaleCondtion.addItemDecoration(new SpacesItemDecoration_horizontal(14));
        rvSaleCondtion.setAdapter(usSizeAdapter);
        rvSaleCondtion.setNestedScrollingEnabled(false);

        rvSaleCondtion.addOnItemTouchListener(new RecyclerTouchListener(this, rvSaleCondtion, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                stDefaultSize = sizeModelArrayList.get(position).getSizeId();
                stDisplaySize = sizeModelArrayList.get(position).getSizeValue();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            case R.id.menu_credit:
                String message = "";
                Intent intent;
                int applicationNameId = SearchForSellActivity.this.getApplicationInfo().labelRes;
                final String appPackageName = SearchForSellActivity.this.getPackageName();
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                String text = "" + mProduct.getProduct_name() + "\n";
                String link = "http://projects-beta.com/noan_new/webservices/user/appshare?product_id=" + mProduct.getProduct_id();
                Uri appLinkData = Uri.parse(link);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check this out on Noan");
                intent.putExtra(Intent.EXTRA_TEXT, text + " " + appLinkData);
                startActivity(Intent.createChooser(intent, "Share link:"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                stOtherIssue = edtOtherIssue.getText().toString();
                if (stDefaultSize == null) {
                    Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                } else if (!swGoodCondtion.isChecked() && !swMissingLtd.isChecked() && !swDamaged.isChecked() && !swOrignal.isChecked()) {
                    Toast.makeText(this, "Please Select One Box Condtion", Toast.LENGTH_SHORT).show();
                } else {
                    strIssue = setCommaSeparateIssueList(mIssueList);
                    Intent intent
                            = new Intent(SearchForSellActivity.this, PriceSetActivity.class);
                    intent.putExtra("searchProduct", mProduct);
                    intent.putExtra("box_condition", stBoxcondtion);
                    intent.putExtra("issues", strIssue);
                    intent.putExtra("other_issues", stOtherIssue);
                    intent.putExtra("defaultSize", stDefaultSize);
                    intent.putExtra("displaySize", stDisplaySize);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }


                break;
        }
    }

    private String setCommaSeparateIssueList(List<String> mIssueList) {
        StringBuilder sType4 = new StringBuilder();

        for (int i = 0; i < mIssueList.size(); i++) {
            //append the value into the builder
            sType4.append(mIssueList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != mIssueList.size() - 1) {
                sType4.append(",");
            }
        }
        return sType4.toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switchGoodCondtion:
                // Do something
                if (b) {
                    stBoxcondtion = txtGoodCondtion.getText().toString();
                    swGoodCondtion.setChecked(true);
                    swMissingLtd.setChecked(false);
                    swDamaged.setChecked(false);
                    swOrignal.setChecked(false);
                } else {

                    swGoodCondtion.setChecked(false);
                }

                break;
            case R.id.switch_missingltd:
                // Do something
                if (b) {
                    stBoxcondtion = txtMissLtd.getText().toString();
                    swGoodCondtion.setChecked(false);
                    swMissingLtd.setChecked(true);
                    swDamaged.setChecked(false);
                    swOrignal.setChecked(false);
                } else {

                    swMissingLtd.setChecked(false);
                }
                break;
            case R.id.switch_damagedbox:
                // Do something
                if (b) {
                    stBoxcondtion = txtDamaged.getText().toString();
                    swGoodCondtion.setChecked(false);
                    swMissingLtd.setChecked(false);
                    swDamaged.setChecked(true);
                    swOrignal.setChecked(false);
                } else {

                    swDamaged.setChecked(false);
                }
                break;
            case R.id.swich_duplicatebox:
                // Do something
                if (b) {
                    stBoxcondtion = txtNoOriganl.getText().toString();
                    swGoodCondtion.setChecked(false);
                    swMissingLtd.setChecked(false);
                    swDamaged.setChecked(false);
                    swOrignal.setChecked(true);
                } else {

                    swOrignal.setChecked(false);
                }
                break;

            case R.id.switch_hasorder:
                // Do something
                if (b) {
                    mIssueList.add(txtIssue1.getText().toString());

                   /* sw1.setChecked(true);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(false);*/

                } else {
                    mIssueList.remove(txtIssue1.getText().toString());
//                    sw1.setChecked(false);

                }
                break;
            case R.id.switch_discoloration:
                // Do something
                if (b) {
                   /* strIssue = txtIssue2.getText().toString();
                    sw1.setChecked(false);
                    sw2.setChecked(true);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(false);*/

                    mIssueList.add(txtIssue2.getText().toString());
                } else {
                    mIssueList.remove(txtIssue2.getText().toString());
//                    sw2.setChecked(false);
                }
                break;

            case R.id.switch_missinginsoles:
                // Do something
                if (b) {
                   /* strIssue = txtIssue3.getText().toString();
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(true);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(false);*/
                    mIssueList.add(txtIssue3.getText().toString());

                } else {
                    /*sw3.setChecked(false);*/
                    mIssueList.remove(txtIssue3.getText().toString());
                }
                break;
            case R.id.swich_marking:
                // Do something
                if (b) {
                   /* strIssue = txtIssue4.getText().toString();
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(true);
                    sw5.setChecked(false);
                    sw6.setChecked(false);*/

                    mIssueList.add(txtIssue4.getText().toString());

                } else {
//                    sw4.setChecked(false);
                    mIssueList.remove(txtIssue4.getText().toString());

                }
                break;
            case R.id.swich_bgrade:
                // Do something
                if (b) {
                   /* strIssue = txtIssue5.getText().toString();
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(true);
                    sw6.setChecked(false);*/

                    mIssueList.add(txtIssue5.getText().toString());
                } else {
                    /*sw5.setChecked(false);*/
                    mIssueList.add(txtIssue5.getText().toString());
                }
                break;
            case R.id.swich_cuttear:
                // Do something
                if (b) {
                    /*strIssue = txtIssue6.getText().toString();
                    sw1.setChecked(false);
                    sw2.setChecked(false);
                    sw3.setChecked(false);
                    sw4.setChecked(false);
                    sw5.setChecked(false);
                    sw6.setChecked(true);*/
                    mIssueList.add(txtIssue6.getText().toString());
                } else {
//                    sw6.setChecked(false);
                    mIssueList.remove(txtIssue6.getText().toString());
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
