package com.bibsindia.bibstraderpanel.utils;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import com.bibsindia.bibstraderpanel.R;


public class NetworkUtils {
    public static boolean isConnected(Activity mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();
        if (netinfo == null || !netinfo.isConnectedOrConnecting()) {
            Utils.ShowAlert(mActivity,"No Internet Connection","You need to have Mobile Data or wifi to access this. Press ok to Exit",true);
            return false;
        }
        NetworkInfo wifi = cm.getNetworkInfo(1);
        NetworkInfo mobile = cm.getNetworkInfo(0);
        if (mobile != null && mobile.isConnectedOrConnecting()) {
            return true;
        }
        return wifi != null && wifi.isConnectedOrConnecting();
    }
}
