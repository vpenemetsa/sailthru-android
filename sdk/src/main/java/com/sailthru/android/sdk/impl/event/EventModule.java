package com.sailthru.android.sdk.impl.event;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/2/14.
 */
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
