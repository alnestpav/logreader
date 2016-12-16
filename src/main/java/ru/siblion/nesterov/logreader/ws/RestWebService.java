package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.Request;
import ru.siblion.nesterov.logreader.type.DateInterval;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 14.12.2016.
 */
@Path("/restWebService")
public class RestWebService {

    /*@GET
    @Path("{name}")
    @Produces("text/plain")
    public String getHelloText(@PathParam("name") String name) {
        return "Hello, " + name + "!";
    }*/
    /*@POST
    @Path("{string}/{location}/{dateFrom}/{dateTo}")
    @Consumes(value={"text/xml", "application/json"})
    //@Produces(value={"text/xml", "application/json"})
    @Produces(value={"application/json"})
    public List<LogMessage> getListOfLogMessages(@PathParam("string") String string,
                                                 @PathParam("location") String location,
                                                 @PathParam("dateFrom") String dateFrom,
                                                 @PathParam("dateTo") String dateTo) {
        return Request.getListOfLogMessages(string, location, dateFrom, dateTo);

    }*/

    @POST
    //@Path("{string}/{location}/{dateFrom}/{dateTo}")
    //@Consumes(value={"text/xml", "application/json"})
    @Consumes(value={"application/json"})
    //@Produces(value={"text/xml", "application/json"})
    @Produces(value={"application/json"})
    public List<LogMessage> getListOfLogMessages(Request request) {
        return request.getListOfLogMessages();
    }


    @GET
    //@Path("{string}/{location}/{dateFrom}/{dateTo}")
    //@Consumes(value={"text/xml", "application/json"})
    //@Consumes({MediaType.APPLICATION_JSON})
    //@Produces(value={"text/xml", "application/json"})
    //@Produces({MediaType.APPLICATION_JSON})
    //@Produces(value={"application/json"})
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
        Request request = new Request(string, location, dateIntervals);
        return request;
    }

}

