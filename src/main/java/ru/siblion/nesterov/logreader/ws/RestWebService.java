package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.RequestController;
import ru.siblion.nesterov.logreader.util.AppConfig;
import ru.siblion.nesterov.logreader.type.Response;
import ru.siblion.nesterov.logreader.util.AppLogger;
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
    public final static Properties appConfigProperties = AppConfig.getProperties();
    private static final Logger logger = AppLogger.getLogger();

    @POST
    @Path("/getResponse")
    @Consumes(value = {"application/xml,application/json"})
    @Produces(value = {"application/xml,application/json"})
    public Response getResponse(Request request) {
        logger.log(Level.INFO, "New request: " + request);
        try {
            RequestController requestController = new RequestController(request);
            return requestController.getResponse();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Get response error: ", e);
        }
        return null;
    }

    @GET
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getFile(@PathParam("fileName") String fileName) {
        String directory = appConfigProperties.getProperty("directory");
        File file = new File(directory + "\\" + fileName);
        javax.ws.rs.core.Response.ResponseBuilder response = null;
        if (file.exists()) {
            response = javax.ws.rs.core.Response.ok(file);
        }
        return response.build();
    }

}

