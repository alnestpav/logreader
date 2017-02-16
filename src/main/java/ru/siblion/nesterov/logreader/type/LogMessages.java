package ru.siblion.nesterov.logreader.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by alexander on 19.12.2016.
 */

/* Класс, инкапсулирующий объект класса Request и список объектов класса LogMessage,
* используется при записи в пользовательские лог-файлы, конвертируется в xml */
@XmlRootElement(name = "logMessages")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogMessages {
    @XmlElement(name = "request")
    private Request request;

    @XmlElement(name = "logMessage")
    private List<LogMessage> logMessages = null;

    public LogMessages(Request request, List<LogMessage> logMessageList) {
        this.request = request;
        logMessages = logMessageList;
    }

    public LogMessages() {}

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<LogMessage> employees) {
        this.logMessages = employees;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}