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

import java.util.Iterator;

import com.sailthru.android.sdk.impl.external.retrofit.rx.Observable;
import com.sailthru.android.sdk.impl.external.retrofit.rx.Subscriber;
import com.sailthru.android.sdk.impl.external.retrofit.rx.exceptions.Exceptions;

/**
 * Returns an Iterable that always returns the item most recently emitted by an Observable, or a
 * seed value if no item has yet been emitted.
 * <p>
 * <img width="640" src="https://github.com/Netflix/RxJava/wiki/images/rx-operators/B.mostRecent.png">
 */
public final class BlockingOperatorMostRecent {

    /**
     * Returns an {@code Iterable} that always returns the item most recently emitted by the {@code Observable}.
     *
     * @param source
     *            the source {@code Observable}
     * @param initialValue
     *            a default item to return from the {@code Iterable} if {@code source} has not yet emitted any
     *            items
     * @return an {@code Iterable} that always returns the item most recently emitted by {@code source}, or
     *         {@code initialValue} if {@code source} has not yet emitted any items
     */
    public static <T> Iterable<T> mostRecent(final Observable<? extends T> source, final T initialValue) {

        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                MostRecentObserver<T> mostRecentObserver = new MostRecentObserver<T>(initialValue);
                final MostRecentIterator<T> nextIterator = new MostRecentIterator<T>(mostRecentObserver);

                /**
                 * Subscribe instead of unsafeSubscribe since this is the final subscribe in the chain
                 * since it is for BlockingObservable.
                 */
                source.subscribe(mostRecentObserver);

                return nextIterator;
            }
        };

    }

    private static class MostRecentIterator<T> implements Iterator<T> {

        private final MostRecentObserver<T> observer;

        private MostRecentIterator(MostRecentObserver<T> observer) {
            this.observer = observer;
        }

        @Override
        public boolean hasNext() {
            return !observer.isCompleted();
        }

        @Override
        public T next() {
            if (observer.getThrowable() != null) {
                throw Exceptions.propagate(observer.getThrowable());
            }
            return observer.getRecentValue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Read only iterator");
        }
    }

    private static class MostRecentObserver<T> extends Subscriber<T> {
        static final NotificationLite<Object> nl = NotificationLite.instance();
        volatile Object value;
        
        private MostRecentObserver(T value) {
            this.value = nl.next(value);
        }

        @Override
        public void onCompleted() {
            value = nl.completed();
        }

        @Override
        public void onError(Throwable e) {
            value = nl.error(e);
        }

        @Override
        public void onNext(T args) {
            value = nl.next(args);
        }

        private boolean isCompleted() {
            return nl.isCompleted(value);
        }

        private Throwable getThrowable() {
            Object v = value;
            return nl.isError(v) ? nl.getError(v) : null;
        }

        @SuppressWarnings("unchecked")
        private T getRecentValue() {
            return (T)value;
        }

    }
}
