package com.sailthru.android.sdk.impl.api;

import android.content.Context;

import com.sailthru.android.sdk.impl.async.RegisterAsyncTask;
import com.sailthru.android.sdk.impl.client.AuthenticatedClient;
import com.sailthru.android.sdk.impl.event.EventTaskService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/6/14.
 */
@Module(
    injects = {
            RegisterAsyncTask.class,
            EventTaskService.class
    },
    complete = false,
    library = true
)
public class ApiModule {

    private final Context context;

    public ApiModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    ApiManager provideApiManager() {
        return new ApiManager(context);
    }
}