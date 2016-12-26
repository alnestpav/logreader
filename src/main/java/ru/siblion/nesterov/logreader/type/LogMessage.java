package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 07.12.2016.
 */
@XmlRootElement(name = "LogMessage")
public class LogMessage implements Comparable<LogMessage> {

    private XMLGregorianCalendar date;
    private String message;

    public LogMessage(String block) {
        this.date = parseDate(block);
        this.message = block;
    }

    public LogMessage(XMLGregorianCalendar date, String message) {
        this.date = date;
        this.message = message;
    }

    public LogMessage() { // Нужен для JAXB

    }

    private static XMLGregorianCalendar parseDate(String block) {
        String dateRegExp = "\\d\\d.\\d\\d.\\d\\d\\d\\d, \\d\\d?:\\d\\d:\\d\\d,\\d+ (PM|AM) (MSK)";
        Pattern p = Pattern.compile(dateRegExp);
        Matcher m = p.matcher(block);
        m.find();
        String stringDate =  m.group();
        XMLGregorianCalendar date = Utils.stringToXMLGregorianCalendar(stringDate);
        return date;
    }

    public XMLGregorianCalendar getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public void setDate(XMLGregorianCalendar date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "LogMessage:" + "\n\tDate: " + this.date + "\n\tMessage: " + this.message + "\n";
    }

    @Override
    public int compareTo(LogMessage logMessage) {
        return this.date.compare(logMessage.date);
    }
}
