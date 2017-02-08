package ru.siblion.nesterov.logreader.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by alexander on 22.12.2016.
 */

/* Класс для работы с config-файлом appConfig.xml,
*  в котором записаны некоторые параметры для записи пользовательских лог-файлов
 * кроме того может содержать дополнительные константы, которые используются в других классах */
public class AppConfig {

    public final static String DOMAIN_DIRECTORY = new File("").getAbsolutePath();
    private static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\appConfig.xml");

    private static AppConfig instance;
    private Properties properties;

    private AppConfig() {
        try(FileInputStream fileInput = new FileInputStream(configFile)) {
            properties = new Properties();
            properties.loadFromXML(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    public static AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

}
