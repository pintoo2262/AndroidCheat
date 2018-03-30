package com.app.noan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.IncompleteListActivity;
import com.app.noan.activity.ListingActivity;
import com.app.noan.activity.NeedToConfirmActivity;
import com.app.noan.activity.NeedToShipActivity;
import com.app.noan.activity.OrderActivity;
import com.app.noan.activity.PaymentHistoryActivity;
import com.app.noan.activity.ReceivedOfferForSellActivity;
import com.app.noan.activity.ShippedActivtiy;
import com.app.noan.adapters.SellMenuAdapter;
import com.app.noan.helper.SimpleDividerItemDecoration;
import com.app.noan.listener.RecyclerTouchListener;
import com.app.noan.model.SellMenu;

import java.util.ArrayList;

/**
 * Created by smn on 29/12/17.
 */

public class SellerFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar;
    TextView tvTbTitle;
    AppBarLayout appbar;
    String userId, sellerType;
    private RecyclerView rvSellMenu;
    private SellMenuAdapter sellMenudapter;
    private ImageButton ibtnCredit;

    private final String sellMenuList[] = {
            "Listing", "Incomplete Listing", "Need to Confirm",
            "Received Offers for Sell", "Need to Ship", "Shipped",
            "Orders", "Payment"
    };
    private final String sellMenuList1[] = {
            "Listing", "Need to Confirm",
            "Received Offers for Sell", "Need to Ship", "Shipped",
            "Orders", "Payment"
    };
    private ArrayList<SellMenu> sellMenuItem;

    public SellerFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller, container, false);

        toolBarAndDrawerInitilization(view);
        initalization(view);

        if (sellerType.equals("vendorseller")) {
            sellMenuItem = getSellMenu1();
            sellListMenu1RecyclerBind();
        } else {
            sellMenuItem = getSellMenu();
            sellListMenuRecyclerBind();
        }


        return view;
    }

    private void initalization(View view) {
        rvSellMenu = (RecyclerView) view.findViewById(R.id.rv_sell_menu);
    }

    private void toolBarAndDrawerInitilization(View view) {

        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        toolbar = (Toolbar) appbar.findViewById(R.id.mtoolbar);
        ((HomeActivity) getActivity()).setSupportActionBar(toolbar);
        tvTbTitle = (TextView) toolbar.findViewById(R.id.txt_toolbar);
        ibtnCredit = toolbar.findViewById(R.id.ibtn_credit);
        ibtnCredit.setVisibility(View.INVISIBLE);
        tvTbTitle.setText(getResources().getString(R.string.sell_title));
        setHasOptionsMenu(true);
        assert ((HomeActivity) getActivity()).getSupportActionBar() != null;
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        /*((HomeActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.home_icon);*/
        ibtnCredit.setOnClickListener(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_credit:
                break;
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private ArrayList<SellMenu> getSellMenu() {
        ArrayList<SellMenu> sellMenuItem = new ArrayList<>();
        for (int i = 0; i < sellMenuList.length; i++) {
            SellMenu sellMenu = new SellMenu();
            sellMenu.setSellMenuTitle(sellMenuList[i]);
            sellMenuItem.add(sellMenu);
        }
        return sellMenuItem;
    }

    private ArrayList<SellMenu> getSellMenu1() {
        ArrayList<SellMenu> sellMenuItem = new ArrayList<>();
        for (int i = 0; i < sellMenuList1.length; i++) {
            SellMenu sellMenu = new SellMenu();
            sellMenu.setSellMenuTitle(sellMenuList1[i]);
            sellMenuItem.add(sellMenu);
        }
        return sellMenuItem;
    }

    private void sellListMenuRecyclerBind() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSellMenu.setLayoutManager(manager);
        sellMenudapter = new SellMenuAdapter(getActivity(), sellMenuItem);
        rvSellMenu.setAdapter(sellMenudapter);
        rvSellMenu.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        rvSellMenu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvSellMenu, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), ListingActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), IncompleteListActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), NeedToConfirmActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), ReceivedOfferForSellActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), NeedToShipActivity.class));
                        break;

                    case 5:
                        startActivity(new Intent(getActivity(), ShippedActivtiy.class));
                        break;

                    case 6:
                        startActivity(new Intent(getActivity(), OrderActivity.class));
                        break;
                    case 7:
                        startActivity(new Intent(getActivity(), PaymentHistoryActivity.class));
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void sellListMenu1RecyclerBind() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSellMenu.setLayoutManager(manager);
        sellMenudapter = new SellMenuAdapter(getActivity(), sellMenuItem);
        rvSellMenu.setAdapter(sellMenudapter);
        rvSellMenu.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        rvSellMenu.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rvSellMenu, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), ListingActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), NeedToConfirmActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(getActivity(), ReceivedOfferForSellActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(getActivity(), NeedToShipActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), ShippedActivtiy.class));
                        break;
                    case 5:
                        startActivity(new Intent(getActivity(), OrderActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(getActivity(), PaymentHistoryActivity.class));
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn_credit:
                Intent intent
                        = new Intent(getActivity(), PaymentHistoryActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void setArguments(String userId, String sellerType) {
        this.userId = userId;
        this.sellerType = sellerType;
    }
}
