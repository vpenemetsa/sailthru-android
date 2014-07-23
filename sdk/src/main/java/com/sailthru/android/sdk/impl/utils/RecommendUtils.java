package com.sailthru.android.sdk.impl.utils;

import com.sailthru.android.sdk.impl.api.ApiConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 *
 * Utility methods for getting recommendations
 */
public class RecommendUtils {

    /**
     * Builds request to be sent out to the Recommend endpoint.
     *
     * @param domain String
     * @param hid String
     * @param count Integer
     * @param tags List<String>
     * @param appTrackTags List<String>
     * @param url String
     * @return Map<String,String>
     */
    public static Map<String, String> buildRequest(String domain, boolean useStoredTags, String hid,
                                                   Integer count, List<String> tags,
                                                   List<String> appTrackTags, String url) {
        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(ApiConstants.REC_DOMAIN_KEY, domain);
        parameters.put(ApiConstants.REC_HID_KEY, hid);

        if (useStoredTags) {
            parameters.put(ApiConstants.REC_USE_STORED_TAGS, "1");
        }

        if (count != null) {
            parameters.put(ApiConstants.REC_COUNT_KEY, Integer.toString(count));
        }

        if (tags != null && tags.size() > 0) {
            String tagsString = SailthruUtils.getCommaDelimitedString(tags);
            parameters.put(ApiConstants.REC_FILTER_TAGS_KEY, tagsString);
        }

        if (appTrackTags != null && appTrackTags.size() > 0) {
            String appTrackTagsString = SailthruUtils.getCommaDelimitedString(appTrackTags);
            parameters.put(ApiConstants.REC_TAGS_KEY, appTrackTagsString);
        }

        if (url != null) {
            parameters.put(ApiConstants.REC_URL_KEY, url);
        }

        return parameters;
    }
}