package com.sailthru.android.sdk.impl.utils;

import com.sailthru.android.sdk.impl.api.ApiConstants;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.logger.STLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay Penemetsa on 6/6/14.
 *
 * Helper methods for AppTrack request
 */
public class AppTrackUtils {

    /**
     * Builds request parameters for an AppTrack request
     *
     * @param event {@link com.sailthru.android.sdk.impl.event.Event}
     * @return Map<String,String>
     */
    public Map<String, String> buildRequest(Event event) {

        STLog log = STLog.getInstance();

        Map<String, String> parameters = new HashMap<String, String>();

        String urlString = getCommaDelimitedString(event.getUrls());
        if (urlString != null && !urlString.isEmpty()) {
            parameters.put(ApiConstants.APPTRACK_URL_KEY, urlString);
        }

        String tagsString = getCommaDelimitedString(event.getTags());
        if (tagsString != null && !tagsString.isEmpty()) {
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

    private String getCommaDelimitedString(List<String> input) {
        String output = "";

        if (input != null && input.size() > 0) {
            for (String tag : input) {
                output += tag + ",";
            }

            output = output.substring(0, output.length() - 1);
            output = output.replaceAll(" ", "");
        }

        return output;
    }
}