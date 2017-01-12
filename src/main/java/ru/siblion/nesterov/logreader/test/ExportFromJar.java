package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 11.01.2017.
 */

@Startup
@Singleton
public class ExportFromJar {

    private static final Logger logger = MyLogger.getLogger();

    static public void exportResource(String resourceName) throws Exception {
        String resource = resourceName.replace('\\', '/'); // метод getResourceAsStream использует '/' для разделения в пути файла
        InputStream file = ExportFromJar.class.getResourceAsStream(resource);
        Files.copy(file, Paths.get("C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\xsl\\"));
    }

    static public void exportResources() {
        logger.log(Level.INFO, "Starting Export from jar");
        try {
            ExportFromJar.exportResource("\\xsl");
            logger.log(Level.INFO, "Export from jar!!!!!!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при экспорте из jar ", e) ;
        }
    }
}
