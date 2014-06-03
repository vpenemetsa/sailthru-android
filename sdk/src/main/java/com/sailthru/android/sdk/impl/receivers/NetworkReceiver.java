package com.sailthru.android.sdk.impl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.api.NetworkQueue;

import javax.inject.Inject;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Listens for system broadcasts about network status.
 */
public class NetworkReceiver extends BroadcastReceiver {

    public NetworkReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        AuthenticatedClient client = AuthenticatedClient.getInstance(context);
        client.setConnectedToNetwork(networkInfo != null &&
                networkInfo.isConnectedOrConnecting());

        if (client.isConnectedToNetwork()) {
            NetworkQueue.registerCachedAttemptIfAvailable(context.getApplicationContext(),
                    client);
        }
    }
}