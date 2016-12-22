package ru.siblion.nesterov.logreader.test;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.concurrent.*;

public class FileHandler {
    private final static File configFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\config\\logreader.xml");
    public static void main(String[] args) {
        Config config = new Config();
        try {
            config = (Config) JaxbParser.xmlToObject(configFile, config); // второй параметр возможно нужно поменять в сигнатуре метода
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(config.getDirectory());
        System.out.println(config.getTimeInterval());

        ScheduledExecutorService ses =
                Executors.newScheduledThreadPool(1);
        Runnable pinger = new Runnable() {
            public void run() {
                System.out.println("Clearing directory!");
            }
        };
        ses.scheduleAtFixedRate(pinger, 0, config.getTimeInterval(), TimeUnit.SECONDS);
    }

    public void clearDirectory(File dir) {
        for(File file : dir.listFiles())
            if (!file.isDirectory())
                file.delete();
    }

}
