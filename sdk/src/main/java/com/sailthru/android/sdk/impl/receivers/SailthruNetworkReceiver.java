package com.sailthru.android.sdk.impl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.api.NetworkQueue;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Listens for system broadcasts about network status.
 */
public class SailthruNetworkReceiver extends BroadcastReceiver {

    STLog log;

    public SailthruNetworkReceiver() {
        log = STLog.getInstance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        AuthenticatedClient client = AuthenticatedClient.getInstance(context);
        client.setConnectedToNetwork(networkInfo != null &&
                networkInfo.isConnectedOrConnecting());

        if (client.isConnectedToNetwork()) {
            NetworkQueue networkQueue = new NetworkQueue();
            networkQueue.registerCachedAttemptIfAvailable(context.getApplicationContext(),
                    client);
        }

        broadcastNetworkStatus(context, client.isConnectedToNetwork());
    }

    /**
     * Broadcast network status to listeners
     *
     * @param context {@link android.content.Context}
     * @param value boolean
     */
    private void broadcastNetworkStatus(Context context, boolean value) {
        log.d(Logger.LogLevel.FULL, "Network receiver value", value + "");
        Intent i = new Intent(Constants.ST_BROADCAST_NETWORK_STATUS);
        i.putExtra(Constants.INTENT_EXTRA_NETWORK_STATUS, value);
        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
    }
}