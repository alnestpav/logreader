package ru.siblion.nesterov.logreader.core;

/**
 * Created by alexander on 22.12.2016.
 */
import ru.siblion.nesterov.logreader.util.AppLogger;
import ru.siblion.nesterov.logreader.util.AppConfig;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Класс для удаления пользовательских лог-файлов */
@Startup
@Singleton
public class FileRemover {
    private final static Properties APP_CONFIG_PROPERTIES = AppConfig.getProperties();

    private static final Logger logger = AppLogger.getLogger();

    @Schedule(minute="0", hour="0") // запуск метода каждый день в полночь
    public void removeOldFiles() {
        logger.log(Level.INFO, "Удаление старых лог-файлов");

        long configLifeTime = Integer.parseInt(APP_CONFIG_PROPERTIES.getProperty("life-time"));
        File directory = new File(APP_CONFIG_PROPERTIES.getProperty("directory"));
        if (directory.exists()) {
            for(File file : directory.listFiles()) {
                Pattern fileDatePattern = Pattern.compile("d(?<date>.+)h");
                Matcher fileDateMatcher = fileDatePattern.matcher(file.getName());
                if (!fileDateMatcher.find()) {
                    continue; // если файл в папке не соответствует шаблону, то пропускаем его
                }
                String fileDateString = fileDateMatcher.group("date");

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
                Date currentDate = new Date();
                Date fileDate = null;
                try {
                    fileDate = simpleDateFormat.parse(fileDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long lifeTime = (currentDate.getTime() - fileDate.getTime())/1000; // в секундах
                if (!file.isDirectory() &&  lifeTime > configLifeTime)
                    file.delete();
            }
        }
    }
}
