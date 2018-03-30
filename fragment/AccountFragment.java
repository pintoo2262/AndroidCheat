package com.app.noan.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.noan.R;
import com.app.noan.activity.PaymentActivity;

/**
 * Created by smn on 20/9/17.
 */

public class AccountFragment extends Fragment {

    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initialize(view);

        return view;
    }

    private void initialize(View view) {
    }

    public void setArguments(PaymentActivity paymentActivity) {
        mActivity = paymentActivity;
    }
}
