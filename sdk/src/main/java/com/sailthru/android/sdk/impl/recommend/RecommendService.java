package com.sailthru.android.sdk.impl.recommend;

import android.content.Context;

import com.sailthru.android.sdk.impl.api.ApiManager;

import java.util.List;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 *
 * Service that accepts input filters and returns recommendations
 */
public class RecommendService {

    /**
     * Get recommendations from Recommend endpoint
     *
     * @param domain String
     * @param hid String
     * @param count Integer
     * @param tags List<String>
     * @return String
     */
    public static String getRecommendations(Context context, boolean useStoredTags, String domain,
                                            String hid, Integer count, List<String> tags,
                                            List<String> appTrackTags, String url) {
        ApiManager apiManager = new ApiManager(context);
        return apiManager.getRecommendations(domain, hid, useStoredTags, count, tags,
                appTrackTags, url);
    }
}
