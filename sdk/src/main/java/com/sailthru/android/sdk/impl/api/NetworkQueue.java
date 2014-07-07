package com.sailthru.android.sdk.impl.api;

import android.content.Context;

import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.Sailthru;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
public class NetworkQueue {

    public void registerCachedAttemptIfAvailable(Context context,
                                                           AuthenticatedClient client) {
        if (client.isCachedRegisterAttempt()) {
            Sailthru stClient = new Sailthru(context);
            Sailthru.RegistrationEnvironment mode;
            if (client.getMode().equals(Sailthru.RegistrationEnvironment.DEV.toString())) {
                mode = Sailthru.RegistrationEnvironment.DEV;
            } else {
                mode = Sailthru.RegistrationEnvironment.PROD;
            }

            Sailthru.Identification identification;
            if (client.getIdentification().
                    equals(Sailthru.Identification.ANONYMOUS.toString())) {
                identification = Sailthru.Identification.ANONYMOUS;
            } else {
                identification = Sailthru.Identification.EMAIL;
            }

            stClient.register(mode, client.getDomain(), client.getApiKey(),
                    client.getAppId(), identification, client.getUid(), client.getToken(),
                    client.getPlatformAppId());

            client.deleteCachedRegisterAttempt();
        }
    }
}