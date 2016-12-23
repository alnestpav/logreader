package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.FileFormat;
import ru.siblion.nesterov.logreader.type.Request;
import ru.siblion.nesterov.logreader.type.DateInterval;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.ws.rs.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 14.12.2016.
 */
@Path("/restWebService")
public class RestWebService {

    @POST
    @Consumes(value={"application/xml,application/json"})
    @Produces(value={"application/xml,application/json"})
    public File getListOfLogMessages(Request request) {
        return request.getResponse();
    }

    @GET
    @Produces(value={"text/xml"})
    public Request test() {
        String location = "webl_domain";
        String string = "javax";
        String dateFromString = "15.12.2016, 10:47:53,548 AM MSK";
        String dateToString = "15.12.2016, 10:48:00,477 AM MSK";
        XMLGregorianCalendar dateFrom = Utils.stringToXMLGregorianCalendar(dateFromString);
        XMLGregorianCalendar dateTo = Utils.stringToXMLGregorianCalendar(dateToString);
        List<DateInterval> dateIntervals = new ArrayList<>();
        dateIntervals.add(new  DateInterval(dateFrom, dateTo));
        Request request = Request.getNewRequest(string, location, dateIntervals, FileFormat.doc);
        return request;
    }

}

