package com.app.noan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.noan.R;
import com.app.noan.activity.HomeActivity;
import com.app.noan.activity.TermAndConditionsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by smn on 23/10/17.
 */

@SuppressLint("ValidFragment")
public class FragmentOne extends Fragment {

    private RecyclerView rv_sandleAnimation;
    private ArrayList<Integer> animationImageList;
    private int[] images = new int[]{R.drawable.browsing, R.drawable.browsing, R.drawable.browsing, R.drawable.browsing, R.drawable.browsing};
    AdvertimentAdapter advertimentAdapter;
    private int scrollCount = 0;
    TextView txtStart;
    Activity mContext;

    public FragmentOne() {
    }

    @SuppressLint("ValidFragment")
    public FragmentOne(Activity context) {
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);


        // initilzation of xml componet
        initialization(view);
//        customTextView(txtStart);


        return view;

    }

    private void initialization(View view) {
        rv_sandleAnimation = view.findViewById(R.id.rv_sandleAnimation);
        txtStart = view.findViewById(R.id.txtStartBrowsing);
        animationImageList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            animationImageList.add(images[i]);
        }
        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
            }
        });
        setDataAdvertisment();
    }

    private void setDataAdvertisment() {
        advertimentAdapter = new AdvertimentAdapter(animationImageList, getActivity()) {
            @Override
            public void load() {
                animationImageList.addAll(animationImageList);
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(mContext) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rv_sandleAnimation.setLayoutManager(layoutManager);
        rv_sandleAnimation.setHasFixedSize(true);
        rv_sandleAnimation.setAdapter(advertimentAdapter);
        autoScroll();


    }

   /* public static FragmentOne newInstance() {
        FragmentOne signupTouristFragment = new FragmentOne();
        return signupTouristFragment;
    }
*/

    public abstract class AdvertimentAdapter extends RecyclerView.Adapter<AdvertimentAdapter.ViewHolderAdvertisment> {

        ArrayList<Integer> advertimentModelArrayList;
        Context mactivity;

        public abstract void load();

        public AdvertimentAdapter(ArrayList<Integer> advertisement_list, Context mActivity) {
            advertimentModelArrayList = advertisement_list;
            mactivity = mActivity;
        }

        @Override
        public AdvertimentAdapter.ViewHolderAdvertisment onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_rvadvertisment, parent, false);
            AdvertimentAdapter.ViewHolderAdvertisment viewHolderAdvertisment = new AdvertimentAdapter.ViewHolderAdvertisment(view);
            return viewHolderAdvertisment;
        }

        @Override
        public void onBindViewHolder(AdvertimentAdapter.ViewHolderAdvertisment holder, int position) {
            Glide.with(mactivity.getApplicationContext())
                    .load(advertimentModelArrayList.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageViewIcon);

        }

        @Override
        public int getItemCount() {
            return advertimentModelArrayList.size();
        }

        public class ViewHolderAdvertisment extends RecyclerView.ViewHolder {
            ImageView imageViewIcon;


            public ViewHolderAdvertisment(View itemView) {
                super(itemView);
                imageViewIcon = (ImageView) itemView.findViewById(R.id.iv_imageAdvertisement);
            }
        }

    }

    public void autoScroll() {
        scrollCount = 0;
        final int speedScroll = 1;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (scrollCount == advertimentAdapter.getItemCount()) {
                    advertimentAdapter.load();
                    advertimentAdapter.notifyDataSetChanged();
                }
                rv_sandleAnimation.smoothScrollToPosition((scrollCount++));
                handler.postDelayed(this, speedScroll);
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }

    private void customTextView(TextView view) {


        SpannableStringBuilder spanTxt = new SpannableStringBuilder();
        spanTxt.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view1) {
                Intent intent
                        = new Intent(getActivity(), TermAndConditionsActivity.class);
                startActivity(intent);
            }
        }, spanTxt.length() - getResources().getString(R.string.nameapp).length(), spanTxt.length(), 0);
        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanTxt, TextView.BufferType.SPANNABLE);


    }


}
