package com.sailthru.android.sdk.impl.utils;

import android.content.Context;

import com.sailthru.android.sdk.Sailthru;
import com.sailthru.android.sdk.impl.event.EventModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Vijay Penemetsa on 6/2/14.
 */

@Module (
    injects = Sailthru.class,
    includes = EventModule.class,
    complete = false,
    library = true
)
public class UtilsModule {

    private final Context context;

    public UtilsModule(Context context) {
        this.context = context;
    }

    @Provides @Singleton
    AppRegisterUtils provideAppRegisterUtils() {
        return new AppRegisterUtils();
    }

    @Provides @Singleton
    DeviceUtils provideDeviceUtils() {
        return new DeviceUtils(context);
    }

    @Provides @Singleton
    SecurePreferences provideSecurePreferences() {
        return new SecurePreferences(context);
    }

}
