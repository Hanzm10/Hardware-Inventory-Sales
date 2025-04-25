/** 
 *  Copyright 2025 Aaron Ragudos, Hanz Mapua, Peter Dela Cruz, Jerick Remo, Kurt Raneses, and the contributors of the project.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.github.hanzm_10.murico.swingapp.lib.logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import com.github.hanzm_10.murico.swingapp.lib.utils.MuricoStringUtils;

public class MuricoLogFormatter extends Formatter {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BOLD_ON = "\u001B[1m";
    public static final String ANSI_BOLD_OFF = "\u001B[22m";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.US).withZone(ZoneId.systemDefault());

    private void appendException(final StringBuilder stringBuilder, final Throwable exception) {
        stringBuilder.append(exception.getClass().getCanonicalName()).append(": ");
        stringBuilder.append(exception.getMessage());
        stringBuilder.append("\n");

        var stackTrace = exception.getStackTrace();

        for (StackTraceElement element : stackTrace) {
            stringBuilder.append("\tat ").append(element);
            stringBuilder.append("\n");
        }

        Set<Throwable> circularReferences = Collections.newSetFromMap(new IdentityHashMap<>());

        var suppresedExceptions = exception.getSuppressed();

        if (suppresedExceptions.length != 0) {
            stringBuilder.append("\t[SUPPRESSED]: ");
            stringBuilder.append("\n");

            for (Throwable suppressedException : suppresedExceptions) {
                printEnclosedStackTrace(stringBuilder, suppressedException, stackTrace, "\t[SUPPRESSED]: ", "\t",
                        circularReferences);
            }
        }

        var cause = exception.getCause();

        if (cause != null) {
            stringBuilder.append("\t[CAUSE]: ");
            stringBuilder.append("\n");

            printEnclosedStackTrace(stringBuilder, cause, stackTrace, "\t[CAUSE]: ", "\t", circularReferences);
        }
    }

    private int computeFramesInCommon(final StackTraceElement[] trace, final StackTraceElement[] enclosingTrace,
            final int m, final int n) {
        var copyM = m;
        var copyN = n;

        while (copyM >= 0 && copyN >= 0 && trace[copyM].equals(enclosingTrace[copyN])) {
            --copyM;
            --copyN;
        }

        return trace.length - copyM - 1;
    }

    @Override
    public String format(final LogRecord record) {
        var stringBuilder = new StringBuilder();
        stringBuilder.append(ANSI_BOLD_ON);
        stringBuilder.append(ANSI_BLUE);

        var timeString = formatDateString(record.getMillis());
        stringBuilder.append("[");
        stringBuilder.append(timeString);
        stringBuilder.append("]");

        stringBuilder.append(ANSI_YELLOW);
        stringBuilder.append(" ");

        stringBuilder.append("[");
        stringBuilder.append(record.getLevel().getLocalizedName());
        stringBuilder.append("]");

        stringBuilder.append(ANSI_RESET);
        stringBuilder.append(getMessageColor(record));
        stringBuilder.append(" ");

        stringBuilder.append(record.getMessage());

        stringBuilder.append(ANSI_RESET);
        stringBuilder.append(ANSI_BOLD_ON);
        stringBuilder.append(" [at ");
        stringBuilder.append(record.getSourceClassName());
        stringBuilder.append("]");
        stringBuilder.append(ANSI_BOLD_OFF);

        var params = record.getParameters();
        var spaceLen = timeString.length() + 3 + record.getLevel().getLocalizedName().length() + 3;
        var space = MuricoStringUtils.repeat(" ", spaceLen);

        if (params != null) {
            stringBuilder.append("\n");
            stringBuilder.append(MuricoStringUtils.repeat(" ", spaceLen - 10));
            stringBuilder.append(ANSI_YELLOW);
            stringBuilder.append("[Details]");
            stringBuilder.append(getMessageColor(record));

            for (int i = 0, l = params.length; i < l; ++i) {
                stringBuilder.append(params[i]);

                if (i < l - 1) {
                    stringBuilder.append(",\n");
                    stringBuilder.append(space);
                }
            }
        }

        stringBuilder.append(ANSI_RESET);
        stringBuilder.append("\n");

        var thrown = record.getThrown();

        if (thrown != null) {
            stringBuilder.append(getMessageColor(record));
            appendException(stringBuilder, thrown);
        }

        return stringBuilder.toString();
    }

    private String formatDateString(final long ms) {
        return dateTimeFormatter.format(Instant.ofEpochMilli(ms));
    }

    private String getMessageColor(final LogRecord record) {
        if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
            return ANSI_RED;
        } else if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
            return ANSI_YELLOW;
        } else {
            return ANSI_BLACK;
        }
    }

    private void printEnclosedStackTrace(final StringBuilder stringBuilder, final Throwable exception,
            final StackTraceElement[] enclosingTrace, final String caption, final String prefix,
            final Set<Throwable> circularReferences) {
        if (circularReferences.contains(exception)) {
            stringBuilder.append(prefix).append(caption).append(" [CIRCULAR REFERENCE]: ").append(this);
            stringBuilder.append("\n");

            return;
        }

        circularReferences.add(exception);

        var stackTrace = exception.getStackTrace();
        var m = stackTrace.length - 1;
        var n = enclosingTrace.length - 1;
        var framesInCommon = computeFramesInCommon(stackTrace, enclosingTrace, m, n);

        stringBuilder.append(prefix).append(caption).append(exception);
        stringBuilder.append("\n");

        for (var i = 0; i <= m; ++i) {
            stringBuilder.append(prefix).append("\tat ").append(stackTrace[i]);
        }

        if (framesInCommon != 0) {
            stringBuilder.append(prefix).append("\t... ").append(framesInCommon).append(" more");
            stringBuilder.append("\n");
        }

        var suppressedExceptions = exception.getSuppressed();

        if (suppressedExceptions.length != 0) {
            stringBuilder.append(prefix).append("\t[SUPPRESSED]: ");
            stringBuilder.append("\n");

            for (Throwable suppressedException : suppressedExceptions) {
                printEnclosedStackTrace(stringBuilder, suppressedException, enclosingTrace, "\t[SUPPRESSED]: ",
                        prefix + "\t", circularReferences);
            }
        }

        var cause = exception.getCause();

        if (cause != null) {
            stringBuilder.append(prefix).append("\t[CAUSE]: ");
            stringBuilder.append("\n");

            printEnclosedStackTrace(stringBuilder, cause, enclosingTrace, "\t[CAUSE]: ", prefix + "\t",
                    circularReferences);
        }
    }
}
