package com.sailthru.android.sdk.impl.utils;

import android.content.Context;
import android.text.TextUtils;

import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.external.gson.Gson;
import com.sailthru.android.sdk.impl.logger.Logger;
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
public class UserRegisterUtils {

    private static final String TAG = UserRegisterUtils.class.getSimpleName();

    private static STLog log;

    /**
     * Builds request parameters for AppRegister
     *
     * @param context {@link android.content.Context}
     * @param appId Stirng
     * @param apiKey String
     * @param id String
     * @param userType {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @return Map<String,String>
     */
    public static Map<String, String> buildRequest(Context context, String env, String appId,
                                            String apiKey, String id,
                                            Sailthru.Identification userType, String platformAppId) {
        log = STLog.getInstance();
        Map<String, String> params = new HashMap<String, String>();
        params.put(ApiConstants.UR_API_KEY, apiKey);
        params.put(ApiConstants.UR_FORMAT_KEY, ApiConstants.UR_FORMAT_VALUE);
        params.put(ApiConstants.UR_JSON_KEY, generateRegisterJson(context, env, id, userType,
                platformAppId));
        params.put(ApiConstants.UR_SIG_KEY, generateSig(appId, params));

        return params;
    }

    /**
     * Generates json string parameter needed for AppRegister
     *
     * @param context {@link android.content.Context}
     * @param uid String
     * @param userType {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @return String
     */
    private static String generateRegisterJson(Context context, String env, String uid,
                                        Sailthru.Identification userType, String platformAppId) {
        Map<String, String> data = new HashMap<String, String>();
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_KEY, ApiConstants.UR_JSON_PLATFORM_APP_VERSION_VALUE);

        if (userType == null) {
            log.d(Logger.LogLevel.FULL, TAG, "Existing anonymous user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, "hid");
        } else if (userType.equals(Sailthru.Identification.EMAIL)) {
            log.d(Logger.LogLevel.FULL, TAG, "Email user");
            data.put(ApiConstants.UR_JSON_ID_KEY, uid);
            data.put(ApiConstants.UR_JSON_KEY_KEY, userType.toString());
        } else {
            log.d(Logger.LogLevel.FULL, TAG, "Anonymous user");
        }

        DeviceUtils deviceUtils = new DeviceUtils(context);
        data.put(ApiConstants.UR_JSON_DEVICE_ID_KEY, deviceUtils.getDeviceId());
        data.put(ApiConstants.UR_JSON_OS_VERSION_KEY, deviceUtils.getOsVersion());
        data.put(ApiConstants.UR_JSON_ENV_KEY, env);
        data.put(ApiConstants.UR_JSON_PLATFORM_APP_ID_KEY, platformAppId);
        data.put(ApiConstants.UR_JSON_DEVICE_TYPE_KEY, deviceUtils.getDeviceType());
        data.put(ApiConstants.UR_JSON_DEVICE_VERSION_KEY, deviceUtils.getDeviceVersion());

        Gson gson = new Gson();
        String registerJson = gson.toJson(data);
        log.d(Logger.LogLevel.FULL, "Register JSON - ", registerJson);
        return registerJson;
    }

    /**
     * Generates hash signature parameter needed for AppRegister
     *
     * @param appId String
     * @param params Map<String,String>
     * @return String
     */
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

        log.d(Logger.LogLevel.FULL, "Registration sig hash - ", builder.toString());
        return getMd5(builder.toString());
    }

    /**
     * @param data String
     * @return String
     */
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
            log.e(Logger.LogLevel.FULL, "MD5 hash error", e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Helper method to check if input String is not null or empty
     *
     * @param input String
     * @return boolean
     */
    public static boolean notNullOrEmpty(String input) {
        if (input != null ) {
            if (!TextUtils.isEmpty(input)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Helper method to check if input string is not null and has given value
     *
     * @param input String
     * @param match String
     * @return boolean
     */
    public static boolean notNullAndHasValue(String input, String match) {
        if (input != null) {
            if (input.equals(match)) {
                return true;
            }
        }

        return false;
    }
}