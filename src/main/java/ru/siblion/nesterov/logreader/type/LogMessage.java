package ru.siblion.nesterov.logreader.type;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ru.siblion.nesterov.logreader.util.*;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 07.12.2016.
 */

/* Класс, инкапсулирующий лог-сообщение */
@XmlRootElement(name = "LogMessage")
public class LogMessage implements Comparable<LogMessage> {

    private XMLGregorianCalendar date;
    private String message;

    private static final Logger logger = MyLogger.getLogger();

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
        String dateRegExp = "<[^<>]*>";
        Pattern p = Pattern.compile(dateRegExp);
        Matcher m = p.matcher(block);
        int positionOfTimestamp = 10; // позиция <timestamp>
        for (int i = 0; i <= (positionOfTimestamp - 1); i++) {
            m.find();
        }
        logger.log(Level.INFO, "count: " + m.groupCount());
        String stringTimestamp =  m.group().substring(1, 14);
        logger.log(Level.INFO, "stringTimestamp: " + stringTimestamp + "count: " + m.groupCount());
        Timestamp stamp = new Timestamp(Long.parseLong(stringTimestamp));
        Date date = new Date(stamp.getTime());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        XMLGregorianCalendar xmlGregorianDate = new XMLGregorianCalendarImpl();
        try {
            xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Ошибка при получения экземпляра XMLGregorianCalendar", e);
        }
        return xmlGregorianDate;
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
