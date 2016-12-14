package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.ws.SoapWebService;

import javax.xml.datatype.XMLGregorianCalendar;
import java.beans.XMLDecoder;
import java.util.List;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {

    public static void main(String[] args) {
        String location = "webl_server2";
        String string = "javax";
        String dateFrom = "13.12.2016, 4:14:06,807 PM MSK";
        String dateTo = "13.12.2016, 4:14:06,870 PM MSK";
        List<LogMessage> logMessageList = null;
        SoapWebService soapWebService = new SoapWebService();
        try {
            logMessageList = soapWebService.getListOfLogMessages(string, location, dateFrom, dateTo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(logMessageList);
    }

}
