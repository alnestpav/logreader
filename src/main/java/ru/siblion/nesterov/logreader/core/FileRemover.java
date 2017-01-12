package ru.siblion.nesterov.logreader.core;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/* Класс для удаления пользовательских лог-файлов */
@Startup
@Singleton
public class FileRemover {
    private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private final static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private Config config;

    private static final Logger logger = MyLogger.getLogger();

    @Schedule(minute="0", hour="0") // запуск метода каждый день в полночь
    public void removeOldFiles() {
        logger.log(Level.INFO, "Удаление старых лог-файлов");
        try {
            config = (Config) JaxbParser.xmlToObject(configFile, new Config()); // второй параметр возможно нужно поменять в сигнатуре метода
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(config.getDirectory());
        System.out.println(config.getLifeTime());

        long configLifeTime = config.getLifeTime();
        File directory = config.getDirectory();
        for(File file : directory.listFiles()) {
            String stringDateOfFile = null;
            try {
                stringDateOfFile = file.getName().substring(0, 28); // здесь программа зависала, хотя должно быть исключение
            } catch (StringIndexOutOfBoundsException e) {
                logger.log(Level.WARNING, "В папке находятся не только экспортированные лог-файлы", e) ;
                continue;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
            Date currentDate = new Date();
            Date dateOfFile = null;
            try {
                dateOfFile = simpleDateFormat.parse(stringDateOfFile);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long lifeTime = (currentDate.getTime() - dateOfFile.getTime())/1000; // в секундах
            if (!file.isDirectory() &&  lifeTime > configLifeTime)
                file.delete();
        }
    }

}
