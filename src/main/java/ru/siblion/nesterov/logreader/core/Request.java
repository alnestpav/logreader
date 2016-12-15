package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 15.12.2016.
 */
@XmlRootElement(name = "Request")
public class Request {
    //@FormParam("string")
    private String string;

    //@FormParam("location")
    private String location;

    //@FormParam("dateFrom")
    private String dateFrom;

    //@FormParam("dateTo")
    private String dateTo;

    public Request(String string, String location, String dateFrom, String dateTo) {
        this.string = string;
        this.location = location;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public List<LogMessage> getListOfLogMessages() {
        List<LogMessage> logMessageList = null;
        //String stringDateFormat = "dd.MM.yy, hh:mm:ss,SSS aa zzz"; // проверить h или K
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
