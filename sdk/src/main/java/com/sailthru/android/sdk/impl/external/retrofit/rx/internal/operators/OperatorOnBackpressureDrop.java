/**
 * Copyright 2014 Netflix, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sailthru.android.sdk.impl.external.retrofit.rx.internal.operators;

import java.util.concurrent.atomic.AtomicInteger;

import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable.Operator;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Producer;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;

public class OperatorOnBackpressureDrop<T> implements Operator<T, T> {

    @Override
    public Subscriber<? super T> call(final Subscriber<? super T> child) {
        final AtomicInteger requested = new AtomicInteger();

        child.setProducer(new Producer() {

            @Override
            public void request(int n) {
                requested.getAndAdd(n);
            }

        });
        return new Subscriber<T>(child) {

            @Override
            public void onCompleted() {
                child.onCompleted();
            }

            @Override
            public void onError(Throwable e) {
                child.onError(e);
            }

            @Override
            public void onNext(T t) {
                if (requested.get() > 0) {
                    child.onNext(t);
                    requested.decrementAndGet();
                }
            }

        };
    }

}
