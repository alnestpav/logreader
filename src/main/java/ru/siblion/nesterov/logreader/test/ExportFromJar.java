package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 11.01.2017.
 */

@Startup
@Singleton
public class ExportFromJar {
    //private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test

    private final static File DIRECTORY = new File(DOMAIN_DIRECTORY + "\\logreader\\");

    private static final Logger logger = MyLogger.getLogger();

    static public void exportResource(String resourceName) throws Exception {
        /*Pattern fileNamePattern = Pattern.compile("\\w+\\ ");
        Matcher fileNamePatternMatcher;

        fileNamePatternMatcher = fileNamePattern.matcher(resourceName);
        fileNamePatternMatcher.find();
        String fileName = fileNamePatternMatcher.group();
        System.out.println(fileName);
        DIRECTORY.mkdir(); // переписать с помощью nio*/
        String filePath = DIRECTORY + resourceName;
        String resource = resourceName.replace('\\', '/'); // метод getResourceAsStream использует '/' для разделения в пути файла
        InputStream file = ExportFromJar.class.getResourceAsStream(resource);
        Files.copy(file, Paths.get(filePath));
    }

    static public void exportResources() {
        logger.log(Level.INFO, "Starting Export from jar");
        new File(DOMAIN_DIRECTORY + "\\logreader\\xsl").mkdirs();
        new File(DOMAIN_DIRECTORY + "\\logreader\\config").mkdirs();
        try {
            ExportFromJar.exportResource("\\config\\config.xml");
            ExportFromJar.exportResource("\\xsl\\doc.xsl");
            ExportFromJar.exportResource("\\xsl\\html.xsl");
            ExportFromJar.exportResource("\\xsl\\pdf.xsl");
            ExportFromJar.exportResource("\\xsl\\rtf.xsl");
            ExportFromJar.exportResource("\\xsl\\siblion_logo.gif");
            logger.log(Level.INFO, "Export from jar!!!!!!");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при экспорте из jar ", e) ;
        }
    }
}
