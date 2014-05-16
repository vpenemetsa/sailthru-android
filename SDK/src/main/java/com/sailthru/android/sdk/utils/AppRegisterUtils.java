package com.sailthru.android.sdk.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.sailthru.android.sdk.Constants;
import com.sailthru.android.sdk.api.ApiConstants;

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
class AppRegisterUtils {

    public String generateRegisterJson() {

        return "";
    }

    public Map<String, String> buildRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("api_key", "419188ee0d0dc748f04e0cd9ea7d7c0f");
        params.put("format", "json");
        params.put("json", ApiConstants.TEMP_JSON);
        params.put("sig", "d48ffaf86db3afaa1d7201ca8ac25bac");

        return params;
    }

    public String generateSig(String apiSecret, Map<String, String> params) {
        List<String> values = new ArrayList<String>();

        StringBuilder builder = new StringBuilder();
        builder.append(apiSecret);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.append(entry.getValue());
        }
        Collections.sort(values);

        for (String value : values) {
            builder.append(value);
        }

        return getMd5(builder.toString());
    }

    private String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private String getOsVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
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
