package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.EventModule;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;
import com.sailthru.android.sdk.impl.logger.Logger;
import com.sailthru.android.sdk.impl.logger.STLog;
import com.sailthru.android.sdk.impl.recommend.RecommendService;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.ObjectGraph;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class Sailthru {

    @Inject
    Lazy<EventTaskQueue> eventTaskQueue;

    AuthenticatedClient authenticatedClient;

    Context context;
    private SailthruClient sailthruClient;
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
    public enum RegistrationMode {
        DEV("dev"), PROD("prod");

        private final String name;

        private RegistrationMode(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * Initializes Sailthru Client
     *
     * @param context
     */
    public Sailthru(Context context) {
        this.context = context;
        ObjectGraph.create(new EventModule(context)).inject(this);
        log = STLog.getInstance();
        authenticatedClient = AuthenticatedClient.getInstance(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            authenticatedClient.setConnectedToNetwork(false);
        } else {
            authenticatedClient.setConnectedToNetwork(true);
        }

        sailthruClient = new SailthruClient(context, authenticatedClient);

        log.d("***********Constructor*************", authenticatedClient.isConnectedToNetwork() + "");
    }

//    acabd25475bfbdb927ca989ef5cba4d0eefb9655d33d127dc8e01432dc01e8ca
//    acabd25475bfbdb927ca989ef5cba4d0eefb9655d33d127dc8e01432dc01e8ca

    /**
     * Public method to register a client to Sailthru
     *
     * @param mode
     * @param domain
     * @param apiKey
     * @param appId
     * @param identification
     * @param uid
     * @param token
     */
    public void register(RegistrationMode mode, String domain,
                         String apiKey, String appId, Identification identification,
                         String uid, String token) {
        if (sailthruClient.passedSanityChecks(mode, domain, apiKey, appId, identification, uid, token)) {
            if (authenticatedClient.isConnectedToNetwork()) {
                log.d("************", "Registering");
                sailthruClient.makeRegistrationRequest(appId, apiKey, uid, identification);
            } else {
                log.d("************", "No network");
                authenticatedClient.setCachedRegisterAttempt();
            }

            sailthruClient.saveCredentials(mode.toString(), domain, apiKey, appId, identification.toString(), uid, token);
        }
        log.d("***********register*************", authenticatedClient.isConnectedToNetwork() + "");
    }

    /**
     * Public method to unregister current client from Sailthru
     */
    public void unregister() {
        log.d("***********Unregister*************", authenticatedClient.isConnectedToNetwork() + "");
        authenticatedClient.deleteHid();
    }

    /**
     *
     * @param tags
     * @param url
     */
    public void sendTags(List<String> tags, String url, String latitude, String longitude) {
        log.d("***********SEND TAGS*************", authenticatedClient.isConnectedToNetwork() + "");
        sailthruClient.addEventToQueue(context, tags, url, latitude, longitude, eventTaskQueue.get());
    }

    /**
     * Used to set an external logger to intercept all log messages
     *
     * @param logger
     */
    public void setLogger(Logger logger) {
        log.setExternalLogger(logger);
    }

    public String getRecommendations(int count, List<String> tags) {
        String recommendations = "";
        if (sailthruClient.canGetRecommendations()) {
            recommendations = RecommendService.getRecommendations(authenticatedClient.getDomain(),
                    authenticatedClient.getHid(), count, tags);
        }

        return recommendations;
    }
}