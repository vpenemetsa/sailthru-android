 /**
  * Copyright 2014 Netflix, Inc.
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy of
  * the License at
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations under
  * the License.
  */
package com.sailthru.android.sdk.impl.external.retrofit.rx.internal.operators;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable.Operator;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.functions.Action0;
import com.sailthru.android.sdk.impl.external.retrofit.rx.observers.SerializedSubscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.subscriptions.SerialSubscription;
import com.sailthru.android.sdk.impl.external.retrofit.rx.subscriptions.Subscriptions;

/**
 * Returns an Observable that emits the items emitted by two or more Observables, one after the
 * other.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/concat.png">
 * @param <T> the source and result value type
 */
public final class OperatorConcat<T> implements Operator<T, Observable<? extends T>> {
    @Override
    public Subscriber<? super Observable<? extends T>> call(final Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<T>(child);
        final SerialSubscription current = new SerialSubscription();
        child.add(current);
        return new ConcatSubscriber<T>(s, current);
    }
    
    static final class ConcatSubscriber<T> extends Subscriber<Observable<? extends T>> {
        final NotificationLite<Observable<? extends T>> nl = NotificationLite.instance();
        private final Subscriber<T> s;
        private final SerialSubscription current;
        final ConcurrentLinkedQueue<Object> queue;
        volatile int wip;
        @SuppressWarnings("rawtypes")
        static final AtomicIntegerFieldUpdater<ConcatSubscriber> WIP_UPDATER
                = AtomicIntegerFieldUpdater.newUpdater(ConcatSubscriber.class, "wip");
        
        public ConcatSubscriber(Subscriber<T> s, SerialSubscription current) {
            super(s);
            this.s = s;
            this.current = current;
            this.queue = new ConcurrentLinkedQueue<Object>();
            add(Subscriptions.create(new Action0() {
                @Override
                public void call() {
                    queue.clear();
                }
            }));
        }
        
        @Override
        public void onNext(Observable<? extends T> t) {
            queue.add(nl.next(t));
            if (WIP_UPDATER.getAndIncrement(this) == 0) {
                subscribeNext();
            }
        }
        
        @Override
        public void onError(Throwable e) {
            s.onError(e);
            unsubscribe();
        }
        
        @Override
        public void onCompleted() {
            queue.add(nl.completed());
            if (WIP_UPDATER.getAndIncrement(this) == 0) {
                subscribeNext();
            }
        }
        void completeInner() {
            if (WIP_UPDATER.decrementAndGet(this) > 0) {
                subscribeNext();
            }
        }
        void subscribeNext() {
            Object o = queue.poll();
            if (nl.isCompleted(o)) {
                s.onCompleted();
            } else 
            if (o != null) {
                Observable<? extends T> obs = nl.getValue(o);
                Subscriber<T> sourceSub = new Subscriber<T>() {

                    @Override
                    public void onNext(T t) {
                        s.onNext(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ConcatSubscriber.this.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        completeInner();
                    }

                };
                current.set(sourceSub);
                obs.unsafeSubscribe(sourceSub);
            }
        }
    }
}
