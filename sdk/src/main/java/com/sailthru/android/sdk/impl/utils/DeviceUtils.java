package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Helper methods to get device information
 */
public class DeviceUtils {

    private Context context;

    public DeviceUtils(Context context) {
        this.context = context;
    }

    /**
     * Returns device ID
     *
     * @return String
     */
    public String getDeviceId() {
//        return "1234567890";
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * Returns OS Version
     *
     * @return String
     */
    public String getOsVersion() {
        return "7.1";
//        return String.valueOf(Build.VERSION.SDK_INT);
    }

    /**
     * Returns Device manufacturer
     *
     * @return String
     */
    public String getDeviceType() {
        return "iphone";
//        return Build.MANUFACTURER;
    }

    /**
     * Returns Device model
     *
     * @return String
     */
    public static String getDeviceVersion() {
        return "4.3";
//        return Build.MODEL;
    }
}