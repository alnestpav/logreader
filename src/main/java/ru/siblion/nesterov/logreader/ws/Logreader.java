package ru.siblion.nesterov.logreader.ws;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Collections;
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
        Collections.sort(logMessageList);

        StringBuilder allMessagesStringBuilder = new StringBuilder();
        for (LogMessage logMessage : logMessageList) {
            allMessagesStringBuilder.append(logMessage.getMessage() + "\n");
        }
        return allMessagesStringBuilder.toString();
    }

    @WebMethod(operationName = "getDomain")
    public String getDomain(@WebParam(name = "string") String string, @WebParam(name = "location") String location) {
        //List<LogMessage> logMessageList = Methods.getLogMessageList(string, location);
        List<LogMessage> logMessageList = Methods.getLogMessageList("java", "webl_domain");
        StringBuilder allMessagesStringBuilder = new StringBuilder();
        for (LogMessage logMessage : logMessageList) {
            allMessagesStringBuilder.append(logMessage.getMessage() + "\n");
        }
        return allMessagesStringBuilder.toString();
}
}
