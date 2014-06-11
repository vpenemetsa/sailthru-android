package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.logger.STLog;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Util methods to construct data for App Register request
 */
public class AppRegisterUtils {

    private static final String TAG = AppRegisterUtils.class.getSimpleName();

    STLog log;

    //Builds request for App Register
    public Map<String, String> buildRequest(Context context, String appId, String apiKey, String id,
                                            Sailthru.Identification userType) {
        log = STLog.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiConstants.UR_API_KEY, apiKey);
        params.put(ApiConstants.UR_FORMAT_KEY, ApiConstants.UR_FORMAT_VALUE);
        params.put(ApiConstants.UR_JSON_KEY, generateRegisterJson(context, id, userType));
        params.put(ApiConstants.UR_SIG_KEY, generateSig(appId, params));

//        ST_Logger.sendLog(params.get(API_Constants.UR_SIG_KEY));
//        Log.d("**********SIG************", params.get(API_Constants.UR_SIG_KEY));

        return params;
    }

    private String generateRegisterJson(Context context, String uid, Sailthru.Identification userType) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_KEY, ApiConstants.UR_JSON_PLATFORM_APP_VERSION_VALUE);

        if (userType == null) {
            log.d(TAG, "Existing anonymous user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, "hid");
        } else if (userType.equals(Sailthru.Identification.EMAIL)) {
            log.d(TAG, "Email user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, userType.toString());
        } else {
            log.d(TAG, "Anonymous user");
        }

        DeviceUtils deviceUtils = new DeviceUtils(context);
        data.put(ApiConstants.UR_JSON_DEVICE_ID_KEY, deviceUtils.getDeviceId());
        data.put(ApiConstants.UR_JSON_OS_VERSION_KEY, deviceUtils.getOsVersion());
        data.put(ApiConstants.UR_JSON_ENV_KEY, ApiConstants.UR_JSON_ENV_VALUE);
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_ID_KEY, ApiConstants.UR_JSON_PLATFORM_APP_ID_VALUE);
        data.put(ApiConstants.UR_JSON_DEVICE_TYPE_KEY, deviceUtils.getDeviceType());
        data.put(ApiConstants.UR_JSON_DEVICE_VERSION_KEY, deviceUtils.getDeviceVersion());

        log.d("***********************", data.get(ApiConstants.UR_JSON_DEVICE_ID_KEY));

        Gson gson = new Gson();
        return gson.toJson(data);
    }

    private String generateSig(String appId, Map<String, String> params) {
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

    private String getMd5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(data.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    public boolean notNullOrEmpty(String input) {
        if (input != null ) {
            if (!TextUtils.isEmpty(input)) {
                return true;
            }
        }

        return false;
    }

    public boolean notNullAndHasValue(String input, String match) {
        if (input != null) {
            if (input.equals(match)) {
                return true;
            }
        }

        return false;
    }
}