package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.ws.SoapWebService;

import java.util.List;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {

    public static void main(String[] args) {
        String location = "webl_server1";
        String string = "4:14.[0-9][0-9],[0-9][0-9][0-9]";
        List<LogMessage> logMessageList = null;
        SoapWebService soapWebService = new SoapWebService();
        try {
            logMessageList = soapWebService.getListOfLogMessages(string, location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(logMessageList);
    }

}
