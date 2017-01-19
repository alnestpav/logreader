package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.Response;
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.Request;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 14.12.2016.
 */
@Path("/restWebService")
public class RestWebService {
    private static final Logger logger = MyLogger.getLogger();

    @POST
    @Path("/getResponse")
    @Consumes(value = {"application/xml,application/json"})
    @Produces(value = {"application/xml,application/json"})
    public Response getListOfLogMessages(Request request) {
        logger.log(Level.INFO, "rest web webservice");
        try {
            logger.log(Level.INFO, "getting request: " + request);
            return request.getResponse();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Какая-то ошибка", e);
        }
        return null;
    }

    @GET
    @Path("/getFile")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getFile(String fileName) {
        File file = new File(fileName);
        javax.ws.rs.core.Response.ResponseBuilder response = javax.ws.rs.core.Response.ok((Object) file);
        response.header("Content-Disposition", "attachment; filename=" + fileName);
        return response.build();
    }
}

