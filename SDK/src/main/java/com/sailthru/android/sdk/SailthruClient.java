package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class SailthruClient {

    Context mContext;
    Async_RegisterTask mHorIdLoader = null;

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
    }

    public void register(RegistrationMode mode, String domain,
                         String apiKey, String appId, Identification identification,
                         String uid, String token) {

        if (mHorIdLoader != null) {
            mHorIdLoader.cancel(true);
        }

        Async_RegisterTask loader = new Async_RegisterTask(mContext, appId, apiKey, uid, identification);
        loader.execute((Void) null);
    }

    public void unregisterUser() {
        Utils_SecurePreferences prefs = new Utils_SecurePreferences(mContext,
                St_Constants.ST_SECURE_PREFS, St_Constants.ST_SECURE_PREFS_KEY, true);
        if (prefs.containsKey(St_Constants.ST_PREFS_CACHED_HID)) {
            Log.d("((((((HID))))))", prefs.getString(St_Constants.ST_PREFS_CACHED_HID));
            prefs.removeValue(St_Constants.ST_PREFS_CACHED_HID);
            Log.d("((((((HID-AFTER))))))", "HID CLEARED");
        }
    }

}
