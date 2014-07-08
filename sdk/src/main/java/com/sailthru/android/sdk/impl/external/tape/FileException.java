// Copyright 2012 Square, Inc.
package com.sailthru.android.sdk.impl.external.tape;

import java.io.File;
import java.io.IOException;

/** Encapsulates an {@link java.io.IOException} in an extension of {@link RuntimeException}. */
public class FileException extends RuntimeException {
  private final File file;

  public FileException(String message, IOException e, File file) {
    super(message, e);
    this.file = file;
  }

  public File getFile() {
    return file;
  }
}
