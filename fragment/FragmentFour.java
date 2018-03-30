package com.app.noan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.noan.R;

/**
 * Created by smn on 23/10/17.
 */

public class FragmentFour extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        // initilzation of xml componet
        initialization(view);
        return view;

    }

    private void initialization(View view) {
    }

    public static FragmentFour newInstance() {
        FragmentFour fragmentFour = new FragmentFour();
        return fragmentFour;
    }

}
