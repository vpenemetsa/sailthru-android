package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient extends SailthruClient_Abstract {

    /**
     *
     * @param context
     */
    public SailthruClient(Context context) {
        mContext = context;
        mAuthenticatedClient = St_AuthenticatedClient.getInstance(context);

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

        if (mHorIdLoader != null) {
            mHorIdLoader.cancel(true);
        }

        if (passedSanityChecks()) {
            if (mAuthenticatedClient.isConnectedToNetwork()) {
                Async_RegisterTask loader;
                String storedHid = mAuthenticatedClient.getHid();
                if (notNullAndHasValue(mAuthenticatedClient.getIdentification(), Identification.ANONYMOUS.toString()) &&
                        identification.equals(Identification.EMAIL) && notNullOrEmpty(storedHid)) {
                    Log.d("stored Hid", storedHid);
                    loader = new Async_RegisterTask(mContext, appId, apiKey, storedHid,
                            null, mAuthenticatedClient);
                } else {
                    loader = new Async_RegisterTask(mContext, appId, apiKey, uid,
                            identification, mAuthenticatedClient);
                }

                loader.execute((Void) null);
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