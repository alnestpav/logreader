package ru.siblion.nesterov.logreader.test;

import org.apache.fop.apps.FOPException;
import ru.siblion.nesterov.logreader.core.FileFormat;
import ru.siblion.nesterov.logreader.core.LogFileWriter;
import ru.siblion.nesterov.logreader.core.Request;
import ru.siblion.nesterov.logreader.type.DateInterval;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.ws.RestWebService;
import ru.siblion.nesterov.logreader.ws.SoapWebService;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {

    public static void main(String[] args) {
        String location = "webl_domain";
        String string = "javax";
/*        String dateFromString = "2016-12-14T15:48:28.432+03:00";
        String dateToString = "2016-12-14T15:48:31.734+03:00";*/
        String dateFromString = "15.12.2016, 10:47:53,548 AM MSK";
        String dateToString = "15.12.2016, 10:48:00,477 AM MSK";
/*        XMLGregorianCalendar dateFrom = Utils.stringToXMLGregorianCalendar(dateFromString);
        XMLGregorianCalendar dateTo = Utils.stringToXMLGregorianCalendar(dateToString);*/

        XMLGregorianCalendar dateFrom = null;
        XMLGregorianCalendar dateTo = null;
        List<LogMessage> logMessageList = null;
        SoapWebService soapWebService = new SoapWebService();
        List<DateInterval> dateIntervals = new ArrayList<>();
        dateIntervals.add(new  DateInterval(dateFrom, dateTo));
        Request request = Request.getNewRequest(string, location, dateIntervals);
        try {
            logMessageList = soapWebService.getListOfLogMessages(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(logMessageList);

        LogFileWriter.write(logMessageList, FileFormat.xml);
        LogFileWriter.write(logMessageList, FileFormat.pdf);
        LogFileWriter.write(logMessageList, FileFormat.rtf);
        LogFileWriter.write(logMessageList, FileFormat.txt);
        LogFileWriter.write(logMessageList, FileFormat.log);



    }

}