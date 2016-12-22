package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.FileFormat;
import ru.siblion.nesterov.logreader.test.MyRunnable;
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
    public String getListOfLogMessages(@WebParam(name = "request") Request request) {
        Runnable myRunnable = new MyRunnable(request);
        Thread t = new Thread(myRunnable);
        t.start();
        return request.getFilePath();
    }

    @WebMethod(operationName = "getWorkingDirectory")
    public FileFormat getWorkingDirectory() {
        String currentDirectory;
        File file = new File("");
        currentDirectory = file.getAbsolutePath();
        return FileFormat.pdf;
    }
}
