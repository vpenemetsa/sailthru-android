// Copyright 2012 Square, Inc.
package com.sailthru.android.sdk.impl.external.tape;

import com.sailthru.android.sdk.impl.external.tape.*;

/**
 * Inject dependencies into tasks of any kind.
 *
 * @param <T> The type of tasks to inject.
 */
public interface TaskInjector<T extends com.sailthru.android.sdk.impl.external.tape.Task> {
  void injectMembers(T task);
}
