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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable.Operator;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.exceptions.CompositeException;
import com.sailthru.android.sdk.impl.external.retrofit.rx.observers.SerializedSubscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.subscriptions.CompositeSubscription;

/**
 * This behaves like {@link OperatorMerge} except that if any of the merged Observables notify of
 * an error via <code>onError</code>, mergeDelayError will refrain from propagating that error
 * notification until all of the merged Observables have finished emitting items.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/mergeDelayError.png">
 * <p>
 * Even if multiple merged Observables send <code>onError</code> notifications, mergeDelayError will
 * only invoke the <code>onError</code> method of its Observers once.
 * <p>
 * This operation allows an Observer to receive all successfully emitted items from all of the
 * source Observables without being interrupted by an error notification from one of them.
 * <p>
 * NOTE: If this is used on an Observable that never completes, it will never call
 * <code>onError</code> and will effectively swallow errors.
 *
 * @param <T> the source and result value type
 */
public final class OperatorMergeDelayError<T> implements Operator<T, Observable<? extends T>> {
    
    @Override
    public Subscriber<? super Observable<? extends T>> call(Subscriber<? super T> child) {
        final SerializedSubscriber<T> s = new SerializedSubscriber<T>(child);
        final CompositeSubscription csub = new CompositeSubscription();
        child.add(csub);
        
        return new MergeDelayErrorSubscriber<T>(s, csub);
    }
    
    static final class MergeDelayErrorSubscriber<T> extends Subscriber<Observable<? extends T>> {
        final Subscriber<? super T> s;
        final CompositeSubscription csub;
        final ConcurrentLinkedQueue<Throwable> exceptions = new ConcurrentLinkedQueue<Throwable>();
        
        volatile int wip;
        @SuppressWarnings("rawtypes")
        static final AtomicIntegerFieldUpdater<MergeDelayErrorSubscriber> WIP_UPDATER
                = AtomicIntegerFieldUpdater.newUpdater(MergeDelayErrorSubscriber.class, "wip");
        
        public MergeDelayErrorSubscriber(Subscriber<? super T> s, CompositeSubscription csub) {
            super(s);
            this.s = s;
            this.csub = csub;
            this.wip = 1;
        }
        
        @Override
        public void onNext(Observable<? extends T> t) {
            WIP_UPDATER.incrementAndGet(this);
            
            Subscriber<T> itemSub = new Subscriber<T>() {
                /** Make sure terminal events are handled once to avoid wip problems. */
                boolean once = true;
                @Override
                public void onNext(T t) {
                    // prevent misbehaving source to emit past the error
                    if (once) {
                        try {
                            s.onNext(t);
                        } catch (Throwable e) {
                            // in case the source doesn't properly handle exceptions
                            onError(e);
                        }
                    }
                }
                
                @Override
                public void onError(Throwable e) {
                    if (once) {
                        once = false;
                        error(e);
                    }
                }
                
                @Override
                public void onCompleted() {
                    if (once) {
                        once = false;
                        try {
                            complete();
                        } finally {
                            csub.remove(this);
                        }
                    }
                }
                
            };
            csub.add(itemSub);
            
            t.unsafeSubscribe(itemSub);
        }
        
        @Override
        public void onError(Throwable e) {
            error(e);
        }
        
        @Override
        public void onCompleted() {
            complete();
        }

        void error(Throwable e) {
            exceptions.add(e);
            complete();
        }

        void complete() {
            if (WIP_UPDATER.decrementAndGet(this) == 0) {
                if (exceptions.isEmpty()) {
                    s.onCompleted();
                } else
                    if (exceptions.size() > 1) {
                        s.onError(new CompositeException(exceptions));
                    } else {
                        s.onError(exceptions.peek());
                    }
                exceptions.clear();
                unsubscribe();
            }
        }
    }
}
