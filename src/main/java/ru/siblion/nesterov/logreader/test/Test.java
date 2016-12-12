package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;
import ru.siblion.nesterov.logreader.ws.Logreader;

import java.util.*;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        String location = "webl_domain";
        String string = "at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)";
        Logreader logreader = new Logreader();
        String res = logreader.getLogMessageListString(string, location);
        System.out.println(res);

        System.out.println(logreader.getDomain("java", "loc"));
    }

}
