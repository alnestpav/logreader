package ru.siblion.nesterov.logreader.util;

import java.util.logging.Logger;

/**
 * Created by alexander on 28.12.2016.
 */

/* Класс, предоставляющий логгер для всего приложения */
public class AppLogger {
    private static Logger logger;

    private AppLogger() {
        logger = Logger.getLogger("AppLogger");
    }

    public static Logger getLogger() {
        if (logger == null) {
            new AppLogger();
        }
        return logger;
    }
}
