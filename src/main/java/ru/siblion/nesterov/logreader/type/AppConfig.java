package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.ExportFromArchive;
import ru.siblion.nesterov.logreader.util.JaxbParser;
import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 22.12.2016.
 */

/* Класс для работы с config-файлом appConfig.xml,
*  в котором записаны некоторые параметры для записи пользовательских лог-файлов */
public class AppConfig { // сделать синглтоном

    private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
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
