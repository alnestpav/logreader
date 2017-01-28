package ru.siblion.nesterov.logreader.util;

import java.util.logging.Logger;

/**
 * Created by alexander on 28.12.2016.
 */

/* Класс, предоставляющий логгер, отличающийся от стандартного */
public class MyLogger {
    private static Logger logger;

    private MyLogger() {
        logger = Logger.getLogger("MyLogger");
    }

    public static Logger getLogger() {
        if (logger == null) {
            new MyLogger();
        }
        return logger;
    }
}
