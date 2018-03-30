package com.app.noan.activity;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.noan.R;
import com.app.noan.helper.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LargeView extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener {


    TouchImageView ivZoom;

    ViewPager viewPager;
    PagerAdapter adapter;
    RelativeLayout rlIndicator;
    LinearLayout llIndaicator;
    private int dotsCount;
    private ImageView[] dots;

    private int mCurrentPosition;
    private int mScrollState;

    List<String> mImProductsImage;
    Integer index;
    ImageView leftNav, rightNav, ivClose;
    RelativeLayout mClose;

    //    Imagezoom property
    private static final String TAG = "Touch";

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.largeview);
        mImProductsImage = new ArrayList<>();
        mImProductsImage = (List<String>) getIntent().getSerializableExtra("tempImageList");
        index = getIntent().getIntExtra("index", 0);

        //get Product id form details view page


        //assign id to relavent view and create object
        rlIndicator = (RelativeLayout) findViewById(R.id.rl_Indicator);
        llIndaicator = (LinearLayout) findViewById(R.id.ll_Indicator_Dots);
        leftNav = (ImageView) findViewById(R.id.left_nav);
        rightNav = (ImageView) findViewById(R.id.right_nav);
        ivClose = (ImageView) findViewById(R.id.iv_LargeClose);
        mClose = (RelativeLayout) findViewById(R.id.rl_TopClose);


        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //call large view method
        LargeView();

        // Images move left navigation in view pager
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

        // Images move right navigation in view pager
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void LargeView() {

        viewPager = (ViewPager) findViewById(R.id.vp_LargeImagePager);
        adapter = new ViewPagerAdapter(LargeView.this, mImProductsImage);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
        viewPager.setOnPageChangeListener(this);
        //Method for show indicator below images
//        setUIPageController();


    }

    // Handle the indicator for view pager
    @SuppressWarnings("deprecation")
    private void setUIPageController() {
        dotsCount = adapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dots));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            llIndaicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected_dots));
    }

    // 3 Methods for view pager onPageChangeListner
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;

       /* for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_selected_dots));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected_dots));*/
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        handleScrollState(state);
        mScrollState = state;

    }

    private void handleScrollState(final int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setNextItemIfNeeded();
        }
    }

    private void setNextItemIfNeeded() {
        if (!isScrollStateSettling()) {
            handleSetNextItem();
        }
    }

    private boolean isScrollStateSettling() {
        return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
    }

    private void handleSetNextItem() {
        final int lastPosition = viewPager.getAdapter().getCount() - 1;
        if (mCurrentPosition == 0) {
            viewPager.setCurrentItem(lastPosition, false);
        } else if (mCurrentPosition == lastPosition) {
            viewPager.setCurrentItem(0, false);
        }
    }

    //=====================For Pinch to Zoom Images
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        // make the image scalable as a matrix
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_POINTER_DOWN: //second finger down
                oldDist = spacing(event); // calculates the distance between two points where user touched.
                Log.d(TAG, "oldDist=" + oldDist);
                // minimal distance between both the fingers
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched.
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) { //movement of first finger
                    matrix.set(savedMatrix);
                    if (view.getLeft() >= -392) {
                        matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                    }
                } else if (mode == ZOOM) { //pinch zooming
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / oldDist; //thinking I need to play around with this value to limit it**
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        // Perform the transformation
        view.setImageMatrix(matrix);

        return true; // indicate event was handled
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
//=========================End of Zoom

    // PagerAdapter class
    public class ViewPagerAdapter extends PagerAdapter {

        List<String> mImageList;
        LayoutInflater inflater;
        Context mContext;


        public ViewPagerAdapter(LargeView context, List<String> mImageList) {
            this.mContext = context;
            this.mImageList = mImageList;
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView imgflag;

            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.largepagerview, container,
                    false);


            final TouchImageView imageView = (TouchImageView) itemView.findViewById(R.id.iv_ZoomImage);

            Picasso.with(mContext)
                    .load(mImageList.get(position))
                    .placeholder(R.drawable.loading)
                    .into(imageView);


            imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
                @Override
                public void onMove() {
                    PointF point = imageView.getScrollPosition();
                    RectF rect = imageView.getZoomedRect();
                    float currentZoom = imageView.getCurrentZoom();
                    boolean isZoomed = imageView.isZoomed();
                }
            });

            ((ViewPager) container).addView(itemView);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((LinearLayout) object);
        }
    }


}
