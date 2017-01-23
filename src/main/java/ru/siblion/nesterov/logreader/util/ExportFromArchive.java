package ru.siblion.nesterov.logreader.util;

import ru.siblion.nesterov.logreader.type.Config;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 11.01.2017.
 */
// Вернуть, ошибка при развертывании была, поэтому закомментил
@Startup
@Singleton
public class ExportFromArchive {
    private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    //private final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test

    private final static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private static Config config;

    private final static File DIRECTORY = new File(DOMAIN_DIRECTORY + "\\logreader\\");

    private static final Logger logger = MyLogger.getLogger();

    static public void exportResource(String resourceName, String copyFilePath) throws Exception {
        /*Pattern fileNamePattern = Pattern.compile("\\w+\\ ");
        Matcher fileNamePatternMatcher;

        fileNamePatternMatcher = fileNamePattern.matcher(resourceName);
        fileNamePatternMatcher.find();
        String fileName = fileNamePatternMatcher.group();
        System.out.println(fileName);
        DIRECTORY.mkdir(); // переписать с помощью nio*/
        String resource = resourceName.replace('\\', '/'); // метод getResourceAsStream использует '/' для разделения в пути файла
        InputStream file = ExportFromArchive.class.getResourceAsStream(resource);
        Files.copy(file, Paths.get(copyFilePath));
    }

    static public void exportConfig() {
        new File(DOMAIN_DIRECTORY + "\\logreader\\config").mkdirs();
        try {
            exportResource("\\config\\config.xml", DIRECTORY + "\\config\\config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public void exportXsls() {
        config = Config.getConfig(configFile);
        new File(DOMAIN_DIRECTORY + "\\logreader\\xsl").mkdirs();
        new File(DOMAIN_DIRECTORY + "\\logreader\\logs").mkdirs();
        try {
            exportResource("\\xsl\\doc.xsl", DIRECTORY + "\\xsl\\doc.xsl");
            exportResource("\\xsl\\html.xsl", DIRECTORY + "\\xsl\\html.xsl");
            exportResource("\\xsl\\pdf.xsl", DIRECTORY + "\\xsl\\pdf.xsl");
            exportResource("\\xsl\\rtf.xsl", DIRECTORY + "\\xsl\\rtf.xsl");

            //exportResource("\\xsl\\siblion_logo.gif", DIRECTORY + "\\xsl\\siblion_logo.gif");;
            exportResource("\\xsl\\siblion_logo.gif", config.getDirectory() + "\\siblion_logo.gif");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при экспорте из архива ", e) ;
        }
    }
}
