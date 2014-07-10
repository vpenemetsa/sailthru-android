package com.sailthru.android.sdk.impl.request;

import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.external.gson.annotations.SerializedName;

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

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getPlatformAppVersion() {
        return platformAppVersion;
    }

    public void setPlatformAppVersion(String platformAppVersion) {
        this.platformAppVersion = platformAppVersion;
    }

    public String getPlatformAppId() {
        return platformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        this.platformAppId = platformAppId;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}