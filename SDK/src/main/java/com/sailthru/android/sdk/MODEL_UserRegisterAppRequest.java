package com.sailthru.android.sdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Request Class used to send data across to the API during registration
 */
class MODEL_UserRegisterAppRequest {

    @SerializedName("platform_app_version")
    private String platformAppVersion;

    private String id;

    @SerializedName("device_id")
    private String deviceId;

    @SerializedName("os_version")
    private String osVersion;

    private String env;

    @SerializedName("platform_app_id")
    private String platformAppId;

    private String key;

    @SerializedName("device_type")
    private String deviceType;

    @SerializedName("device_version")
    private String deviceVersion;

    public String getPlatformAppVersion() {
        return platformAppVersion;
    }

    public void setPlatformAppVersion(String platformAppVersion) {
        this.platformAppVersion = platformAppVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getPlatformAppId() {
        return platformAppId;
    }

    public void setPlatformAppId(String platformAppId) {
        this.platformAppId = platformAppId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }
}
