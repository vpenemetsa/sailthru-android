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
class Utils_AppRegister {

    public static Map<String, String> buildRequest(Context context, String appId, String apiKey, String uid,
                                            SailthruClient.Identification userType) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(Api_Constants.UR_API_KEY, apiKey);
        params.put(Api_Constants.UR_FORMAT_KEY, Api_Constants.UR_FORMAT_VALUE);
        params.put(Api_Constants.UR_JSON_KEY, generateRegisterJson(context, uid, userType));
        params.put(Api_Constants.UR_SIG_KEY, generateSig(appId, params));

        return params;
    }

    private static String generateRegisterJson(Context context, String uid, SailthruClient.Identification userType) {
        Utils_Device UTILSDevice = Utils_Device.getInstance();
        UTILSDevice.setContext(context);

        Map<String, String> data = new HashMap<String, String>();
        data.put(Api_Constants.UR_JSON_PLATFORM_APP_VERSION_KEY, Api_Constants.UR_JSON_PLATFORM_APP_VERSION_VALUE);
        Log.d("*******************", userType.toString());
        if (userType.equals(SailthruClient_Abstract.Identification.EMAIL)) {
            Log.d("*******************", "ENtered as email");
            data.put(Api_Constants.UR_JSON_ID_KEY, uid);
            data.put(Api_Constants.UR_JSON_KEY_KEY, userType.toString());
        } else if (userType == null) {
            Log.d("*******************", "ENtered as null");
            data.put(Api_Constants.UR_JSON_KEY_KEY, "hid");
        }

        data.put(Api_Constants.UR_JSON_DEVICE_ID_KEY, UTILSDevice.getDeviceId());
        data.put(Api_Constants.UR_JSON_OS_VERSION_KEY, UTILSDevice.getOsVersion());
        data.put(Api_Constants.UR_JSON_ENV_KEY, Api_Constants.UR_JSON_ENV_VALUE);
        data.put(Api_Constants.UR_JSON_PLATFORM_APP_ID_KEY, Api_Constants.UR_JSON_PLATFORM_APP_ID_VALUE);
        data.put(Api_Constants.UR_JSON_DEVICE_TYPE_KEY, UTILSDevice.getDeviceType());
        data.put(Api_Constants.UR_JSON_DEVICE_VERSION_KEY, UTILSDevice.getDeviceVersion());

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
