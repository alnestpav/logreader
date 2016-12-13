package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.ws.Logreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        String location = "webl_domain";
        String string = "javax";
        Logreader logreader = new Logreader();
        String res = logreader.getLogMessageListString(string, location);
        System.out.println(res);
    }

    /*public static List<Integer> getStringPositionsOldVersion(String string, String filePath) {
        List<Integer> numbers = new ArrayList<Integer>();
        try {
            BufferedReader reader;
            String command = "findstr /n /c:" + "\"" + string + "\"" + " " + filePath ;
            Process p = Runtime.getRuntime().exec(command);

            InputStream is = p.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                int i = 0;
                while (i < line.toCharArray().length && line.toCharArray()[i] != ':') i++;
                numbers.add(Integer.parseInt((line.substring(0, i))));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numbers;
    }*/

}
