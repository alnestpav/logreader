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
/* Класс для работы с config-файлом config/config.xml,
* в котором записаны некоторые параметры для записи пользовательских лог-файлов */
public class Config { // сделать синглтоном
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


    public Config() {

    }

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


    public static Config getConfig(File configFile) {
        Config config = null;
        try {
            ExportFromArchive.exportConfig();
            config = (Config) JaxbParser.xmlToObject(configFile, new Config());
        } catch (JAXBException e) {
            logger.log(Level.WARNING, "Config файл не найден", e) ;
        }
        return config;
    }

}
