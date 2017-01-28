package ru.siblion.nesterov.logreader.util;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ru.siblion.nesterov.logreader.type.DateInterval;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by alexander on 06.12.2016.
 */

/* Класс, предоставляющий вспомогательные методы для других классов */
public class Utils {

    private static final Logger logger = MyLogger.getLogger();

    private final static String XML_GREGORIAN_CALENDAR_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // проверить H или k
    private final static String STRING_DATE_FORMAT = "dd.MM.yyyy, hh:mm:ss,SSS aa zzz"; // проверить h 11/12


    /* Метод используется при тестировании проекта в классе Test, когда в запрос добавляется интервал дат */
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

    /* Метод раньше был в классе FileSearcher, но перенес в класс Utils, так как он также используется еще и в классе Request */
    public static List<String> getFilesMatching(File root, String regExp) {
        try {
            if (!root.isDirectory()) {
                throw new IllegalArgumentException(root + " это не директория.");
            }
        } catch(IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Ошибка при поиске файлов в некоторой папке", e) ;
        }
        final Pattern p = Pattern.compile(regExp);

        File[] files = root.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return p.matcher(file.getName()).matches();
            }
        });

        List<String> filesMatching = new ArrayList<>();
        for (File file : files) {
            filesMatching.add(file.toString());
        }
        return filesMatching;
    }

}