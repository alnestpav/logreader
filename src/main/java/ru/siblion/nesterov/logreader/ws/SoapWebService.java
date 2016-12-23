package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.Request;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import java.io.File;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "SoapWebService")
@Stateless
public class SoapWebService {

    @WebMethod(operationName = "getListOfLogMessages")
    public File getListOfLogMessages(@WebParam(name = "request") Request request) { // File или String лучше?
        return request.getResponse();
    }

    /*@WebMethod(operationName = "getWorkingDirectory")
    public FileFormat getWorkingDirectory() {
        String currentDirectory;
        File file = new File("");
        currentDirectory = file.getAbsolutePath();
        return FileFormat.pdf;
    }*/
}
