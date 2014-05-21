package com.sailthru.android.sdk;

import android.content.Context;

/**
 * Created by Vijay Penemetsa on 5/21/14.
 */
class Api_Queue {

    public static void registerCachedAttemptIfAvailable(Context context,
                                                        St_AuthenticatedClient client) {
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