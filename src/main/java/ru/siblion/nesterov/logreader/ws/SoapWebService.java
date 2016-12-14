package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.util.*;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "SoapWebService")
@Stateless
public class SoapWebService {

    @WebMethod(operationName = "getListOfLogMessages")
    public List<LogMessage> getListOfLogMessages(@WebParam(name = "string") String string,
                                                 @WebParam(name = "location") String location,
                                                 @WebParam(name = "dateFrom") String dateFrom,
                                                 @WebParam(name = "dateTo") String dateTo) {
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
