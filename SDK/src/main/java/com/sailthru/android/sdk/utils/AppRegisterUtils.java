package com.sailthru.android.sdk.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import com.sailthru.android.sdk.Constants;
import com.sailthru.android.sdk.api.ApiConstants;
import com.sailthru.android.sdk.api.model.UserRegisterAppRequest;

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

    public static Map<String, String> buildRequest(Context context, String appId, String apiKey, String uid,
                                            ApiConstants.Identification userType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiConstants.UR_API_KEY, apiKey);
        params.put(ApiConstants.UR_FORMAT_KEY, ApiConstants.UR_FORMAT_VALUE);
        params.put(ApiConstants.UR_JSON_KEY, generateRegisterJson(context, uid, userType));
        params.put(ApiConstants.UR_SIG_KEY, generateSig(appId, params));

        return params;
    }

    private static String generateRegisterJson(Context context, String uid, ApiConstants.Identification userType) {
        DeviceUtils deviceUtils = DeviceUtils.getInstance();
        deviceUtils.setContext(context);

        UserRegisterAppRequest request = new UserRegisterAppRequest();
        request.setPlatformAppVersion(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_VALUE);
        request.setId(uid);
        request.setDeviceId(deviceUtils.getDeviceId());
        request.setOsVersion(deviceUtils.getOsVersion());
        request.setEnv(ApiConstants.UR_JSON_ENV_VALUE);
        request.setPlatformAppId(ApiConstants.UR_JSON_PLATFORM_APP_ID_VALUE);
        request.setKey(userType.toString());
        request.setDeviceType(deviceUtils.getDeviceType());
        request.setDeviceVersion(deviceUtils.getDeviceVersion());

        Map<String, String> data = new HashMap<String, String>();
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_KEY, ApiConstants.UR_JSON_PLATFORM_APP_VERSION_VALUE);
        data.put(ApiConstants.UR_JSON_ID_KEY, uid);
        data.put(ApiConstants.UR_JSON_DEVICE_ID_KEY, deviceUtils.getDeviceId());
        data.put(ApiConstants.UR_JSON_OS_VERSION_KEY, deviceUtils.getOsVersion());
        data.put(ApiConstants.UR_JSON_ENV_KEY, ApiConstants.UR_JSON_ENV_VALUE);
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_ID_KEY, ApiConstants.UR_JSON_PLATFORM_APP_ID_VALUE);
        data.put(ApiConstants.UR_JSON_KEY_KEY, userType.toString());
        data.put(ApiConstants.UR_JSON_DEVICE_TYPE_KEY, deviceUtils.getDeviceType());
        data.put(ApiConstants.UR_JSON_DEVICE_VERSION_KEY, deviceUtils.getDeviceVersion());

        Gson gson = new Gson();
        Log.d("**********************", gson.toJson(request));
        return gson.toJson(request);
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
}
