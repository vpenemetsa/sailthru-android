package com.sailthru.android.sdk.impl.api;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class ApiConstants {

    //API ENDPOINTS
    public static final String ST_API_ENDPOINT = "https://api.sailthru.com/mobile";
    public static final String HORIZON_API_ENDPOINT = "https://horizon.sailthru.com/horizon";

    public static final String API_USER_REGISTER_PATH = "/userregisterapp";
    public static final String API_APPTRACK_PATH = "/apptrack";

    //User Registration request keys
    public static final String UR_API_KEY = "api_key";
    public static final String UR_FORMAT_KEY = "format";
    public static final String UR_FORMAT_VALUE = "json";
    public static final String UR_JSON_KEY = "json";
    public static final String UR_SIG_KEY = "sig";

    public static final String UR_JSON_PLATFORM_APP_VERSION_KEY = "platform_app_version";
    public static final String UR_JSON_PLATFORM_APP_VERSION_VALUE = "1.0";
    public static final String UR_JSON_ID_KEY = "id";
    public static final String UR_JSON_DEVICE_ID_KEY = "device_id";
    public static final String UR_JSON_OS_VERSION_KEY = "os_version";
    public static final String UR_JSON_ENV_KEY = "env";
    public static final String UR_JSON_ENV_VALUE = "dev";
    public static final String UR_JSON_PLATFORM_APP_ID_KEY = "platform_app_id";
    public static final String UR_JSON_PLATFORM_APP_ID_VALUE = "com.sailthru.qa";
    public static final String UR_JSON_KEY_KEY = "key";
    public static final String UR_JSON_DEVICE_TYPE_KEY = "device_type";
    public static final String UR_JSON_DEVICE_VERSION_KEY = "device_version";

    //AppTrack keys
    public static final String APPTRACK_APP_ID_KEY = "app_id";
    public static final String APPTRACK_HID_KEY = "hid";
    public static final String APPTRACK_URL_KEY = "url";
    public static final String APPTRACK_TAGS_KEY  = "tags";
    public static final String APPTRACK_DATE_KEY = "date";
    public static final String APPTRACK_MESSAGE_ID_KEY = "mid";
    public static final String APPTRACK_LATITUDE_KEY = "lat";
    public static final String APPTRACK_LONGITUDE_KEY = "lng";
    public static final String APPTRACK_DOMAIN_KEY = "d";

    public static final String ST_CACHED_UR_ATTEMPTED = "ST_CACHED_UR_ATTEMPTED";



//    public static final String TEMP_JSON = "{ \"platform_app_version\" : \"1.0\", \"id\" : \"dhoerl+testa009@sailthru.com\", \"device_id\" : \"acabd25475bfbdb927ca989ef5cba4d0eefb9655d33d127dc8e01432dc01e8ca\", \"os_version\" : \"7.1\", \"env\" : \"dev\", \"platform_app_id\" : \"com.sailthru.qa\", \"key\" : \"email\", \"device_type\" : \"iphone\", \"device_version\" : \"4.3\" }";

}