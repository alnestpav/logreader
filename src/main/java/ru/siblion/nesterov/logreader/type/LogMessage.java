package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.Methods;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by alexander on 07.12.2016.
 */
public class LogMessage implements Comparable<LogMessage> {
    XMLGregorianCalendar date;
    String message;

    LogMessage(XMLGregorianCalendar date, String message) {
        this.date = date;
        this.message = message;
    }

    public LogMessage(String block) {
        this.date = Methods.getDate(block);
        this.message = block;
    }

    public XMLGregorianCalendar getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int compareTo(LogMessage logMessage) {
        return this.date.compare(logMessage.date);
    }
}
