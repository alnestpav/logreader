package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.DateInterval;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 15.12.2016.
 */
@XmlRootElement(name = "Request")
public class Request {
    @XmlElement
    private String string;
    @XmlElement
    private String location;
    @XmlElement
    private List<DateInterval> dateIntervals;

    public Request(String string, String location, List<DateInterval> dateIntervals) {
        this.string = string;
        this.location = location;
        this.dateIntervals = dateIntervals;
    }

    public Request() { // Нужен для JAXB

    }

    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public List<DateInterval> getDateIntervals() {
        return dateIntervals;
    }
    public void setDateFrom(List<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }


    public List<LogMessage> getListOfLogMessages() {
        List<LogMessage> logMessageList = null;
        for (DateInterval dateInterval : dateIntervals) {
            try {
                logMessageList = getLogMessages(string, location, dateInterval.getDateFrom(), dateInterval.getDateTo());
                Collections.sort(logMessageList);
            } catch (Exception e) {
                e.printStackTrace();
                //throw new IllegalArgumentException("Ошибка: неверный атрибут location");
            }
        }

        return logMessageList;
    }

}
