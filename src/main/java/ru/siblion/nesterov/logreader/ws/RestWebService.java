package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.Config;
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

    //private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test
    private static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private static Config config = Config.getConfig(configFile);

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
    @Path("{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public javax.ws.rs.core.Response getFile(@PathParam("fileName") String fileName) {
        config = Config.getConfig(configFile);
        String directory = config.getDirectory().toString();
        System.out.println("dir + filename " + directory + fileName);
        File file = new File(directory + fileName);
        javax.ws.rs.core.Response.ResponseBuilder response = null;
        if (file.exists()) {
            response = javax.ws.rs.core.Response.ok((Object) file);
        }
        response.header("Content-Disposition", "attachment; filename=" + fileName);
        return response.build();
    }
}

