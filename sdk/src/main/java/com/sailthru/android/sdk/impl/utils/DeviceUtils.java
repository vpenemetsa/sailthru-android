package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.provider.Settings;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vijay Penemetsa on 5/19/14.
 *
 * Helper methods to get device information
 */
@Singleton
public class DeviceUtils {

    private static Context context;

    @Inject
    public DeviceUtils(Context context) {
        this.context = context;
    }

    public static String getDeviceId() {
        return "1234567890";
    }

    public static String getOsVersion() {
        return "7.1";
//        return String.valueOf(Build.VERSION.SDK_INT);
    }

    public static String getDeviceType() {
        return "iphone";
//        return Build.MANUFACTURER;
    }

    public static String getDeviceVersion() {
        return "4.3";
//        return Build.MODEL;
    }
}