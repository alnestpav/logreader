package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.util.MyLogger;
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
    private static final Logger logger = MyLogger.getLogger();

    @WebMethod(operationName = "getListOfLogMessages")
    public File getListOfLogMessages(@WebParam(name = "request") Request request) { // File или String лучше?
        logger.log(Level.INFO, "soap web webservice");
        try {
            request.configure();
            return request.getResponse();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Какая-то ошибка", e);
        }
        return null;
    }
}
