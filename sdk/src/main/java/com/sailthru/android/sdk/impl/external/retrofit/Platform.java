/*
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sailthru.android.sdk.impl.external.retrofit;

import android.os.Build;
import android.os.Process;
import com.sailthru.android.sdk.impl.external.gson.Gson;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import com.sailthru.android.sdk.impl.external.retrofit.android.AndroidApacheClient;
import com.sailthru.android.sdk.impl.external.retrofit.android.AndroidLog;
import com.sailthru.android.sdk.impl.external.retrofit.android.MainThreadExecutor;
import com.sailthru.android.sdk.impl.external.retrofit.client.Client;
import com.sailthru.android.sdk.impl.external.retrofit.client.OkClient;
import com.sailthru.android.sdk.impl.external.retrofit.client.UrlConnectionClient;
import com.sailthru.android.sdk.impl.external.retrofit.converter.Converter;
import com.sailthru.android.sdk.impl.external.retrofit.converter.GsonConverter;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static java.lang.Thread.MIN_PRIORITY;

abstract class Platform {
  private static final Platform PLATFORM = findPlatform();

  static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();

  static Platform get() {
    return PLATFORM;
  }

  private static Platform findPlatform() {
    try {
      Class.forName("android.os.Build");
      if (Build.VERSION.SDK_INT != 0) {
        return new Android();
      }
    } catch (ClassNotFoundException ignored) {
    }

//    if (System.getProperty("com.google.appengine.runtime.version") != null) {
//      return new AppEngine();
//    }

    return new Base();
  }

  abstract Converter defaultConverter();
  abstract Client.Provider defaultClient();
  abstract Executor defaultHttpExecutor();
  abstract Executor defaultCallbackExecutor();
  abstract RestAdapter.Log defaultLog();

  /** Provides sane defaults for operation on the JVM. */
  private static class Base extends Platform {
    @Override Converter defaultConverter() {
      return new GsonConverter(new Gson());
    }

    @Override Client.Provider defaultClient() {
      final Client client;
      if (hasOkHttpOnClasspath()) {
        client = OkClientInstantiator.instantiate();
      } else {
        client = new UrlConnectionClient();
      }
      return new Client.Provider() {
        @Override public Client get() {
          return client;
        }
      };
    }

    @Override Executor defaultHttpExecutor() {
      return Executors.newCachedThreadPool(new ThreadFactory() {
        @Override public Thread newThread(final Runnable r) {
          return new Thread(new Runnable() {
            @Override public void run() {
              Thread.currentThread().setPriority(MIN_PRIORITY);
              r.run();
            }
          }, RestAdapter.IDLE_THREAD_NAME);
        }
      });
    }

    @Override Executor defaultCallbackExecutor() {
      return new Utils.SynchronousExecutor();
    }

    @Override RestAdapter.Log defaultLog() {
      return new RestAdapter.Log() {
        @Override public void log(String message) {
          System.out.println(message);
        }
      };
    }
  }

  /** Provides sane defaults for operation on Android. */
  private static class Android extends Platform {
    @Override Converter defaultConverter() {
      return new GsonConverter(new Gson());
    }

    @Override Client.Provider defaultClient() {
      final Client client;
      if (hasOkHttpOnClasspath()) {
        client = OkClientInstantiator.instantiate();
      } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
        client = new AndroidApacheClient();
      } else {
        client = new UrlConnectionClient();
      }
      return new Client.Provider() {
        @Override public Client get() {
          return client;
        }
      };
    }

    @Override Executor defaultHttpExecutor() {
      return Executors.newCachedThreadPool(new ThreadFactory() {
        @Override public Thread newThread(final Runnable r) {
          return new Thread(new Runnable() {
            @Override public void run() {
              Process.setThreadPriority(THREAD_PRIORITY_BACKGROUND);
              r.run();
            }
          }, RestAdapter.IDLE_THREAD_NAME);
        }
      });
    }

    @Override Executor defaultCallbackExecutor() {
      return new MainThreadExecutor();
    }

    @Override RestAdapter.Log defaultLog() {
      return new AndroidLog("Retrofit");
    }
  }

//  private static class AppEngine extends Base {
//    @Override Client.Provider defaultClient() {
//      final UrlFetchClient client = new UrlFetchClient();
//      return new Client.Provider() {
//        @Override public Client get() {
//          return client;
//        }
//      };
//    }
//  }

  /** Determine whether or not OkHttp 1.6 or newer is present on the runtime classpath. */
  private static boolean hasOkHttpOnClasspath() {
    boolean okUrlFactory = false;
    try {
      Class.forName("com.squareup.okhttp.OkUrlFactory");
      okUrlFactory = true;
    } catch (ClassNotFoundException e) {
    }

    boolean okHttpClient = false;
    try {
      Class.forName("com.squareup.okhttp.OkHttpClient");
      okHttpClient = true;
    } catch (ClassNotFoundException e) {
    }

    if (okHttpClient != okUrlFactory) {
      throw new RuntimeException(""
          + "Retrofit detected an unsupported OkHttp on the classpath.\n"
          + "To use OkHttp with this version of Retrofit, you'll need:\n"
          + "1. com.squareup.okhttp:okhttp:1.6.0 (or newer)\n"
          + "2. com.squareup.okhttp:okhttp-urlconnection:1.6.0 (or newer)\n"
          + "Note that OkHttp 2.0.0+ is supported!");
    }

    return okHttpClient;
  }

  /**
   * Indirection for OkHttp class to prevent VerifyErrors on Android 2.0 and earlier when the
   * dependency is not present.
   */
  private static class OkClientInstantiator {
    static Client instantiate() {
      return new OkClient();
    }
  }

  private static boolean hasRxJavaOnClasspath() {
    try {
      Class.forName("rx.Observable");
      return true;
    } catch (ClassNotFoundException ignored) {
    }
    return false;
  }
}
