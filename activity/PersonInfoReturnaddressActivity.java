package com.app.noan.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.app.noan.adapters.PlaceArrayAdapter;
import com.app.noan.helper.RightDrawableOnTouchListener;
import com.app.noan.model.ReuestSellerModelResponse;
import com.app.noan.retrofit_api.APIRequestSell;
import com.app.noan.retrofit_api.ApiClient;
import com.app.noan.utils.MyUtility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PersonInfoReturnaddressActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, OnMapReadyCallback, LocationListener {

    private String TAG = PersonInfoReturnaddressActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private TextView mtoolbarTitle;
    private Button btnContinue;
    private AutoCompleteTextView mAutocompleteView;
    private EditText edtTextAddress;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    private GoogleApiClient mGoogleApiClient, mGoogleApiClient1;
    private static final int REQUEST_SOLVE_CONNEXION = 999;

    // Location
    private LocationRequest mlocationRequest;
    private Location mlastLocation;

    private final int REQ_PERMISSION = 999;
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;


    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private TextWatcher textWatcher;
    String strAdderssline1, strAdderssline2;


    private String shAddress1, shAddress2;
    RelativeLayout mRelativeLayout;
    private String userId;
    private Marker currentlocationMaker;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_person_info_returnaddress);
        Log.d(TAG, "onCreateView()");

        toolBarAndDrawerInitilization();
        MyUtility.onResumeSellerScreenType = 2;
        // initilzation of xml componet
        initialization();
        initGMaps();
        userId = MyUtility.getSavedPreferences(getApplicationContext(), "id");
        shAddress1 = getIntent().getStringExtra("IntetAddress1");
        shAddress2 = getIntent().getStringExtra("IntetAddress2");


        if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
            //We get a connection to the Google Play Service API to get the location of the user
            createGoogleApi();

        } else {
            GooglePlayServicesUtil.getErrorDialog(GooglePlayServicesUtil.isGooglePlayServicesAvailable(this),
                    this,
                    REQUEST_SOLVE_CONNEXION);
        }


        mGoogleApiClient1 = new GoogleApiClient.Builder(PersonInfoReturnaddressActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteView.setAdapter(mPlaceArrayAdapter);
        mAutocompleteView.setOnTouchListener(new RightDrawableOnTouchListener(mAutocompleteView) {
            @Override
            public boolean onDrawableTouch(MotionEvent event) {
                mAutocompleteView.setText("");
                return true;
            }
        });


        if (shAddress1 != null) {
            edtTextAddress.setText(shAddress1);
        }
        if (shAddress2 != null) {
            mAutocompleteView.setText(shAddress2);
            mAutocompleteView.setSelection(mAutocompleteView.length());
        }


    }

    private void initGMaps() {
        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);
    }


    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Log.d(mapTAG, "onMapReady()");
        mGoogleMap = googleMap;
        // Showing / hiding your current location
        mGoogleMap.setMyLocationEnabled(true);
        // Enable / Disable my location button
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        // Enable / Disable Compass icon
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        // Enable / Disable zooming functionality
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        // Enable / Disable Rotate gesture
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        // Enable / Disable zooming functionality
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMapToolbarEnabled(true);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
        mGoogleMap.getUiSettings().isTiltGesturesEnabled();
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setIndoorEnabled(false);
        mGoogleMap.setBuildingsEnabled(true);
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address != null && !address.isEmpty()) {
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();

                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                if (latLng != null) {
                    dragmarkerLocation(latLng);
                } else {
                    dragmarkerLocation(new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude()));
                }
                //zoom to current position:
            } else {
                dragmarkerLocation(new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude()));
                return null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return latLng;
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient1, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());

                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
            String address = mAutocompleteView.getText().toString();


            if (mAutocompleteView.getText().toString() != null) {
                mAutocompleteView.setSelection(0);
                getLocationFromAddress(PersonInfoReturnaddressActivity.this, address);
            }


        }
    };


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        // Call GoogleApiClient connection when starting the Activity
     /*   if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (mlastLocation != null) {
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude())).bearing(360).
                                zoom(14f).tilt(90).build();
                        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        if (shAddress2 != null && !shAddress2.equals("")) {
                            getLocationFromAddress(PersonInfoReturnaddressActivity.this, shAddress2);
                        } else {
                            dragmarkerLocation(new LatLng(mlastLocation.getLatitude(), mlastLocation.getLongitude()));
                        }
//                    hideDialog();
                    }

                }
            };
            handler.postDelayed(runnable, 2000);
        }*/

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
        // Disconnect GoogleApiClient when stopping Activity
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }


    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }
    }

    // Start location Updates
    private void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates()");
        mlocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mlocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        mlastLocation = location;
        writeActualLocation(location);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected()");
        getLastKnownLocation();
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient1);


    }

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();

    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            mlastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mlastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + mlastLocation.getLongitude() +
                        " | Lat: " + mlastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void writeActualLocation(Location location) {
    }

    private void writeLastLocation() {
        writeActualLocation(mlastLocation);
    }

    private void dragmarkerLocation(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .snippet("Current Address").draggable(true);
        if (mGoogleMap != null) {
            currentlocationMaker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }


    private void initialization() {
        btnContinue = (Button) findViewById(R.id.btnPersoninfoaddressContinue);
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextview);

        edtTextAddress = (EditText) findViewById(R.id.edt_Apartment);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.loadItemsLayout_listView);
        mAutocompleteView.setThreshold(2);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        //set listeners
        edtTextAddress.addTextChangedListener(textWatcher);


    }

    private void checkFieldsForEmptyValues() {
        btnContinue.setOnClickListener(this);
        strAdderssline1 = edtTextAddress.getText().toString();
        strAdderssline2 = mAutocompleteView.getText().toString();
        if (strAdderssline1.length() > 2 && strAdderssline2.length() > 2) {
            btnContinue.setTextColor(getResources().getColor(R.color.btn_bgwhite));
            btnContinue.setEnabled(true);
            btnContinue.setVisibility(View.VISIBLE);

        } else {
            btnContinue.setEnabled(false);
            btnContinue.setVisibility(View.VISIBLE);
            btnContinue.setTextColor(getResources().getColor(R.color.btn_reqsell));
        }
    }


    private void toolBarAndDrawerInitilization() {
        mToolbar = (Toolbar) findViewById(R.id.mtoolbar);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mtoolbarTitle = mToolbar.findViewById(R.id.txt_toolbar);
        mtoolbarTitle.setText(getResources().getString(R.string.returnaddress));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.requestSell));
        mtoolbarTitle.setTextColor(getResources().getColor(R.color.btn_bgwhite));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(item));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPersoninfoaddressContinue:
                if (validate() == false) {
                    return;
                } else {
                    PersonalReturnaddressInfoByserver();
                    finish();
                }
                break;
        }
    }

    public boolean validate() {
        boolean valid = true;
        if (edtTextAddress.getText().toString().equals("")) {
            edtTextAddress.setError("Enter valid  Address");
            valid = false;

        } else if (mAutocompleteView.getText().toString().equals("")) {
            mAutocompleteView.setError("Enter valid  Address");
            valid = false;

        }
        return valid;
    }


    private void PersonalReturnaddressInfoByserver() {
        mRelativeLayout.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.GONE);

        shAddress1 = edtTextAddress.getText().toString();
        shAddress2 = mAutocompleteView.getText().toString();
        APIRequestSell service = ApiClient.getClient().create(APIRequestSell.class);
        Map<String, String> credMap = new HashMap<>();
        credMap.put("user_id", userId);
        credMap.put("termsagree", "");
        credMap.put("full_name", "");
        credMap.put("phone_number", "");
        credMap.put("birth_date", "");
        credMap.put("address1", shAddress1);
        credMap.put("address2", shAddress2);
        credMap.put("seller_info", "");
        credMap.put("facebook_link", "");
        credMap.put("twitter_link", "");
        credMap.put("insta_link", "");
        credMap.put("submit_type", "return_address");
        Call<ReuestSellerModelResponse> call = service.pInfoRequestToSell(credMap);
        call.enqueue(new Callback<ReuestSellerModelResponse>() {
            @Override
            public void onResponse(Call<ReuestSellerModelResponse> call, Response<ReuestSellerModelResponse> response) {
                if (response.isSuccessful()) {
                    ReuestSellerModelResponse sellerModelResponse = ((ReuestSellerModelResponse) response.body());
                    mRelativeLayout.setVisibility(View.GONE);
                    btnContinue.setVisibility(View.VISIBLE);
                    if (sellerModelResponse.getStatus() == 1) {
                        Intent intentMessage = new Intent();
                        // Set The Result in Intent
                        setResult(2, intentMessage);
                        // finish The activity
                        finish();
                    } else {
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<ReuestSellerModelResponse> call, Throwable t) {
                mRelativeLayout.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
                call.cancel();
                Toast.makeText(PersonInfoReturnaddressActivity.this, "Please check your network connection and internet permission" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
