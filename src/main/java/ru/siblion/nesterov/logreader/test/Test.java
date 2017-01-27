package ru.siblion.nesterov.logreader.test;


import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.type.*;
import ru.siblion.nesterov.logreader.util.Utils;
import ru.siblion.nesterov.logreader.ws.RestWebService;
import ru.siblion.nesterov.logreader.ws.SoapWebService;


import javax.xml.datatype.XMLGregorianCalendar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 06.12.2016.
 */

/* Класс для тестирования проекта без развертывания приложения на сервере */
public class Test {

    public static void main(String[] args) {
        String location = "webl_server1";
        LocationType locationType  = LocationType.server;
        String string =  "java";
/*        String dateFromString = "2016-12-14T15:48:28.432+03:00";
        String dateToString = "2016-12-14T15:48:31.734+03:00";*/
        String dateFromString = "14.12.2016, 10:47:53,548 AM MSK";
        String dateToString = "13.12.2016, 4:14:06,807 PM MSK";
        /*XMLGregorianCalendar dateFrom = null;
        XMLGregorianCalendar dateTo = Utils.stringToXMLGregorianCalendar(dateToString);*/

        XMLGregorianCalendar dateFrom = null;
        XMLGregorianCalendar dateTo = null;
        SoapWebService soapWebService = new SoapWebService();
        RestWebService restWebService = new RestWebService();
        List<DateInterval> dateIntervals = new ArrayList<>();
        dateIntervals.add(new  DateInterval(dateFrom, dateTo));
        FileFormat[] fileFormats = { FileFormat.pdf };
        for (FileFormat fileFormat : fileFormats) {
            Request request = Request.getNewRequest(string, locationType, location, dateIntervals, fileFormat);
            Response response = null;
            try {
                response = soapWebService.getListOfLogMessages(request);
                //filePath = restWebService.getListOfLogMessages(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(response.getLogMessages());
            System.out.println(response.getOutputFile());
            /*FileRemover fileRemover = new FileRemover();
            fileRemover.removeOldFiles();*/
            ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter(response);
            objectToFileWriter.write(FileFormat.xml, new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\response.xml"));
        }

    }


}