package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.LoginActivity;
import com.app.noan.activity.RegistrationStep1Activity;
import com.app.noan.utils.MyUtility;


public class RequestToSellFragment extends Fragment implements View.OnClickListener {

    ConstraintLayout constraintLayout, constrialayoutwithoutLogin;

    Button btnLogin, btnRegistration;
    String isUserSeller, userId;
    SellerFragment mSellerFragment;
    Request_sell_Fragment mRequestSellFragment;
    Activity activity;
    boolean mUserVisibleHint = true;


    public RequestToSellFragment() {
    }

    @SuppressLint("ValidFragment")
    public RequestToSellFragment(HomeActivity homeActivity) {
        activity = homeActivity;
    }


    public static Fragment newInstance() {
        return new RequestToSellFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reques_to_sell, container, false);

        isUserSeller = MyUtility.getSavedPreferences(getActivity(), "sellerType");

        MyUtility.onResumeScreenType = 3;
        initalization(view);


        return view;
    }

    private void initalization(View view) {
        constraintLayout = view.findViewById(R.id.constrianlayoutRequesttoSell);
        constrialayoutwithoutLogin = view.findViewById(R.id.withoutRequestlink);
        btnLogin = view.findViewById(R.id.btnIncludeLogin);
        btnRegistration = view.findViewById(R.id.btnIncludeRegister);

        btnLogin.setOnClickListener(this);
        btnRegistration.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mUserVisibleHint) {
            visible();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mUserVisibleHint) {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnIncludeLogin:
                Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.btnIncludeRegister:
                Intent intent2 = new Intent(getActivity(), RegistrationStep1Activity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                getActivity().finish();
                break;
        }
    }


    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        mUserVisibleHint = visible;
        if (mUserVisibleHint && isResumed()) {
            visible();

        }
    }

    void visible() {
        isUserSeller = MyUtility.getSavedPreferences(getActivity(), "sellerType");
        userId = MyUtility.getSavedPreferences(getActivity(), "id");
        if (!userId.equals("")) {
            constrialayoutwithoutLogin.setVisibility(View.INVISIBLE);
            constraintLayout.setVisibility(View.VISIBLE);
            if (isUserSeller.equals("isUserSell")) {
                mSellerFragment = new SellerFragment();
                mSellerFragment.setArguments(userId, isUserSeller);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameRequestToSell, mSellerFragment);
                fragmentTransaction.commit();
            } else if (isUserSeller.equals("vendorseller")) {
                mSellerFragment = new SellerFragment();
                mSellerFragment.setArguments(userId, isUserSeller);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameRequestToSell, mSellerFragment);
                fragmentTransaction.commit();
            } else if (isUserSeller.equals("unapproved")) {
                mRequestSellFragment = new Request_sell_Fragment(activity, getActivity());
                mRequestSellFragment.setArguments(userId);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameRequestToSell, mRequestSellFragment);
                fragmentTransaction.commit();
                mRequestSellFragment.setUserVisible();
            } else if (isUserSeller.equals("unapprove")) {
                mRequestSellFragment = new Request_sell_Fragment(activity, getActivity());
                mRequestSellFragment.setArguments(userId);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameRequestToSell, mRequestSellFragment);
                fragmentTransaction.commit();
                mRequestSellFragment.setUserVisible();
            }
        } else {
            constrialayoutwithoutLogin.setVisibility(View.VISIBLE);
            constraintLayout.setVisibility(View.INVISIBLE);
        }
    }

}
