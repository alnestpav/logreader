package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.ws.SoapWebService;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;


/**
 * Created by alexander on 06.12.2016.
 */
public class Test {

    public static void main(String[] args) {
        String location = "webl_server1";
        String string = "javax";
        //SoapWebService soapWebService = new SoapWebService();
        List<LogMessage> logMessageList = null;
        try {
            logMessageList = getLogMessages(string, location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.sort(logMessageList);
        System.out.println(logMessageList);

    }

}
