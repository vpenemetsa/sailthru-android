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

import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable.Operator;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Producer;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Scheduler;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Scheduler.Worker;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.functions.Action0;

/**
 * Subscribes Observers on the specified Scheduler.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/subscribeOn.png">
 */
public class OperatorSubscribeOn<T> implements Operator<T, Observable<T>> {

    private final Scheduler scheduler;

    public OperatorSubscribeOn(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Subscriber<? super Observable<T>> call(final Subscriber<? super T> subscriber) {
        final Worker inner = scheduler.createWorker();
        subscriber.add(inner);
        return new Subscriber<Observable<T>>(subscriber) {

            @Override
            public void onCompleted() {
                // ignore because this is a nested Observable and we expect only 1 Observable<T> emitted to onNext
            }

            @Override
            public void onError(Throwable e) {
                subscriber.onError(e);
            }

            @Override
            public void onNext(final Observable<T> o) {
                inner.schedule(new Action0() {

                    @Override
                    public void call() {
                        final Thread t = Thread.currentThread();
                        o.unsafeSubscribe(new Subscriber<T>(subscriber) {

                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(T t) {
                                subscriber.onNext(t);
                            }

                            @Override
                            protected Producer onSetProducer(final Producer producer) {
                                return new Producer() {

                                    @Override
                                    public void request(final int n) {
                                        if (Thread.currentThread() == t) {
                                            // don't schedule if we're already on the thread (primarily for first setProducer call)
                                            // see unit test 'testSetProducerSynchronousRequest' for more context on this
                                            producer.request(n);
                                        } else {
                                            inner.schedule(new Action0() {

                                                @Override
                                                public void call() {
                                                    producer.request(n);
                                                }
                                            });
                                        }
                                    }

                                };
                            }

                        });
                    }
                });
            }

        };
    }
}
