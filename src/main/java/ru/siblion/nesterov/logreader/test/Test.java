package ru.siblion.nesterov.logreader.test;


import ru.siblion.nesterov.logreader.core.FileFormat;
import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.type.Request;
import ru.siblion.nesterov.logreader.type.DateInterval;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;
import ru.siblion.nesterov.logreader.ws.SoapWebService;


import javax.xml.datatype.XMLGregorianCalendar;

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
        SoapWebService soapWebService = new SoapWebService();
        List<DateInterval> dateIntervals = new ArrayList<>();
        dateIntervals.add(new  DateInterval(dateFrom, dateTo));
        FileFormat fileFormat = FileFormat.rtf;
        Request request = Request.getNewRequest(string, location, dateIntervals, fileFormat);
        String filePath = null;
        try {
            filePath = soapWebService.getListOfLogMessages(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(filePath);

        //ObjectToFileWriter.write(logMessageList, FileFormat.xml);
        /*LogMessages logMessages = new LogMessages();
        logMessages.setLogMessages(logMessageList);
        ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter(logMessageList);
        objectToFileWriter.write(FileFormat.pdf);
        objectToFileWriter.write(FileFormat.rtf);
        objectToFileWriter.write(FileFormat.log);
        objectToFileWriter.write(FileFormat.doc);
        objectToFileWriter.write(FileFormat.html);*/


    }

}