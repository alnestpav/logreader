package ru.siblion.nesterov.logreader.core;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by alexander on 08.02.2017.
 */
public class Test {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\logreader\\appConfig.xml");
            FileInputStream fileInput = new FileInputStream(file);
            Properties properties = new Properties();
            properties.loadFromXML(fileInput);
            System.out.println(properties.get("directory"));
            fileInput.close();

            Enumeration enuKeys = properties.keys();
            while (enuKeys.hasMoreElements()) {
                String key = (String) enuKeys.nextElement();
                String value = properties.getProperty(key);
                System.out.println(key + ": " + value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
