package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.exceptions.TooManyProcessException;
import ru.siblion.nesterov.logreader.type.Request;

import javax.ws.rs.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 14.12.2016.
 */
@Path("/restWebService")
public class RestWebService {
    private static final Logger logger = Logger.getLogger(Request.class.getName());

    @POST
    @Consumes(value = {"application/xml,application/json"})
    @Produces(value = {"application/xml,application/json"})
    public File getListOfLogMessages(Request request) {
        try {
            return request.getResponse();
        } catch (TooManyProcessException e) {
            logger.log(Level.WARNING, "Количество процессов не может быть больше 10", e);
        }
        return null;
    }
}

