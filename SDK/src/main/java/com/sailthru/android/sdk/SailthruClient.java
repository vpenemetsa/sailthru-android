package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient {

    Context mContext;
    Async_RegisterTask mHorIdLoader = null;
    St_AuthenticatedClient mAuthenticatedClient;

    public enum Identification {
        EMAIL("email"), ANONYMOUS("anonymous");

        private final String name;

        private Identification(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    public enum RegistrationMode {
        DEV("DEV"), PROD("PROD");

        private final String name;

        private RegistrationMode(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

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

    public void register(RegistrationMode mode, String domain,
                         String apiKey, String appId, Identification identification,
                         String uid, String token) {

        if (mHorIdLoader != null) {
            mHorIdLoader.cancel(true);
        }

        if (mAuthenticatedClient.isConnectedToNetwork()) {
            Async_RegisterTask loader = new Async_RegisterTask(mContext, appId, apiKey, uid,
                    identification, mAuthenticatedClient);
            loader.execute((Void) null);
        }
    }

    public void unregisterUser() {
        mAuthenticatedClient.deleteHid();
    }
}
