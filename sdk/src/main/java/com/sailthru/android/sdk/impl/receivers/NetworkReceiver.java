package com.sailthru.android.sdk.impl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.api.NetworkQueue;

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

        broadcastNetworkStatus(context, client.isConnectedToNetwork());
    }

    private void broadcastNetworkStatus(Context context, boolean value) {
        Log.d("Network receiver value", value + "");
        Intent i = new Intent(Constants.BROADCAST_NETWORK_STATUS);
        i.putExtra(Constants.INTENT_EXTRA_NETWORK_STATUS, value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}