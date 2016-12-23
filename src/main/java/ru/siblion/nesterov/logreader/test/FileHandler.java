package ru.siblion.nesterov.logreader.test;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.type.Request;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileHandler {
    private final static File configFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\config\\logreader.xml");
    private Config config;

    private static final Logger logger = Logger.getLogger(Request.class.getName());


    public void startFileTracking() {
        try {
            config = (Config) JaxbParser.xmlToObject(configFile, new Config()); // второй параметр возможно нужно поменять в сигнатуре метода
            System.out.println(config.getDirectory());
            System.out.println(config.getLifeTime());

            ScheduledExecutorService ses =
                    Executors.newScheduledThreadPool(1);
            System.out.println("runnable");
            Runnable pinger = new Runnable() {
                public void run() {
                    System.out.println("IN THREAD");
                    removeOldFiles(config.getDirectory());
                }
            };
            ses.scheduleAtFixedRate(pinger, 1, 2, TimeUnit.SECONDS);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void removeOldFiles(File directory) {
        long configLifeTime = config.getLifeTime();
        for(File file : directory.listFiles()) {
            String stringDateOfFile = null;
            try {
                stringDateOfFile = file.getName().substring(0, 28); // здесь программа зависала, хотя должно быть исключение
            } catch (StringIndexOutOfBoundsException e) {
                logger.log(Level.WARNING, "В папке находятся не только экспортированные log файлы", e) ;
                continue;
            }
            System.out.println("dddddddateeeeeeeee " + stringDateOfFile);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
            Date currentDate = new Date();
            Date dateOfFile = null;
            try {
                dateOfFile = simpleDateFormat.parse(stringDateOfFile);
            } catch (ParseException e) {
                //break;
            }
            System.out.println("date " + dateOfFile);
            System.out.println("config " + configLifeTime);
            long lifeTime = (currentDate.getTime() - dateOfFile.getTime())/1000; // в секундах
            System.out.println(lifeTime);
            if (!file.isDirectory() &&  lifeTime > configLifeTime)
                file.delete();
        }
    }


}
