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

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable.OnSubscribe;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Producer;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;

/**
 * Emit ints from start to end inclusive.
 */
public final class OnSubscribeRange implements OnSubscribe<Integer> {

    private final int start;
    private final int end;

    public OnSubscribeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void call(final Subscriber<? super Integer> o) {
        o.setProducer(new RangeProducer(o, start, end));
    }

    private static final class RangeProducer implements Producer {
        private final Subscriber<? super Integer> o;
        @SuppressWarnings("unused")
        // accessed by REQUESTED_UPDATER
        private volatile int requested;
        private static final AtomicIntegerFieldUpdater<RangeProducer> REQUESTED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(RangeProducer.class, "requested");
        private volatile int index;
        private final int end;
        private final int start;

        private RangeProducer(Subscriber<? super Integer> o, int start, int end) {
            this.o = o;
            this.index = start;
            this.end = end;
            this.start = start;
        }

        @Override
        public void request(int n) {
            if (n < 0) {
                // fast-path without backpressure
                for (int i = index; i <= end; i++) {
                    if (o.isUnsubscribed()) {
                        return;
                    }
                    o.onNext(i);
                }
                o.onCompleted();
            } else if (n > 0) {
                // backpressure is requested
                int _c = REQUESTED_UPDATER.getAndAdd(this, n);
                if (_c == 0) {
                    while (true) {
                        /*
                         * This complicated logic is done to avoid touching the volatile `index` and `requested` values
                         * during the loop itself. If they are touched during the loop the performance is impacted significantly.
                         */
                        int numLeft = start + (end - index);
                        int e = Math.min(numLeft, requested);
                        boolean completeOnFinish = numLeft < requested;
                        int stopAt = e + index;
                        for (int i = index; i < stopAt; i++) {
                            if (o.isUnsubscribed()) {
                                return;
                            }
                            o.onNext(i);
                        }
                        index += e;
                        if (completeOnFinish) {
                            o.onCompleted();
                            return;
                        }
                        if (REQUESTED_UPDATER.addAndGet(this, -e) == 0) {
                            // we're done emitting the number requested so return
                            return;
                        }
                    }
                }
            }
        }
    }

}
