package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.ProductBuyActivity;
import com.app.noan.model.Offer;
import com.app.noan.model.OrderLastSold;
import com.app.noan.model.SpinnerObject;
import com.app.noan.utils.MyUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tistory.dwfox.dwrulerviewlibrary.utils.DWUtils;
import com.tistory.dwfox.dwrulerviewlibrary.view.ObservableHorizontalScrollView;
import com.tistory.dwfox.dwrulerviewlibrary.view.ScrollingValuePicker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by smn on 5/10/17.
 */
@SuppressLint("ValidFragment")
public class OfferDialogFragment extends DialogFragment implements View.OnClickListener {

    Dialog offerDialog;
    TextView txtOfferPrice, txtOfferDialogToolbar, txtLowestPrice, txtTopPrice;
    ImageView ivProductImage;


    RecyclerView rvLastSoldPrice;
    public List<OrderLastSold> mlastSoldList;
    LastSoldAdapter mlastSoldAdapter;
    private GridLayoutManager lLayout;

    Button btnNext;
    ImageView dialoClose;

    private ScrollingValuePicker myScrollingValuePicker;
    private static float MIN_VALUE = 5;
    private static float MAX_VALUE = 100;
    private static float Center_VALUE;
    private static final float LINE_RULER_MULTIPLE_SIZE = 4.5f;


    Spinner spinnerSelectionDay;
    public ArrayList<SpinnerObject> CustomListViewValuesArr = new ArrayList<SpinnerObject>();
    public ArrayList CustomListArrDisplay = new ArrayList();
    CustomSpinnerAdapter adapter;

    Offer mOffer;
    public String ProducId, sizeId, SizeValue, stPrice, stExp;

    Double cMin;
    int iMin, minPrice, maxPrice, middelvalue;


    public OfferDialogFragment(String s, String sizeId, String sizeValue, Offer Offer) {
        ProducId = s;
        this.sizeId = sizeId;
        this.SizeValue = sizeValue;
        mOffer = Offer;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_dialog_layout, container);

        initilization(view);
        stExp = "0";

       /* if (!mOffer.getData().getLowOfferPrice().equals("0")) {
            if (!mOffer.getData().getLowOfferPrice().equals("")) {
                txtLowestPrice.setText("$" + mOffer.getData().getLowOfferPrice());
                minPrice = 25;
                maxPrice = Integer.parseInt(mOffer.getData().getLowOfferPrice());
                middelvalue = Integer.parseInt(mOffer.getData().getLowOfferPrice());
            } else {
                txtLowestPrice.setText("-");
                minPrice = 25;
                maxPrice = 2000;
                middelvalue = 25;
            }

        } else {
            txtLowestPrice.setText("$" + mOffer.getData().getSeller_min_price());
            minPrice = 25;
            maxPrice = Integer.parseInt(mOffer.getData().getSeller_min_price());
            middelvalue = Integer.parseInt(mOffer.getData().getSeller_min_price());
        }
*/

        if (!mOffer.getData().getSeller_min_price().equals("0")) {
            if (!mOffer.getData().getSeller_min_price().equals("")) {
                txtLowestPrice.setText("$" + mOffer.getData().getSeller_min_price());
                minPrice = 25;
                maxPrice = Integer.parseInt(mOffer.getData().getSeller_min_price());
                middelvalue = Integer.parseInt(mOffer.getData().getSeller_min_price());
            } else {
                txtLowestPrice.setText("$ 0");
                minPrice = 25;
                maxPrice = 2000;
                middelvalue = 25;
            }

        } else {
            txtLowestPrice.setText("$ 0");
            minPrice = 25;
            maxPrice = 2000;
            middelvalue = 25;
        }


        if (!mOffer.getData().getTopOfferPrice().equals("") && !mOffer.getData().getTopOfferPrice().equals("0")) {
            txtTopPrice.setText("$" + mOffer.getData().getTopOfferPrice());
        } else {
            txtTopPrice.setText("$ 0");
        }


        Glide.with(this).load(mOffer.getData().getProductData().getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(ivProductImage);

        MIN_VALUE = (float) minPrice;
        MAX_VALUE = (float) maxPrice;


        myScrollingValuePicker.setViewMultipleSize(LINE_RULER_MULTIPLE_SIZE);
        myScrollingValuePicker.setMaxValue(MIN_VALUE, MAX_VALUE);
        myScrollingValuePicker.setValueTypeMultiple(50);
        myScrollingValuePicker.setInitValue(middelvalue);
        txtOfferPrice.setText("$" + middelvalue);
        stPrice = String.valueOf(middelvalue);
        myScrollingValuePicker.getScrollView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    myScrollingValuePicker.getScrollView().startScrollerTask();
                }
                return false;
            }
        });

        myScrollingValuePicker.setOnScrollChangedListener(new ObservableHorizontalScrollView.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ObservableHorizontalScrollView view, int l, int t) {
            }

            @Override
            public void onScrollStopped(int l, int t) {
                txtOfferPrice.setText("$" +
                        DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView()
                                , l
                                , t
                                , MAX_VALUE
                                , MIN_VALUE
                                , myScrollingValuePicker.getViewMultipleSize()));

                stPrice = String.valueOf(DWUtils.getValueAndScrollItemToCenter(myScrollingValuePicker.getScrollView()
                        , l
                        , t
                        , MAX_VALUE
                        , MIN_VALUE
                        , myScrollingValuePicker.getViewMultipleSize()));
            }
        });


        if (CustomListViewValuesArr.size() > 0) {

        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 3);
            Date dateThree = calendar.getTime();
            String dat3 = String.valueOf(dateThree);
            Calendar cal = getCalendar(dat3);
            int threeday = cal.get(Calendar.DATE);
            int threeMonth = (cal.get(Calendar.MONTH) + 1);

            Calendar calendar1 = Calendar.getInstance();
            calendar1.add(Calendar.DATE, 7);
            Date dateSeven = calendar1.getTime();
            String dat7 = String.valueOf(dateSeven);
            Calendar cal7 = getCalendar(dat7);
            int Sevenday = cal7.get(Calendar.DATE);
            int SevenMonth = (cal7.get(Calendar.MONTH) + 1);

            Calendar calendar3 = Calendar.getInstance();
            calendar3.add(Calendar.DATE, 30);
            Date dateThirty = calendar3.getTime();
            String dat30 = String.valueOf(dateThirty);
            Calendar cal30 = getCalendar(dat30);
            int thirtyday = cal30.get(Calendar.DATE);
            int thirtyMonth = (cal30.get(Calendar.MONTH) + 1);


            String threeDay = "3 Days - Expiring    " + threeMonth + "/" + threeday;
            String sevenDay = "7 Days - Expiring    " + SevenMonth + "/" + Sevenday;
            String thirdtyDay = "30 Days - Expiring   " + thirtyMonth + "/" + thirtyday;


            CustomListViewValuesArr.add(0, new SpinnerObject("Expire my Offer In:"));
            CustomListViewValuesArr.add(1, new SpinnerObject(threeDay));
            CustomListViewValuesArr.add(2, new SpinnerObject(sevenDay));
            CustomListViewValuesArr.add(3, new SpinnerObject(thirdtyDay));
        }


        if (CustomListArrDisplay.size() > 0) {

        } else {
            CustomListArrDisplay.add("Expire in 3 days");
            CustomListArrDisplay.add("Expire in 3 days");
            CustomListArrDisplay.add("Expire in 7 days");
            CustomListArrDisplay.add("Expire in 30 days");
        }


        Resources res = getResources();
        adapter = new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, CustomListViewValuesArr, CustomListArrDisplay, res);
        spinnerSelectionDay.setAdapter(adapter);
        spinnerSelectionDay.setSelection(0);
        spinnerSelectionDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    TextView txt = (TextView) view.findViewById(R.id.dropdown_txt);
                    stExp = String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        setDataLastSoldPrice();

        return view;
    }

    @NonNull
    Calendar getCalendar(String dat1) {
        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = null;
        try {
            date = (Date) formatter.parse(dat1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String formatedDate = cal.get(Calendar.DATE) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
        System.out.println("formatedDate : " + formatedDate);
        return cal;
    }


    private void setDataLastSoldPrice() {
        if (mOffer.getData().getOrderData() != null) {
            mlastSoldAdapter = new LastSoldAdapter(getActivity(), mOffer.getData().getOrderData());
            rvLastSoldPrice.setAdapter(mlastSoldAdapter);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvLastSoldPrice.getContext(), DividerItemDecoration.HORIZONTAL);
            dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.vertical_line));
            rvLastSoldPrice.addItemDecoration(dividerItemDecoration);
        }

    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    private void initilization(View view) {
        rvLastSoldPrice = view.findViewById(R.id.rv_offerDialog);
        myScrollingValuePicker = view.findViewById(R.id.myScrollingValuePicker);
        txtOfferDialogToolbar = view.findViewById(R.id.txt_offerDialogToolbar);
        txtLowestPrice = view.findViewById(R.id.txtLowstOfferPrice);
        txtTopPrice = view.findViewById(R.id.txtTofferPrice);
        ivProductImage = (ImageView) view.findViewById(R.id.rv_image);
        txtOfferDialogToolbar.setText("S i z e  : " + SizeValue);
        mlastSoldList = new ArrayList<>();

        txtOfferPrice = view.findViewById(R.id.txt_offer_price);
        spinnerSelectionDay = view.findViewById(R.id.spnOfferExpire);
        dialoClose = view.findViewById(R.id.iv_OfferDialogClose);
        btnNext = view.findViewById(R.id.btnOfferNext);

        //Recycleview
        lLayout = new GridLayoutManager(getActivity(), 2);
        rvLastSoldPrice.setHasFixedSize(true);
        rvLastSoldPrice.setLayoutManager(lLayout);


        //click
        btnNext.setOnClickListener(this);
        dialoClose.setOnClickListener(this);

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color
                .TRANSPARENT));
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        offerDialog = getDialog();
        if (offerDialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            offerDialog.getWindow().setLayout(width, height);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            offerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOfferNext:
                int exp = 0;
                Intent intent
                        = new Intent(getActivity(), ProductBuyActivity.class);
                intent.putExtra("screenType", "Offer");
                intent.putExtra("OfferProduct", (Serializable) mOffer.getData().getProductData());
                intent.putExtra("OfferSizeId", sizeId);
                if (stExp.equals("0")) {
                    exp = 3;
                } else if (stExp.equals("1")) {
                    exp = 7;
                } else if (stExp.equals("2")) {
                    exp = 30;
                }
                intent.putExtra("exp_in", String.valueOf(exp));
                intent.putExtra("selectPrice", (Serializable) stPrice);

                startActivity(intent);
                break;
            case R.id.iv_OfferDialogClose:
                offerDialog.dismiss();
                break;
        }
    }

    private class CustomSpinnerAdapter extends ArrayAdapter<String> {

        private Activity activity;
        private ArrayList data;
        private ArrayList display;
        public Resources res;
        SpinnerObject tempValues = null;
        LayoutInflater inflater;
        String tempDisplay = "";

        public CustomSpinnerAdapter(Activity activitySpinner, int textViewResourceId, ArrayList objects, ArrayList customListArrDisplay, Resources resLocal) {
            super(activitySpinner, textViewResourceId, objects);
            activity = activitySpinner;
            data = objects;
            res = resLocal;
            display = customListArrDisplay;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
            tempValues = null;
            tempValues = (SpinnerObject) data.get(position);
            tempDisplay = (String) display.get(position);

            TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_downarrow, 0);
            txt.setText(tempDisplay);
            txt.setSingleLine(true);
            txt.setEllipsize(TextUtils.TruncateAt.END);
            txt.setSingleLine(true);

            return row;
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
            tempValues = null;
            tempValues = (SpinnerObject) data.get(position);

            TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
            txt.setText(tempValues.getname());
            txt.setPadding(0, 30, 0, 30);
            txt.setGravity(Gravity.CENTER);

            return row;
        }
    }


    private class LastSoldAdapter extends RecyclerView.Adapter<LastSoldAdapter.DailogViewHolder> {
        Context activity;
        List<OrderLastSold> soldArrayList;


        public LastSoldAdapter(Context applicationContext, List<OrderLastSold> selllerProductList) {
            this.activity = applicationContext;
            soldArrayList = selllerProductList;
        }


        public class DailogViewHolder extends RecyclerView.ViewHolder {

            TextView txtLeftPrice, txtRightDate, txtRightPrice, txtLeftDate;
            LinearLayout llLeft, llRight;

            public DailogViewHolder(View itemView) {
                super(itemView);
                txtLeftPrice = (TextView) itemView.findViewById(R.id.txtLeftSoldPrice);
                txtLeftDate = itemView.findViewById(R.id.txtLeftSoldDate);
                txtRightPrice = (TextView) itemView.findViewById(R.id.txtRightSoldPrice);
                txtRightDate = itemView.findViewById(R.id.txtRightSoldDate);
                llLeft = itemView.findViewById(R.id.llSoldLeft);
                llRight = itemView.findViewById(R.id.llSoldRight);
            }
        }


        @Override
        public DailogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_lastprice_sold_layout, parent, false);
            LastSoldAdapter.DailogViewHolder rcv = new LastSoldAdapter.DailogViewHolder(view);
            return rcv;
        }

        @Override
        public void onBindViewHolder(DailogViewHolder holder, int position) {
            String date;
            if (position % 2 == 0) {
                holder.llLeft.setVisibility(View.VISIBLE);
                holder.llRight.setVisibility(View.GONE);
                holder.txtLeftPrice.setText("$ " + soldArrayList.get(position).getTotal());
                date = MyUtility.getConvertCustomDate(soldArrayList.get(position).getCreated());
                holder.txtLeftDate.setText(date);
            } else {
                holder.llLeft.setVisibility(View.GONE);
                holder.llRight.setVisibility(View.VISIBLE);
                holder.txtRightPrice.setText("$ " + soldArrayList.get(position).getTotal());
                date = MyUtility.getConvertCustomDate(soldArrayList.get(position).getCreated());
                holder.txtRightDate.setText(date);
            }
        }


        @Override
        public int getItemCount() {
            return soldArrayList.size();
        }
    }


}
