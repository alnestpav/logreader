package ru.siblion.nesterov.logreader.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by alexander on 22.12.2016.
 */

/* Класс для работы с config-файлом appConfig.xml,
 * в котором записаны некоторые параметры для записи пользовательских лог-файлов
 * кроме того может содержать дополнительные константы, которые используются в других классах */
public class AppConfig {

    private static Properties properties;
    public final static String DOMAIN_DIRECTORY = new File("").getAbsolutePath();
    private final static File CONFIG_FILE = new File(DOMAIN_DIRECTORY + "\\logreader\\appConfig.xml");

    static {
        try(FileInputStream fileInput = new FileInputStream(CONFIG_FILE)) {
            properties = new Properties();
            properties.loadFromXML(fileInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProperties() {
        return properties;
    }


}
