package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.Request;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
    @Consumes(MediaType.APPLICATION_JSON)
    //@Produces(value={"text/xml", "application/json"})
    @Produces(value={MediaType.APPLICATION_XML})
    public List<LogMessage> getListOfLogMessages(Request request) {
        return request.getListOfLogMessages();

    }

}

