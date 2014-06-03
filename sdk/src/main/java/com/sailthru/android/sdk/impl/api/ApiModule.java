package com.sailthru.android.sdk.impl.api;


import android.content.Context;

import com.sailthru.android.sdk.impl.async.RegisterAsyncTask;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/1/14.
 */
@Module (
        injects = {
                RegisterAsyncTask.class
        },
        includes = AuthenticatedClient.class,
        complete = false,
        library = true
)
public class ApiModule {

    private final Context context;

    public ApiModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    ApiManager provideApiManager(AuthenticatedClient client) {
        return new ApiManager(client);
    }
}
