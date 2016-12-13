package ru.siblion.nesterov.logreader.type;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ru.siblion.nesterov.logreader.util.Methods;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 07.12.2016.
 */
public class LogMessage implements Comparable<LogMessage> {
    private XMLGregorianCalendar date;
    private String message;

    LogMessage(XMLGregorianCalendar date, String message) {
        this.date = date;
        this.message = message;
    }

    public LogMessage(String block) {
        this.date = parseDate(block);
        this.message = block;
    }

    private static XMLGregorianCalendar parseDate(String block) {
        String regex = "\\d\\d.\\d\\d.\\d\\d\\d\\d, \\d\\d?:\\d\\d:\\d\\d,\\d+ (PM|AM) (MSK)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(block);
        m.find();
        String stringDate =  m.group();
        //System.out.println("dateString " + stringDate);
        XMLGregorianCalendar xmlGregorianDate = new XMLGregorianCalendarImpl();
        String stringDateFormat = "dd.MM.yy, hh:mm:ss,SSS aa zzz"; // проверить h 11/12
        SimpleDateFormat format = new SimpleDateFormat(stringDateFormat);
        Date date = new Date();
        try {
            date = format.parse(stringDate);
        } catch (Exception e) {
            e.getStackTrace();
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        try {
            xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianDate;
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
