package com.sailthru.android.sdk.impl.client;

import android.content.Context;

import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.event.EventTask;
import com.sailthru.android.sdk.impl.event.EventTaskQueue;
import com.sailthru.android.sdk.impl.event.EventTaskService;
import com.sailthru.android.sdk.impl.tests.EventTaskQueueTest;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/4/14.
 */

@Module(
        injects = {
                Sailthru.class,
                EventTaskQueue.class,
                EventTaskService.class,
                EventTaskQueueTest.class
        },
        complete = false,
        library = true
)
public class AuthenticatedClientModule {

    private final Context context;

    private static AuthenticatedClient authenticatedClient;

    public AuthenticatedClientModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    AuthenticatedClient provideAuthenticatedClient() {
        if (authenticatedClient == null) {
            authenticatedClient = new AuthenticatedClient(context);
        }

        return authenticatedClient;
    }

}
