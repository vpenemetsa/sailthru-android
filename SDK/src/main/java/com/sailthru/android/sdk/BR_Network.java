package com.sailthru.android.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BR_Network extends BroadcastReceiver {

    public BR_Network() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        ST_AuthenticatedClient client = ST_AuthenticatedClient.getInstance(context);
        client.setConnectedToNetwork(networkInfo != null && networkInfo.isConnectedOrConnecting());

        if (client.isConnectedToNetwork()) {
            API_Queue.registerCachedAttemptIfAvailable(context.getApplicationContext(), client);
        }
    }
}