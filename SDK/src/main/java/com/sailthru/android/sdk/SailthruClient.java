package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient extends SailthruClient_Abstract {

    /**
     * Initializes Sailthru Client
     *
     * @param context
     */
    public SailthruClient(Context context) {
        mContext = context;
        mAuthenticatedClient = ST_AuthenticatedClient.getInstance(context);

        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            mAuthenticatedClient.setConnectedToNetwork(false);
        } else {
            mAuthenticatedClient.setConnectedToNetwork(true);
        }
    }

    /**
     * Public method to register a client to Sailthru
     *
     * @param mode
     * @param domain
     * @param apiKey
     * @param appId
     * @param identification
     * @param uid
     * @param token
     */
    public void register(RegistrationMode mode, String domain,
                         String apiKey, String appId, Identification identification,
                         String uid, String token) {
        if (passedSanityChecks(mode, domain, apiKey, appId, identification, uid, token)) {
            if (mAuthenticatedClient.isConnectedToNetwork()) {
                Log.d("************", "Registering");
                makeRegistrationRequest(appId, apiKey, uid, identification);
            } else {
                Log.d("************", "No network");
                mAuthenticatedClient.setCachedRegisterAttempt();
            }

            saveCredentials(mode.toString(), domain, apiKey, appId, identification.toString(), uid, token);
        }
    }

    /**
     * Public method to unregister current client from Sailthru
     */
    public void unregister() {
        mAuthenticatedClient.deleteHid();
    }
}