package com.github.hanzm_10.murico.swingapp.lib.logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MuricoLogger {
    private static final Logger PARENT_LOGGER = Logger.getLogger("com.github.hanzm_10.murico");
    private static final Handler HANDLER = new MuricoLogHandler();

    static {
        HANDLER.setFormatter(new MuricoLogFormatter());
        PARENT_LOGGER.setUseParentHandlers(false);
        PARENT_LOGGER.addHandler(HANDLER);
    }

    public static Logger getDetachedLogger(final Class<?> c) {
        var logger = getLogger(c);
        logger.setParent(PARENT_LOGGER);
        logger.setUseParentHandlers(false);
        logger.addHandler(HANDLER);

        return logger;
    }

    public static Level getLevel() {
        return PARENT_LOGGER.getLevel();
    }

    public static Logger getLogger(final Class<?> c) {
        var logger = Logger.getLogger(c.getName());
        logger.setUseParentHandlers(true);

        return logger;
    }

    public static <T> T log(final T obj) {
        PARENT_LOGGER.info(String.valueOf(obj));
        return obj;
    }

    public static void setLevel(final Level level) {
        PARENT_LOGGER.setLevel(level);
        HANDLER.setLevel(level);
    }
}
