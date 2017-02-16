package ru.siblion.nesterov.logreader.type;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by alexander on 13.01.2017.
 */

/* Класс, инкапсулирующий ответ на запрос пользователя */
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
    /* При возникновении нештатной ситуации во время обработки запроса в сообщение записывается информация об ошибке */
    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "outputFile")
    private String outputFile;

    @XmlElement(name = "logMessages") // в клиенте создается метод getLogMessage, проверить почему
    private List<LogMessage> logMessages = null;


    public Response() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<LogMessage> logMessages) {
        this.logMessages = logMessages;
    }

}
