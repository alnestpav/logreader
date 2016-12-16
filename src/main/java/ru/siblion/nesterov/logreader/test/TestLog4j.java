package ru.siblion.nesterov.logreader.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Created by alexander on 16.12.2016.
 */
public class TestLog4j {
    private static final Logger logger = LogManager.getLogger(TestLog4j.class);

    public static void main(String[] args) {

        logger.info("It's info");

        logger.error("It's error");

        logger.warn("It's warning");


        //logger.trace("It's critical exception");
    }

}
