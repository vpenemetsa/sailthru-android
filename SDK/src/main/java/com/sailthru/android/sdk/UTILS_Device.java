package com.sailthru.android.sdk;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 */
class Utils_Device {

    static Context mContext;

    public static Utils_Device getInstance() {
        return new Utils_Device();
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public String getOsVersion() {
        return "7.1";
//        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public String getDeviceType() {
        return "iphone";
//        return Build.MANUFACTURER;
    }

    public String getDeviceVersion() {
        return "4.3";
//        return Build.MODEL;
    }
}