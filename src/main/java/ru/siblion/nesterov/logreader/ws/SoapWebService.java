package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.Request;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import java.io.File;
import java.util.List;


/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "SoapWebService")
@Stateless
public class SoapWebService {

    @WebMethod(operationName = "getListOfLogMessages")
    public List<LogMessage> getListOfLogMessages(@WebParam(name = "request") Request request) {
        return request.getListOfLogMessages();
    }

    @WebMethod(operationName = "getWorkingDirectory")
    public String getWorkingDirectory() {
        String currentDirectory;
        File file = new File("");
        currentDirectory = file.getAbsolutePath();
        return currentDirectory;
    }
}
