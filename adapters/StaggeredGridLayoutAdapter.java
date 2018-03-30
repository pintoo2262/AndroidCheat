package com.app.noan.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.noan.R;
import com.app.noan.model.instagram.Edge;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by smn on 16/9/17.
 */

public class StaggeredGridLayoutAdapter extends CollectionRecyclerViewAdapter {
    private Activity activity;
    private int screenWidth;
    List<Edge> collectionedgeList;


    public StaggeredGridLayoutAdapter(Activity activity, List<Edge> imagesLIst) {
        this.activity = activity;
        this.collectionedgeList = imagesLIst;
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
    }


    @Override
    public CustomRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity)
                .inflate(R.layout.rv_collection_custom_image, parent, false);
        Holder dataObjectHolder = new Holder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CustomRecycleViewHolder holder, final int position) {
        final Holder myHolder = (Holder) holder;


        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;

//        BitmapFactory.decodeFile(collectionList.get(position), opts);
        opts.inJustDecodeBounds = false;
        int height;
        if (position == 1 || position == (collectionedgeList.size() - 1)) {
            height = 150;
        } else {
            height = 300;
        }

        Glide.with(activity).load(collectionedgeList.get(position).getNode().getDisplayUrl())
                .error(R.drawable.home_icon)
                .placeholder(R.drawable.home_icon)
                .override(screenWidth / 2, height)
                .centerCrop()
                .into((myHolder.images));

    }

    @Override
    public int getItemCount() {
        return collectionedgeList.size();
    }

    public class Holder extends CustomRecycleViewHolder {
        private ImageView images;

        public Holder(View itemView) {
            super(itemView);
            images = (ImageView) itemView.findViewById(R.id.ivItemGridImage);
        }
    }
}
