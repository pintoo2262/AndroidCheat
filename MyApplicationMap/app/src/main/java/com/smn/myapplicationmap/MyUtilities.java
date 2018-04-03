package com.smn.myapplicationmap;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by smn on 9/5/17.
 */

public class MyUtilities {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static boolean isInternetConnencted(Context context) {
        int conn = MyUtilities.getConnectivityStatus(context);

        if (conn == MyUtilities.TYPE_WIFI) {
            return true;
        } else if (conn == MyUtilities.TYPE_MOBILE) {
            return true;
        } else if (conn == MyUtilities.TYPE_NOT_CONNECTED) {
            return false;
        }
        return false;

    }
}
