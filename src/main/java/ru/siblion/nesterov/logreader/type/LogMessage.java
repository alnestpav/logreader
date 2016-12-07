package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.Methods;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by alexander on 07.12.2016.
 */
public class LogMessage {
    XMLGregorianCalendar date;
    String message;

    LogMessage(XMLGregorianCalendar date, String message) {
        this.date = date;
        this.message = message;
    }

    public LogMessage(String block) {
        this.date = Methods.getDate(block.substring(1, 27));
        this.message = block;
    }

    public XMLGregorianCalendar getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
