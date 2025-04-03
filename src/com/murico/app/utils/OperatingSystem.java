package com.murico.app.utils;

import java.lang.management.ManagementFactory;
import java.util.Locale;

public enum OperatingSystem {
  Windows, MacOS, Linux;

  public static final OperatingSystem CURRENT_OS;

  static {
    var runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    var arguments = runtimeMXBean.getInputArguments();

    for (String arg : arguments) {
      if (arg.startsWith("-Dos.name")) {
        throw new SecurityException("OS Name has been tampered with.");
      } else if (arg.startsWith("-Dos.arch")) {
        throw new SecurityException("OS Architecture has been tampered with.");
      }
    }

    var operatingSystem = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

    if (operatingSystem.contains("win") || operatingSystem.contains("darwin")) {
      CURRENT_OS = Windows;
    } else if (operatingSystem.contains("mac")) {
      CURRENT_OS = MacOS;
    } else if (operatingSystem.contains("nux")) {
      CURRENT_OS = Linux;
    } else {
      throw new UnsupportedOperationException("Unsupported operating system: " + operatingSystem);
    }
  }
}
