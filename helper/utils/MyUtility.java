
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.noan.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by smn on 9/5/17.
 */

public class MyUtility implements Serializable {


    String MAIN_TAG = MyUtility.class.getSimpleName();
    public static int onResumeScreenType = 0;
    public static int onResumeSellerScreenType = 0;

    public static int onSelleScreen = 0;


    public static int screenType = 0;
    public static int screenFilterType = 0;

    //filter count
    public static int selectFilterCount = 0;
    public static int selectSearchFilterCount = 0;
    public static int selectSearchFilterCategoryCount = 0;


    public static List<String> seletedBrandList = new ArrayList<>();
    public static List<String> seletedSizeList = new ArrayList<>();
    public static List<String> seletedCategoryList = new ArrayList<>();
    public static List<String> selectedTypeList = new ArrayList<>();
    public static List<String> selectedColorList = new ArrayList<>();


    //search
    public static List<String> seletedSearchBrandList = new ArrayList<>();
    public static List<String> seletedSearchSizeList = new ArrayList<>();
    public static List<String> seletedSearchCategoryList = new ArrayList<>();
    public static List<String> selectedSearchTypeList = new ArrayList<>();
    public static List<String> selectedSearchColorList = new ArrayList<>();

    //cateigory
    public static List<String> seletedSearchCategoryBrandList = new ArrayList<>();
    public static List<String> seletedSearchCategorySizeList = new ArrayList<>();
    public static List<String> seletedSearchCategoryCategoryList = new ArrayList<>();
    public static List<String> selectedSearchCategoryTypeList = new ArrayList<>();
    public static List<String> selectedSearchCategoryColorList = new ArrayList<>();

    public Activity mActivity;

    public static Dialog customDialog, customDialogValidation;
    // GPS

    LocationManager locationManager;
    public boolean isGPSEnable = false;


    //  Display Size of Width & Height
    Display display;
    Point size;
    int width, height;

    // const
    public static final int MY_PERMISSION_REQUEST = 1;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static boolean location_Success = false;


    public MyUtility(Activity mActivity) {
        this.mActivity = mActivity;
    }


    // Checking Whole App Permission Above23


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public boolean checkSelfPermission() {
        int AccessNetworkStat = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_WIFI_STATE);
        int AccessCorasLocation = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int AccessFineLocation = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int AccessCamera = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        int AccessWrite = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int AccessRead = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int Call = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE);

        List<String> permissionNeeded = new ArrayList<>();

        if (AccessNetworkStat != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (AccessFineLocation != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (AccessCorasLocation != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (AccessRead != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (AccessWrite != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (AccessCamera != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(Manifest.permission.CAMERA);
        }


        if (!permissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(mActivity,
                    permissionNeeded.toArray(new String[permissionNeeded.size()]), MY_PERMISSION_REQUEST);
            return false;
        }
        return true;
    }

    // Checking GPS


    public boolean isGPSEnable() {
        locationManager = (LocationManager) mActivity.getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnable;
    }


    public boolean displayLocationSettingsRequest() {

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {

                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(MAIN_TAG, "All location settings are satisfied.");
                        location_Success = true;
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(MAIN_TAG, "Location settings are not satisfied. " +
                                "Show the user a Dialog to upgrade location settings ");
                        location_Success = false;
                        try {
                            // Show the Dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(MAIN_TAG, "PendingIntent unable to execute request.");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(MAIN_TAG, "Location settings are inadequate, " +
                                "and cannot be fixed here. Dialog not created.");
                        location_Success = false;
                        break;
                }
            }
        });
        return location_Success;
    }

    // Screen Size


    public int getDeviceScreen_Width() {
        display = mActivity.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        width = size.x;
        return width;
    }

    public int getDeviceScreen_height() {
        display = mActivity.getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        height = size.y;
        return height;
    }


    // Checking Interenet


    public boolean isInternetConnect() {
        boolean isConnect = false;
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo ni = cm.getActiveNetworkInfo();
        isConnect = ni != null && ni.isConnectedOrConnecting();

        return isConnect;
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean isNetAvailable = false;
        if (context != null) {
            final ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (mConnectivityManager != null) {
                boolean mobileNetwork = false;
                boolean wifiNetwork = false;

                boolean mobileNetworkConnecetd = false;
                boolean wifiNetworkConnecetd = false;

                final NetworkInfo mobileInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                final NetworkInfo wifiInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mobileInfo != null) {
                    mobileNetwork = mobileInfo.isAvailable();
                }

                if (wifiInfo != null) {
                    wifiNetwork = wifiInfo.isAvailable();
                }

                if (wifiNetwork || mobileNetwork) {
                    if (mobileInfo != null)
                        mobileNetworkConnecetd = mobileInfo
                                .isConnectedOrConnecting();
                    wifiNetworkConnecetd = wifiInfo.isConnectedOrConnecting();
                }

                isNetAvailable = (mobileNetworkConnecetd || wifiNetworkConnecetd);
            }
            /*
             * if (!isNetAvailable) { Util.displayDialog(context,
			 * context.getString(R.string.common_internet), false); }
			 */
        }

        return isNetAvailable;
    }


    // Preferences


    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSavedPreferences(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, "");
    }

    public static void romove(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }





    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.apply();
    }

    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }


    // Validation
    public boolean isValidMobile(String mobile) {
        String phone = "[0-9]{10}";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public boolean isValidEmail(String mailId) {
        String email = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(email);
        Matcher matcher = pattern.matcher(mailId);
        return matcher.matches();
    }

    public void editTextError(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void textInputEditText(final TextInputEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // Date Convert Formate
    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String getConvertDate(String date) {
        try {
            SimpleDateFormat yyyymmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = yyyymmm.parse(date);
            SimpleDateFormat retriveDate = new SimpleDateFormat("dd-MM-yyyy h:mm:s");
            return retriveDate.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getConvertCustomDate(String date) {
        try {
            SimpleDateFormat yyyymmm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date newDate = yyyymmm.parse(date);
            SimpleDateFormat retriveDate = new SimpleDateFormat("dd-MM-yyyy");
            return retriveDate.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getConvertDate1(String date) {
        try {
            SimpleDateFormat yyyymmm = new SimpleDateFormat("dd-MM-yyyy");
            Date newDate = yyyymmm.parse(date);
            SimpleDateFormat retriveDate = new SimpleDateFormat("yyyy-MM-dd");
            return retriveDate.format(newDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }


    // Image related

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public static String getRealPathFromURI(Uri uri, Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    public static Bitmap getBitmap(String filepath) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(filepath);
        return bitmap;
    }


    //  ListView Scroll Issue Solve
    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }


    // Location

    @SuppressLint("LongLogTag")
    public String getCompleteAddressString(Context context, LatLng LATITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE.latitude, LATITUDE.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public List<Address> getGeocoderAddress(Context context, LatLng latLng) {
        if (latLng != null) {

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            try {
                /**
                 * Geocoder.getFromLocation - Returns an array of Addresses
                 * that are known to describe the area immediately surrounding the given latitude and longitude.
                 */
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

                Log.d(MAIN_TAG, String.valueOf(addresses));
                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
                Log.e(MAIN_TAG, "Impossible to connect to Geocoder", e);
            }
        }

        return null;
    }

    public String getLocality(Context context, LatLng latLng) {

        List<Address> addresses = getGeocoderAddress(context, latLng);

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();
            Log.e(MAIN_TAG, locality);
            return locality;

        } else {
            return null;
        }
    }


    //custom Toast


    public void setMessage(String message, int duration) {
        Toast.makeText(mActivity, message, duration).show();
    }


    // Hash Key

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    public static void showCustomMeassge(final Context activity, String btntext, String title, String msg) {

        final TextView dialogTitle, dialogMsg;
        final Button btnCancel;
        customDialog = new Dialog(activity);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.setContentView(R.layout.custom_dialog_msg);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        dialogTitle = customDialog.findViewById(R.id.txt_dialogTitle);
        dialogMsg = customDialog.findViewById(R.id.txt_dialogMsg);
        btnCancel = customDialog.findViewById(R.id.btnCancel);
        dialogTitle.setText(title);
        dialogMsg.setText(msg);
        btnCancel.setText(btntext);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnCancel.getText().toString().equals("Ok")) {
                    customDialog.dismiss();
                } else {
                    customDialog.dismiss();
                    showCustomMeassge(activity, "Ok", "Authentication Failed ", "Authentication was canceled by the user");
                }

            }
        });

        if (!((Activity) activity).isFinishing()) {
            //show custome_deleteDialog
            customDialog.show();
        }


    }


}
