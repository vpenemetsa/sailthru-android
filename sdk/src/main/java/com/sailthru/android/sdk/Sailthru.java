package com.sailthru.android.sdk;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sailthru.android.sdk.impl.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.EventModule;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 */
public class Sailthru extends AbstractSailthru {

    @Inject
    EventTaskQueue eventTaskQueue;

    private ObjectGraph objectGraph;

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

        objectGraph = ObjectGraph.create(new EventModule(context));
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
        if (passedSanityChecks(mode, domain, apiKey, appId, identification, uid, token)) {
            if (authenticatedClient.isConnectedToNetwork()) {
                Log.d("************", "Registering");
                makeRegistrationRequest(appId, apiKey, uid, identification);
            } else {
                Log.d("************", "No network");
                authenticatedClient.setCachedRegisterAttempt();
            }

            saveCredentials(mode.toString(), domain, apiKey, appId, identification.toString(), uid, token);
        }
    }

    /**
     * Public method to unregister current client from Sailthru
     */
    public void unregister() {
        authenticatedClient.deleteHid();
    }

    public void sendTags(ArrayList<String> tags, String email) {

    }
}