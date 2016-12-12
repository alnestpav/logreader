package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        String location = "webl_server1";
        String string = "weblogic.wsee.jaxws.HttpServletAdapter.post(HttpServletAdapter.java:295)";
        List<LogMessage> logMessageList = Methods.getLogMessageList(string, location);
        Collections.sort(logMessageList);
        /*for (LogMessage logMessage : logMessageList) {
            System.out.println("__________________");
            System.out.println(logMessage.getDate());
            System.out.println(logMessage.getMessage());
            System.out.println("__________________");
        }*/

        StringBuilder allMessagesString = new StringBuilder();
        for (LogMessage logMessage : logMessageList) {
            allMessagesString.append(logMessage.getMessage());
        }
    }

}
