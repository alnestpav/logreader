package ru.siblion.nesterov.logreader.util;


import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by alexander on 06.12.2016.
 */
public class Utils {

    public static XMLGregorianCalendar stringToXMLGregorianCalendar(String stringDate, String stringDateFormat) {
        XMLGregorianCalendar xmlGregorianDate = new XMLGregorianCalendarImpl();
        SimpleDateFormat format = new SimpleDateFormat(stringDateFormat);

        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (Exception e) {
            e.getStackTrace();
        }
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        try {
            xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlGregorianDate;
    }
}