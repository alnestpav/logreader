package ru.siblion.nesterov.logreader.util;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 11.01.2017.
 */

@Startup
@Singleton
public class ExportFromArchive {
    private final static String DOMAIN_DIRECTORY = new File("").getAbsolutePath().toString();
    private final static File EXPORT_DIRECTORY = new File(DOMAIN_DIRECTORY + "\\logreader\\");
    private static Properties appConfigProperties;

    private static final Logger logger = AppLogger.getLogger();


    static public void exportResource(String resourceName, String copyFilePath) {
        String resource = resourceName.replace('\\', '/'); // метод getResourceAsStream использует '/' для разделения в пути файла
        InputStream file = ExportFromArchive.class.getResourceAsStream(resource);
        try {
            File checkingFile = new File(copyFilePath); // TODO: 06.02.2017 Нужен ли checkingFile
            if (!checkingFile.exists()) {
                Files.copy(file, Paths.get(copyFilePath));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void exportConfigAndXsls() {
        logger.log(Level.INFO, "Экспорт ресурсов");
        new File(DOMAIN_DIRECTORY + "\\logreader").mkdirs();
        exportResource("\\appConfig.xml", EXPORT_DIRECTORY + "\\appConfig.xml");
        appConfigProperties = AppConfig.getProperties();

        new File(DOMAIN_DIRECTORY + "\\logreader\\xsl").mkdirs();
        new File(DOMAIN_DIRECTORY + "\\logreader\\logs").mkdirs();

        exportResource("\\xsl\\doc.xsl", EXPORT_DIRECTORY + "\\xsl\\doc.xsl");
        exportResource("\\xsl\\html.xsl", EXPORT_DIRECTORY + "\\xsl\\html.xsl");
        exportResource("\\xsl\\pdf.xsl", EXPORT_DIRECTORY + "\\xsl\\pdf.xsl");
        exportResource("\\xsl\\rtf.xsl", EXPORT_DIRECTORY + "\\xsl\\rtf.xsl");

        /* Изображение экспортируется в папку, где будут находится пользовательские логи,
         * чтобы подгружатся в документах формата html, doc */
        exportResource("\\xsl\\siblion_logo.gif", appConfigProperties.get("directory") + "\\siblion_logo.gif");
    }

}
