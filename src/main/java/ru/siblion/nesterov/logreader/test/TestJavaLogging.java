package ru.siblion.nesterov.logreader.test;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 16.12.2016.
 */
public class TestJavaLogging {

    public static void main(String[] args) {
        Logger fLogger = Logger.getLogger(TestJavaLogging.class.getName());

        fLogger.finest("this is finest");
        fLogger.finer("this is finer");
        fLogger.fine("this is fine");
        fLogger.config("this is config");
        fLogger.info("this is info");
        fLogger.warning("this is a warning");
        fLogger.severe("this is severe");
        try {
            System.out.println(10/0);
        } catch(Exception e) {
            fLogger.logp(Level.INFO, TestJavaLogging.class.getName(), e.getMessage(), "");
        }


    }
}
