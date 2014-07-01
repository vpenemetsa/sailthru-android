package com.sailthru.android.sdk.impl.request;

import com.google.gson.annotations.SerializedName;
import com.sailthru.android.sdk.impl.Constants;
import com.sailthru.android.sdk.impl.api.ApiConstants;

/**
 * Created by vijaypenemetsa on 7/1/14.
 */
public class UserRegisterJson {

    @SerializedName(ApiConstants.UR_JSON_DEVICE_VERSION_KEY)
    private String deviceVersion;

    @SerializedName(ApiConstants.UR_JSON_PLATFORM_APP_VERSION_KEY)
    private String platformAppVersion;

    @SerializedName(ApiConstants.UR_JSON_PLATFORM_APP_ID_KEY)
    private String platformAppId;

    private String env;

    private String id;

    @SerializedName(ApiConstants.UR_JSON_OS_VERSION_KEY)
    private String osVersion;

    @SerializedName(ApiConstants.UR_JSON_DEVICE_TYPE_KEY)
    private String deviceType;

    @SerializedName(ApiConstants.UR_JSON_DEVICE_ID_KEY)
    private String deviceId;

    private String key;



}