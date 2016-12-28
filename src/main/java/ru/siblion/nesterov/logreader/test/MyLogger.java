package ru.siblion.nesterov.logreader.test;

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

    private MyLogger() {
        logger = Logger.getLogger("MyLogger");
        Handler fileHandler = null;
        try {
            fileHandler = new FileHandler("C:\\Users\\alexander\\IdeaProjects\\logreader\\messages6.log", true);
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
