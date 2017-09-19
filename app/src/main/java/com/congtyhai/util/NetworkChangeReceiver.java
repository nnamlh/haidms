package com.congtyhai.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by HAI on 9/19/2017.
 */

public class NetworkChangeReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if(isOnline(context)) {
            Toast.makeText(context, "Connect internet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Disconnect internet", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
}