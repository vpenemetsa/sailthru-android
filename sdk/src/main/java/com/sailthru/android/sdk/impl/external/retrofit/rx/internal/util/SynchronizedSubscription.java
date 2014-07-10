package com.sailthru.android.sdk.impl.external.retrofit.rx.internal.util;

import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscription;

public class SynchronizedSubscription implements Subscription {

    private final Subscription s;

    public SynchronizedSubscription(Subscription s) {
        this.s = s;
    }

    @Override
    public synchronized void unsubscribe() {
        s.unsubscribe();
    }

    @Override
    public synchronized boolean isUnsubscribed() {
        return s.isUnsubscribed();
    }

}
