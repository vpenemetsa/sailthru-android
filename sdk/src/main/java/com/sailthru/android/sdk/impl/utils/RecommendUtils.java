package com.sailthru.android.sdk.impl.utils;

import com.sailthru.android.sdk.impl.api.ApiConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 */
public class RecommendUtils {

    public Map<String, String> buildRequest(String domain, String hid, int count, List<String> tags) {
        Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(ApiConstants.REC_DOMAIN_KEY, domain);
        parameters.put(ApiConstants.REC_HID_KEY, hid);
        parameters.put(ApiConstants.REC_COUNT_KEY, Integer.toString(count));
        if (tags != null && tags.size() > 0) {
            String tagsString = "";
            for (String tag : tags) {
                tagsString += tag + ",";
            }

            tagsString = tagsString.replaceAll(" ", "");
            tagsString = tagsString.substring(0, tagsString.length() - 1);
            parameters.put(ApiConstants.REC_FILTER_TAGS_KEY, tagsString);
        }

        return parameters;
    }
}