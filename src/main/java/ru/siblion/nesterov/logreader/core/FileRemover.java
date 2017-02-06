package ru.siblion.nesterov.logreader.core;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.Config;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Класс для удаления пользовательских лог-файлов */
@Startup
@Singleton
public class FileRemover {
    private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private static Config config = Config.getConfig(configFile);

    private static final Logger logger = MyLogger.getLogger();

    @Schedule(minute="0", hour="0") // запуск метода каждый день в полночь
    public void removeOldFiles() {
        logger.log(Level.INFO, "Удаление старых лог-файлов");
        config = Config.getConfig(configFile);

        long configLifeTime = config.getLifeTime();
        File directory = config.getDirectory();

        for(File file : directory.listFiles()) {
            Pattern fileDatePattern = Pattern.compile("d(?<date>.+)h");
            Matcher fileDateMatcher = fileDatePattern.matcher(file.getName());
            fileDateMatcher.find();
            String fileDateString = fileDateMatcher.group("date");

            if (fileDateString == null) {
                continue;
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
            Date currentDate = new Date();
            Date dateOfFile = null;
            try {
                dateOfFile = simpleDateFormat.parse(fileDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long lifeTime = (currentDate.getTime() - dateOfFile.getTime())/1000; // в секундах
            if (!file.isDirectory() &&  lifeTime > configLifeTime)
                file.delete();
        }
    }
}
