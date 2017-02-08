package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.AppConfig;
import ru.siblion.nesterov.logreader.type.Response;
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.Request;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Properties;
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
    public Response getResponse(Request request) {
        logger.log(Level.INFO, "New request: " + request);
        try {
            return request.getResponse();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Get response error: ", e);
        }
        return null;
    }

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getFile(@PathParam("fileName") String fileName) {
        Properties appConfigProperties = AppConfig.getInstance().getProperties();
        String directory = appConfigProperties.getProperty("directory");
        File file = new File(directory + "\\" + fileName);
        javax.ws.rs.core.Response.ResponseBuilder response = null;
        if (file.exists()) {
            response = javax.ws.rs.core.Response.ok(file);
        }
        response.header("Content-Disposition", "attachment; filename=" + fileName);
        return response.build();
    }

}

