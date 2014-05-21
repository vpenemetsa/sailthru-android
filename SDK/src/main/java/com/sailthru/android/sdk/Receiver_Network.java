package com.sailthru.android.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Receiver_Network extends BroadcastReceiver {

    public Receiver_Network() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        St_AuthenticatedClient client = St_AuthenticatedClient.getInstance(context);
        client.setConnectedToNetwork(networkInfo != null && networkInfo.isConnectedOrConnecting());

        if (client.isConnectedToNetwork()) {
            Api_Queue.registerCachedAttemptIfAvailable(context.getApplicationContext(), client);
        }
    }
}