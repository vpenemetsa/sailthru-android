package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;
import com.sailthru.android.sdk.impl.external.gson.src.main.java.com.st.gson.Gson;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.recommend.RecommendService;
import com.sailthru.android.sdk.impl.utils.SailthruUtils;

import java.util.List;
/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Base access class for SDK
 */
public class Sailthru {

    private static final String TAG = Sailthru.class.getSimpleName();

    EventTaskQueue eventTaskQueue;

    AuthenticatedClient authenticatedClient;

    Context context;
    private SailthruUtils sailthruUtils;
    STLog log;

    /**
     * Defines User type for registration
     */
    public enum Identification {
        EMAIL("email"), ANONYMOUS("anonymous");

        private final String name;

        private Identification(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * Defines registration environment
     */
    public enum RegistrationEnvironment {
        DEV("dev"), PROD("prod");

        private final String name;

        private RegistrationEnvironment(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * Initializes Sailthru Client
     *
     * @param context {@link android.content.Context}
     */
    public Sailthru(Context context) {
        this.context = context;
        log = STLog.getInstance();
        authenticatedClient = AuthenticatedClient.getInstance(context);
        eventTaskQueue = EventTaskQueue.create(context, new Gson());
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            authenticatedClient.setConnectedToNetwork(false);
        } else {
            authenticatedClient.setConnectedToNetwork(true);
        }

        sailthruUtils = new SailthruUtils(context, authenticatedClient);
    }

    /**
     * Public method to register a client to Sailthru
     *
     * @param env {@link com.sailthru.android.sdk.Sailthru.RegistrationEnvironment}
     * @param domain String
     * @param apiKey String
     * @param appId String
     * @param identification {@link com.sailthru.android.sdk.Sailthru.Identification}
     * @param platformAppId String
     * @param uid String
     */
    public void register(RegistrationEnvironment env, String domain,
                         String apiKey, String appId, Identification identification,
                         String uid, String platformAppId) {
        if (sailthruUtils.passedSanityChecks(env, domain, apiKey, appId, identification, uid,
                platformAppId)) {
            if (authenticatedClient.isConnectedToNetwork()) {
                log.d(Logger.LogLevel.BASIC, TAG, "Registering");
                sailthruUtils.makeRegistrationRequest(env.toString(), appId, apiKey, uid,
                        identification, platformAppId);
            } else {
                log.d(Logger.LogLevel.BASIC, TAG, "No network");
                authenticatedClient.setCachedRegisterAttempt();
            }

            sailthruUtils.saveCredentials(env.toString(), domain, apiKey, appId,
                    identification.toString(), uid, platformAppId);
        }
    }

    /**
     * Returns boolean to show if the client is currently registered.
     *
     * @return boolean
     */
    public boolean isRegistered() {
        if (authenticatedClient.getHid() != null && !authenticatedClient.getHid().isEmpty()) {
            log.d(Logger.LogLevel.BASIC, "Is registered?", "true");
            return true;
        }

        log.d(Logger.LogLevel.BASIC, "Is registered?", "false");
        return false;
    }

    /**
     * Public method to delete current Horizon id from device
     */
    public void unregister() {
        authenticatedClient.deleteHid();
    }

    /**
     * Used to send parameters to AppTrack
     *
     * @param tags List<String>
     * @param url String
     * @param latitude String
     * @param longitude String
     */
    public void sendAppTrackData(List<String> tags, String url, String latitude, String longitude) {
        if (sailthruUtils.checkAppTrackData(tags, url)) {
            sailthruUtils.addEventToQueue(tags, url, latitude, longitude, eventTaskQueue);
        } else {
            log.d(Logger.LogLevel.BASIC, "Apptrack", "Task not added. No tags or url provided");
        }
    }

    /**
     * Used to set an external logger to intercept all log messages
     *
     * @param logger {@link com.sailthru.android.sdk.impl.logger.Logger}
     */
    public void setLogger(Logger logger) {
        log.setExternalLogger(logger);
    }

    /**
     * Returns instance of {@link com.sailthru.android.sdk.impl.logger.Logger}
     * if set or returns null.
     *
     * @return {@link com.sailthru.android.sdk.impl.logger.Logger}
     */
    public Logger getLogger() {
        return log.getExternalLogger();
    }

    /**
     * Returns a JSON String with recommendations which can be filtered by input tags
     * Leave tags as null to receive all recommendations
     *
     * @param count int
     * @param tags List<String>
     * @return String
     */
    public String getRecommendations(int count, List<String> tags) {
        String recommendations = "";
        if (sailthruUtils.canGetRecommendations()) {
            recommendations = RecommendService.getRecommendations(context,
                    authenticatedClient.getDomain(), authenticatedClient.getHid(), count, tags);
        }

        return recommendations;
    }
}