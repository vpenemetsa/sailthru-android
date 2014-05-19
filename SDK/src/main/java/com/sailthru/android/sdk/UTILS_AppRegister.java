package com.sailthru.android.sdk;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

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
class UTILS_AppRegister {

    public static Map<String, String> buildRequest(Context context, String appId, String apiKey, String uid,
                                            API_Constants.Identification userType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(API_Constants.UR_API_KEY, apiKey);
        params.put(API_Constants.UR_FORMAT_KEY, API_Constants.UR_FORMAT_VALUE);
        params.put(API_Constants.UR_JSON_KEY, generateRegisterJson(context, uid, userType));
        params.put(API_Constants.UR_SIG_KEY, generateSig(appId, params));

        return params;
    }

    private static String generateRegisterJson(Context context, String uid, API_Constants.Identification userType) {
        UTILS_Device UTILSDevice = UTILS_Device.getInstance();
        UTILSDevice.setContext(context);

//        MODEL_UserRegisterAppRequest request = new MODEL_UserRegisterAppRequest();
//        request.setPlatformAppVersion(API_Constants.UR_JSON_PLATFORM_APP_VERSION_VALUE);
//        request.setId(uid);
//        request.setDeviceId(UTILSDevice.getDeviceId());
//        request.setOsVersion(UTILSDevice.getOsVersion());
//        request.setEnv(API_Constants.UR_JSON_ENV_VALUE);
//        request.setPlatformAppId(API_Constants.UR_JSON_PLATFORM_APP_ID_VALUE);
//        request.setKey(userType.toString());
//        request.setDeviceType(UTILSDevice.getDeviceType());
//        request.setDeviceVersion(UTILSDevice.getDeviceVersion());

        Map<String, String> data = new HashMap<String, String>();
        data.put(API_Constants.UR_JSON_PLATFORM_APP_VERSION_KEY, API_Constants.UR_JSON_PLATFORM_APP_VERSION_VALUE);
        data.put(API_Constants.UR_JSON_ID_KEY, uid);
        data.put(API_Constants.UR_JSON_DEVICE_ID_KEY, UTILSDevice.getDeviceId());
        data.put(API_Constants.UR_JSON_OS_VERSION_KEY, UTILSDevice.getOsVersion());
        data.put(API_Constants.UR_JSON_ENV_KEY, API_Constants.UR_JSON_ENV_VALUE);
        data.put(API_Constants.UR_JSON_PLATFORM_APP_ID_KEY, API_Constants.UR_JSON_PLATFORM_APP_ID_VALUE);
        data.put(API_Constants.UR_JSON_KEY_KEY, userType.toString());
        data.put(API_Constants.UR_JSON_DEVICE_TYPE_KEY, UTILSDevice.getDeviceType());
        data.put(API_Constants.UR_JSON_DEVICE_VERSION_KEY, UTILSDevice.getDeviceVersion());

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
}
