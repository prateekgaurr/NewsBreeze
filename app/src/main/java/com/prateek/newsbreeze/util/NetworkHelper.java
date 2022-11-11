package com.prateek.newsbreeze.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {
    public static boolean isNetworkConnected(Context application){
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        MyLogger.d("NETWORK STATUS", "Found to be "+ isConnected);
        return isConnected;
    }
}
