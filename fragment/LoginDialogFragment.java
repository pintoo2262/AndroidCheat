package com.app.noan.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.noan.R;
import com.app.noan.activity.LoginActivity;
import com.app.noan.activity.ProductDetailsActivity;
import com.app.noan.activity.RegistrationStep1Activity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by smn on 2/12/17.
 */

public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {
    RecyclerView rv_animationList;
    Activity activity;
    private ArrayList<Integer> animationImageList;
    private int[] images = new int[]{R.drawable.browsing, R.drawable.browsing, R.drawable.browsing, R.drawable.browsing, R.drawable.browsing};
    AdvertimentAdapter advertimentAdapter;
    private int scrollCount = 0;
    Button btnDialogLogin, btnDialogRegister;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loginfragment, container);
        initilization(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }

    private void initilization(View view) {
        rv_animationList = view.findViewById(R.id.rv_animation);
        btnDialogLogin = view.findViewById(R.id.btnDialogLogin);
        btnDialogRegister = view.findViewById(R.id.btnDialogRegister);

        btnDialogLogin.setOnClickListener(this);
        btnDialogRegister.setOnClickListener(this);

        animationImageList = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            animationImageList.add(images[i]);
        }
        setDataAdvertisment();
    }

    private void setDataAdvertisment() {
        advertimentAdapter = new AdvertimentAdapter(animationImageList, getActivity()) {
            @Override
            public void load() {
                animationImageList.addAll(animationImageList);
            }
        };
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(activity) {
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
        rv_animationList.setLayoutManager(layoutManager);
        rv_animationList.setHasFixedSize(true);
        rv_animationList.setAdapter(advertimentAdapter);
        autoScroll();


    }

    public void setArguments(ProductDetailsActivity productDetailsActivity) {
        activity = productDetailsActivity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDialogLogin:
                Intent intent
                        = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                activity.finish();
                break;
            case R.id.btnDialogRegister:
                Intent intent1
                        = new Intent(activity, RegistrationStep1Activity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                activity.finish();
                break;
        }
    }

    public abstract class AdvertimentAdapter extends RecyclerView.Adapter<AdvertimentAdapter.ViewHolderAdvertisment> {

        ArrayList<Integer> advertimentModelArrayList;
        Activity mactivity;

        public abstract void load();

        public AdvertimentAdapter(ArrayList<Integer> advertisement_list, Activity mActivity) {
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
                rv_animationList.smoothScrollToPosition((scrollCount++));
                handler.postDelayed(this, speedScroll);
            }
        };
        handler.postDelayed(runnable, speedScroll);
    }


}
