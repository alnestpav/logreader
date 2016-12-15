package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.core.Request;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import java.util.List;


/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "SoapWebService")
@Stateless
public class SoapWebService {

    @WebMethod(operationName = "getListOfLogMessages")
    public List<LogMessage> getListOfLogMessages(@WebParam(name = "string") String string,
                                                 @WebParam(name = "location") String location,
                                                 @WebParam(name = "dateFrom") String dateFrom,
                                                 @WebParam(name = "dateTo") String dateTo) {
        Request request = new Request(string, location, dateFrom, dateTo);
        return request.getListOfLogMessages();
    }
}
