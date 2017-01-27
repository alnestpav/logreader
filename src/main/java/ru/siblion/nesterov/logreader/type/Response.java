package ru.siblion.nesterov.logreader.type;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.List;

/**
 * Created by alexander on 13.01.2017.
 */

/* Класс, инкапсулирующий ответ на запрос пользователя */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
    @XmlElement(name = "outputFile")
    private File outputFile; // File или String лучше?

    @XmlElement(name = "logMessages") // в клиенте создается метод getLogMessage, проверить почему
    private List<LogMessage> logMessages = null;

    @XmlElement(name = "message")
    private String message;

    public Response() {

    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<LogMessage> logMessages) {
        this.logMessages = logMessages;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
