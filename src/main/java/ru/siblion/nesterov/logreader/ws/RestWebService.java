package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.ws.rs.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 14.12.2016.
 */
@Path("/hello")
public class RestWebService {
    @GET
    @Path("{string}/{location}/{dateFrom}/{dateTo}")
    @Consumes(value={"text/xml", "application/json"})
    @Produces(value={"text/xml", "application/json"})
    public List<LogMessage> getListOfLogMessages(@PathParam("string") String string,
                                                 @PathParam("location") String location,
                                                 @PathParam("dateFrom") String dateFrom,
                                                 @PathParam("dateTo") String dateTo) {
        List<LogMessage> logMessageList = null;
        try {
            //logMessageList = getLogMessages(string, location, dateFrom, dateTo);
            Collections.sort(logMessageList);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка: неверный атрибут location");
            //e.printStackTrace();
        }
        return logMessageList;
    }

    /*@GET
    @Produces("text/plain")
    public String getHelloText(
            @QueryParam("name")
            @DefaultValue("World")
                    String name) {
        return "Hello, " + name + "!";
    }*/

}

