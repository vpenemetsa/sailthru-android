package com.sailthru.android.sdk.api;

import android.content.Context;

import com.sailthru.android.sdk.ST_AuthenticatedClient;
import com.sailthru.android.sdk.SailthruClient;
import com.sailthru.android.sdk.SailthruClient_Abstract;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
public class API_Queue {

    public static void registerCachedAttemptIfAvailable(Context context,
                                                        ST_AuthenticatedClient client) {
        if (client.isCachedRegisterAttempt()) {
            SailthruClient stClient = new SailthruClient(context);
            SailthruClient.RegistrationMode mode;
            if (client.getMode().equals(SailthruClient_Abstract.RegistrationMode.DEV.toString())) {
                mode = SailthruClient_Abstract.RegistrationMode.DEV;
            } else {
                mode = SailthruClient_Abstract.RegistrationMode.PROD;
            }

            SailthruClient_Abstract.Identification identification;
            if (client.getIdentification().
                    equals(SailthruClient_Abstract.Identification.ANONYMOUS.toString())) {
                identification = SailthruClient_Abstract.Identification.ANONYMOUS;
            } else {
                identification = SailthruClient_Abstract.Identification.EMAIL;
            }

            stClient.register(mode, client.getDomain(), client.getApiKey(),
                    client.getAppId(), identification, client.getUid(), client.getToken());

            client.deleteCachedRegisterAttempt();
        }
    }
}