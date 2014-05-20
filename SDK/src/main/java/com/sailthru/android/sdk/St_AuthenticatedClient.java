package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Vijay Penemetsa on 5/20/14.
 */
class St_AuthenticatedClient {

    private static St_AuthenticatedClient mAuthenticatedClient;

    static Utils_SecurePreferences mPrefs;
    static Context mContext;

    //Shared prefs keys
    private static final String ST_PREFS_CACHED_HID = "ST_PREFS_CACHED_HID";

    //Shared prefs creds
    private static final String ST_SECURE_PREFS = "ST_SECURE_PREFS";
    private static final String ST_SECURE_PREFS_KEY = "de60752302fc542ece2e55ff81ff09dc";

    //Stored variables
    private static String mHid;
    private boolean mConnectedToNetwork;

    public static St_AuthenticatedClient getInstance(Context context) {
        if (mAuthenticatedClient == null) {
            mAuthenticatedClient = new St_AuthenticatedClient(context);
        }

        return mAuthenticatedClient;
    }

    public St_AuthenticatedClient(Context context) {
        mPrefs = new Utils_SecurePreferences(context, ST_SECURE_PREFS,
                ST_SECURE_PREFS_KEY, true);
        mContext = context;
        loadData();
    }

    private static synchronized void loadData() {
        if (mPrefs.containsKey(ST_PREFS_CACHED_HID)) {
            mHid = mPrefs.getString(ST_PREFS_CACHED_HID);
        }
    }

    public String getHid() {
        return mHid;
    }

    public synchronized void saveHid(String hid) {
        mPrefs.put(ST_PREFS_CACHED_HID, hid);
        mHid = hid;
    }

    public synchronized void deleteHid() {
        if (mPrefs.containsKey(ST_PREFS_CACHED_HID)) {
            mPrefs.removeValue(ST_PREFS_CACHED_HID);
        }
        mHid = null;
    }

    public boolean isConnectedToNetwork() {
        return mConnectedToNetwork;
    }

    public void setConnectedToNetwork(boolean connectedToNetwork) {
        mConnectedToNetwork = connectedToNetwork;
    }
}