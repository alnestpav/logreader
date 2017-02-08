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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 22.12.2016.
 */
@XmlRootElement(name = "log-files")
@XmlAccessorType(XmlAccessType.FIELD)
/* Класс для работы с appConfig-файлом appConfig/appConfig.xml,
* в котором записаны некоторые параметры для записи пользовательских лог-файлов */
public class AppConfig { // сделать синглтоном
    @XmlElement(name = "directory")
    private File directory;

    @XmlElement(name = "life-time")
    private int lifeTime; // в секундах

    @XmlElement(name = "doc-template")
    private File docTemplate;

    @XmlElement(name = "html-template")
    private File htmlTemplate;

    @XmlElement(name = "pdf-template")
    private File pdfTemplate;

    @XmlElement(name = "rtf-template")
    private File rtfTemplate;

    private static final Logger logger = MyLogger.getLogger(); // проверить правильно работает в xml

    private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\appConfig.xml");

    private static AppConfig appConfig;

    public AppConfig() {}

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public File getDocTemplate() { return docTemplate; }

    public void setDocTemplate(File docTemplate) {this.docTemplate = docTemplate; }

    public File getHtmlTemplate() { return htmlTemplate; }

    public void setHtmlTemplate(File htmlTemplate) {this.htmlTemplate = htmlTemplate; }

    public File getPdfTemplate() { return pdfTemplate; }

    public void setPdfTemplate(File pdfTemplate) {this.pdfTemplate = pdfTemplate; }

    public File getRtfTemplate() { return rtfTemplate; }

    public void setRtfTemplate(File rtfTemplate) {this.rtfTemplate = rtfTemplate; }


    public static AppConfig getAppConfig() {
        if (appConfig == null) {
            try {
                ExportFromArchive.exportConfig();
                appConfig = (AppConfig) JaxbParser.xmlToObject(configFile, new AppConfig());
            } catch (JAXBException e) {
                logger.log(Level.WARNING, "AppConfig file not exists", e) ;
            }
        }
        return appConfig;
    }

}
