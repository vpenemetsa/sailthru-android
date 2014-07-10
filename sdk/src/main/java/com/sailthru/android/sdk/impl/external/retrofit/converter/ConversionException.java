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
package com.sailthru.android.sdk.impl.external.retrofit.converter;

/** Indicate that conversion was unable to complete successfully. */
@SuppressWarnings("UnusedDeclaration")
public class ConversionException extends Exception {
  public ConversionException(String message) {
    super(message);
  }

  public ConversionException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ConversionException(Throwable throwable) {
    super(throwable);
  }
}