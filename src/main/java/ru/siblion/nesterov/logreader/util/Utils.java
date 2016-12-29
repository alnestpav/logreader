package ru.siblion.nesterov.logreader.util;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 06.12.2016.
 */
public class Utils {

    private static final Logger logger = MyLogger.getLogger();

    private final static String XML_GREGORIAN_CALENDAR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // проверить H или k
    private final static String STRING_DATE_FORMAT = "dd.MM.yyyy, hh:mm:ss,SSS aa zzz"; // проверить h 11/12

    public static XMLGregorianCalendar stringToXMLGregorianCalendar(String stringDate) {
        XMLGregorianCalendar xmlGregorianDate = new XMLGregorianCalendarImpl();
        SimpleDateFormat format = new SimpleDateFormat(STRING_DATE_FORMAT);

        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при парсинге даты из строки", e) ;
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        System.out.println(date);
        System.out.println(stringDate);
        gregorianCalendar.setTime(date);
        try {
            xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            logger.log(Level.SEVERE, "Ошибка при получения экземпляра XMLGregorianCalendar", e) ;
        }
        return xmlGregorianDate;
    }

}