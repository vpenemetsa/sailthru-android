package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sailthru.android.sdk.impl.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.Event;
import com.sailthru.android.sdk.impl.event.EventModule;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;
import com.sailthru.android.sdk.impl.event.EventTaskService;
import com.sailthru.android.sdk.impl.utils.UtilsModule;

import java.util.ArrayList;
import java.util.Arrays;
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

    Context context;
    AuthenticatedClient authenticatedClient;
    private ObjectGraph objectGraph;
    private static SailthruClient sailthruClient;

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
        authenticatedClient = AuthenticatedClient.getInstance(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            authenticatedClient.setConnectedToNetwork(false);
        } else {
            authenticatedClient.setConnectedToNetwork(true);
        }

        if (sailthruClient == null) {
            sailthruClient = new SailthruClient(context, authenticatedClient);
        }

        objectGraph = ObjectGraph.create(getModules().toArray());
        inject(this);
    }

    private List<Object> getModules() {
        return Arrays.asList(
            new EventModule(context),
            new UtilsModule(context)
        );
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

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
        if (SailthruClient.passedSanityChecks(mode, domain, apiKey, appId, identification, uid, token)) {
            if (authenticatedClient.isConnectedToNetwork()) {
                Log.d("************", "Registering");
                SailthruClient.makeRegistrationRequest(appId, apiKey, uid, identification);
            } else {
                Log.d("************", "No network");
                authenticatedClient.setCachedRegisterAttempt();
            }

            SailthruClient.saveCredentials(mode.toString(), domain, apiKey, appId, identification.toString(), uid, token);
        }
    }

    /**
     * Public method to unregister current client from Sailthru
     */
    public void unregister() {
        authenticatedClient.deleteHid();
    }


    /**
     *
     * @param tags
     * @param url
     */
    public void sendTags(ArrayList<String> tags, String url) {
        Event event = new Event();
        event.addTags(tags);
        event.setUrl(url);
        EventTask eventTask = new EventTask(event);
        eventTaskQueue.get().add(eventTask);
    }
}