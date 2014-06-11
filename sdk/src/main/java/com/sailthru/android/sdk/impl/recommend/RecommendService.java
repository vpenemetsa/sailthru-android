package com.sailthru.android.sdk.impl.recommend;

import com.sailthru.android.sdk.impl.api.ApiManager;

import java.util.List;

/**
 * Created by Vijay Penemetsa on 6/11/14.
 */
public class RecommendService {

    public static String getRecommendations(String domain, String hid, int count,
                                            List<String> tags) {
        ApiManager apiManager = new ApiManager();
        return apiManager.getRecommendations(domain, hid, count, tags);
    }
}
