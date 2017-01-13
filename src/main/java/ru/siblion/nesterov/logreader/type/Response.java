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

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class Response {
    @XmlElement(name = "outputFile")
    private File outputFile;

    @XmlElement(name = "logMessage")
    private List<LogMessage> logMessages = null;

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
}