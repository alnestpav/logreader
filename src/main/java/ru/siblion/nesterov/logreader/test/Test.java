package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;
import ru.siblion.nesterov.logreader.ws.Logreader;

import java.io.*;
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
        List<LogMessage> logMessageList = Methods.getLogMessageList(string, location);
        System.out.println(logMessageList);

    }

    public static List<Integer> getStringPositionsOldVersion(String string, String filePath) {
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
    }

    public static String getBlockOldVersion(String filePath, int fromLineNumber, int toLineNumber)  {
        System.out.println("getBlock(" + filePath + ", " + fromLineNumber + ", " + toLineNumber + ")");
        StringBuilder block = new StringBuilder();
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            fr = new FileReader(filePath);
            lnr = new LineNumberReader(fr);
            for (int i = 1; i < fromLineNumber; i++) {
                lnr.readLine();
            }
            for (int i = fromLineNumber; i <= toLineNumber; i++) {
                block.append(lnr.readLine());
            }
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            if(fr!=null) {
                try {
                    fr.close();
                } catch (IOException e) {

                }
            }
            if(lnr!=null) {
                try {
                    lnr.close();
                } catch(IOException e) {

                }
            }
        }
        return block.toString();
    }

}
