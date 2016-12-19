package ru.siblion.nesterov.logreader.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by alexander on 19.12.2016.
 */
@XmlRootElement(name = "logMessages")
@XmlAccessorType(XmlAccessType.FIELD)
public class LogMessages {
    @XmlElement(name = "logMessage")
    private List<LogMessage> logMessages = null;

    public List<LogMessage> getLogMessages() {
        return logMessages;
    }

    public void setLogMessages(List<LogMessage> employees) {
        this.logMessages = employees;
    }
}