package ru.siblion.nesterov.logreader.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

/**
 * Created by alexander on 22.12.2016.
 */
@XmlRootElement(name = "log-files")
@XmlAccessorType(XmlAccessType.FIELD)
/* Класс для работы с config-файлом config/logreader.xml,
* в котором записаны некоторые параметры для записи пользовательских лог-файлов */
public class Config {
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

}
