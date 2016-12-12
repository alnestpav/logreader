package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;
import sun.rmi.runtime.Log;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander nesterov on 05.12.2016.
 */
@WebService(name = "Logreader")
@Stateless
public class Logreader {

    @WebMethod(operationName = "getLogMessageListString")
    public String getLogMessageListString(@WebParam(name = "string") String string, @WebParam(name = "location") String location) {
        List<LogMessage> logMessageList = Methods.getLogMessageList(string, location);
        StringBuilder allMessagesStringBuilder = new StringBuilder();
        for (LogMessage logMessage : logMessageList) {
            allMessagesStringBuilder.append(logMessage.getMessage());
        }
        return allMessagesStringBuilder.toString();
    }



}
