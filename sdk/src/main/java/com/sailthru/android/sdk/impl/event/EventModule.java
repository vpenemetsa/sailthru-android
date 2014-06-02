package com.sailthru.android.sdk.impl.event;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sailthru.android.sdk.Sailthru;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/2/14.
 */

@Module(
    injects = EventTaskService.class,
    complete = false,
    library = true
)
public class EventModule {

    private final Context context;

    public EventModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    EventTaskQueue provideTaskQueue(Gson gson) {
        return EventTaskQueue.create(context, gson);
    }

    @Provides @Singleton
    Gson provideGson() {
        return new GsonBuilder().create();
    }
}
