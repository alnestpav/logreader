package ru.siblion.nesterov.logreader.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by alexander on 28.12.2016.
 */
public class MyLogger {
    private static Logger logger;
    private static final String OUTPUT_FILE = "C:\\Users\\alexander\\IdeaProjects\\logreader\\messages.log";

    private MyLogger() {
        logger = Logger.getLogger("MyLogger");
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler(OUTPUT_FILE, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fileHandler);
        SimpleFormatter formatter = new SimpleFormatter();
        fileHandler.setFormatter(formatter);
    }
    public static Logger getLogger() {
        if (logger == null) {
            new MyLogger();
        }
        return logger;
    }
}
