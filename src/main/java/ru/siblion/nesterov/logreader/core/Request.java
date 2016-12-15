package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.jws.WebParam;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 15.12.2016.
 */
public class Request {
    public static List<LogMessage> getListOfLogMessages(String string, String location, String dateFrom, String dateTo) {
        List<LogMessage> logMessageList = null;
        String stringDateFormat = "dd.MM.yy, hh:mm:ss,SSS aa zzz"; // проверить h или K
        String xmlGregorianCalendarDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"; // проверить H или k
        XMLGregorianCalendar xmlGregorianCalendarFrom = Utils.stringToXMLGregorianCalendar(dateFrom, xmlGregorianCalendarDateFormat);
        XMLGregorianCalendar xmlGregorianCalendarTo = Utils.stringToXMLGregorianCalendar(dateTo, xmlGregorianCalendarDateFormat);
        try {
            logMessageList = getLogMessages(string, location, xmlGregorianCalendarFrom, xmlGregorianCalendarTo);
            Collections.sort(logMessageList);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка: неверный атрибут location");
            //e.printStackTrace();
        }
        return logMessageList;
    }

}
