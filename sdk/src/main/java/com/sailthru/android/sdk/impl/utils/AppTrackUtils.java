package com.sailthru.android.sdk.impl.utils;

import android.util.Log;

import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.event.Event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Vijay Penemetsa on 6/6/14.
 */
@Singleton
public class AppTrackUtils {

    @Inject
    public AppTrackUtils() {
    }

    public static Map<String, String> buildRequest(Event event) {

        Map<String, String> parameters = new HashMap<String, String>();

        String url = event.getUrl();
        if (url != null) {
            parameters.put(ApiConstants.APPTRACK_URL_KEY, url);
        }

        List<String> tags = event.getTags();
        if (tags != null && tags.size() > 0) {
            String tagsString = "";
            for (String tag : tags) {
                tagsString += tag + ",";
            }

            tagsString = tagsString.substring(0, tagsString.length() - 1);
            tagsString = tagsString.replaceAll(" ", "");
            Log.d("*^&%#$%^&*)(^%#$^&*()&^%#$%&^*(&^%#$%&^*()&^%#$&^*(^%$#%&^*%#$", tagsString);
            parameters.put(ApiConstants.APPTRACK_TAGS_KEY, tagsString);
        }

        String timestamp = Long.toString(event.getTimestamp());
        if (timestamp != null) {
            parameters.put(ApiConstants.APPTRACK_DATE_KEY, timestamp);
        }

        String latitude = event.getLatitude();
        String longitude = event.getLongitude();
        if (latitude != null && longitude != null) {
            parameters.put(ApiConstants.APPTRACK_LATITUDE_KEY, latitude);
            parameters.put(ApiConstants.APPTRACK_LONGITUDE_KEY, longitude);
        }

        String hid = event.getHid();
        if (hid != null) {
            parameters.put(ApiConstants.APPTRACK_HID_KEY, hid);
        }

        String appId = event.getAppId();
        if (appId != null) {
            parameters.put(ApiConstants.APPTRACK_APP_ID_KEY, appId);
        }

        String domain = event.getDomain();
        if (domain != null) {
            parameters.put(ApiConstants.APPTRACK_DOMAIN_KEY, domain);
        }

        return parameters;
    }
}