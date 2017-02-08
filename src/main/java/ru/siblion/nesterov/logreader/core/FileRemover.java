package ru.siblion.nesterov.logreader.core;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.AppConfig;

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
    private static final AppConfig APP_CONFIG = AppConfig.getAppConfig();

    private static final Logger logger = MyLogger.getLogger();

    @Schedule(minute="0", hour="0") // запуск метода каждый день в полночь
    public void removeOldFiles() {
        logger.log(Level.INFO, "Удаление старых лог-файлов");

        long configLifeTime = APP_CONFIG.getLifeTime();
        File directory = APP_CONFIG.getDirectory();

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
