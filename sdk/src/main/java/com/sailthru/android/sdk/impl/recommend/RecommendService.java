package com.sailthru.android.sdk.impl.recommend;

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
     * @param count int
     * @param tags List<String>
     * @return String
     */
    public static String getRecommendations(String domain, String hid, int count,
                                            List<String> tags) {
        ApiManager apiManager = new ApiManager();
        return apiManager.getRecommendations(domain, hid, count, tags);
    }
}
