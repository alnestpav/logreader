package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.exceptions.TooManyThreadException;
import ru.siblion.nesterov.logreader.type.Request;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "SoapWebService")
@Stateless
public class SoapWebService {
    private static final Logger logger = Logger.getLogger(Request.class.getName());

    @WebMethod(operationName = "getListOfLogMessages")
    public File getListOfLogMessages(@WebParam(name = "request") Request request) { // File или String лучше?
        try {
            return request.getResponse();
        } catch (TooManyThreadException e) {
            logger.log(Level.WARNING, "Количество процессов не может быть больше 10", e);
        }
        return null;
    }
}
