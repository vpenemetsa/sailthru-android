package com.sailthru.android.sdk.impl.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
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
            registerCachedAttemptIfAvailable(context.getApplicationContext(), client);
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

    /**
     * Register if a previous attempt was cached.
     *
     * @param context {@link android.content.Context}
     * @param client {@link com.sailthru.android.sdk.impl.client.AuthenticatedClient}
     */
    private void registerCachedAttemptIfAvailable(Context context,
                                                 AuthenticatedClient client) {
        if (client.isCachedRegisterAttempt()) {
            Sailthru stClient = new Sailthru(context);
            Sailthru.RegistrationEnvironment mode;
            if (client.getMode().equals(Sailthru.RegistrationEnvironment.DEV.toString())) {
                mode = Sailthru.RegistrationEnvironment.DEV;
            } else {
                mode = Sailthru.RegistrationEnvironment.PROD;
            }

            Sailthru.Identification identification;
            if (client.getIdentification().
                    equals(Sailthru.Identification.ANONYMOUS.toString())) {
                identification = Sailthru.Identification.ANONYMOUS;
            } else {
                identification = Sailthru.Identification.EMAIL;
            }

            stClient.register(mode, client.getDomain(), client.getApiKey(),
                    client.getAppId(), identification, client.getUid(), client.getPlatformAppId(),
                    client.getGcmRegId());

            client.deleteCachedRegisterAttempt();
        }
    }
}