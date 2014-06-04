package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiConstants;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Util methods to construct data for App Register request
 */
@Singleton
public class AppRegisterUtils {

    @Inject
    static DeviceUtils deviceUtils;

    @Inject
    public AppRegisterUtils() {
    }

    private static final String TAG = AppRegisterUtils.class.getSimpleName();

    //Builds request for App Register
    public static Map<String, String> buildRequest(Context context, String appId, String apiKey, String id,
                                            Sailthru.Identification userType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiConstants.UR_API_KEY, apiKey);
        params.put(ApiConstants.UR_FORMAT_KEY, ApiConstants.UR_FORMAT_VALUE);
        params.put(ApiConstants.UR_JSON_KEY, generateRegisterJson(context, id, userType));
        params.put(ApiConstants.UR_SIG_KEY, generateSig(appId, params));

//        ST_Logger.sendLog(params.get(API_Constants.UR_SIG_KEY));
//        Log.d("**********SIG************", params.get(API_Constants.UR_SIG_KEY));

        return params;
    }

    private static String generateRegisterJson(Context context, String uid, Sailthru.Identification userType) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_KEY, ApiConstants.UR_JSON_PLATFORM_APP_VERSION_VALUE);

        if (userType == null) {
            Log.d(TAG, "Existing anonymous user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, "hid");
        } else if (userType.equals(Sailthru.Identification.EMAIL)) {
            Log.d(TAG, "Email user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, userType.toString());
        } else {
            Log.d(TAG, "Anonymous user");
        }

        data.put(ApiConstants.UR_JSON_DEVICE_ID_KEY, deviceUtils.getDeviceId());
        data.put(ApiConstants.UR_JSON_OS_VERSION_KEY, deviceUtils.getOsVersion());
        data.put(ApiConstants.UR_JSON_ENV_KEY, ApiConstants.UR_JSON_ENV_VALUE);
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_ID_KEY, ApiConstants.UR_JSON_PLATFORM_APP_ID_VALUE);
        data.put(ApiConstants.UR_JSON_DEVICE_TYPE_KEY, deviceUtils.getDeviceType());
        data.put(ApiConstants.UR_JSON_DEVICE_VERSION_KEY, deviceUtils.getDeviceVersion());

        Log.d("***********************", data.get(ApiConstants.UR_JSON_DEVICE_ID_KEY));

        Gson gson = new Gson();
        return gson.toJson(data);
    }

    private static String generateSig(String appId, Map<String, String> params) {
        List<String> values = new ArrayList<String>();

        StringBuilder builder = new StringBuilder();
        builder.append(appId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            values.add(entry.getValue());
        }
        Collections.sort(values);

        for (String value : values) {
            builder.append(value);
        }

        return getMd5(builder.toString());
    }

    private static String getMd5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    public static boolean notNullOrEmpty(String input) {
        if (input != null ) {
            if (!TextUtils.isEmpty(input)) {
                return true;
            }
        }

        return false;
    }

    public static boolean notNullAndHasValue(String input, String match) {
        if (input != null) {
            if (input.equals(match)) {
                return true;
            }
        }

        return false;
    }
}