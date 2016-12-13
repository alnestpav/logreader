package ru.siblion.nesterov.logreader.test;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;
import ru.siblion.nesterov.logreader.ws.Logreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    }

    public static void testPowerShell(String string, String filePath) {
        //List<Integer> numbers = new ArrayList<Integer>();

        PowerShellResponse response = PowerShell.executeSingleCommand("echo \"adsfadsf\"");

        //Print results
        System.out.println("List Processes:" + response.getCommandOutput());
        /*try {
            BufferedReader reader;
            String command1 = "powershell.exe";
            String command2 = "dir webl_AdminServer.log00013 | select-string -Pattern 'javax' | select LineNumber";
            Process powerShellProcess = Runtime.getRuntime().exec(command1);
            powerShellProcess.getOutputStream().close();

            InputStream is = powerShellProcess.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                numbers.add(Integer.parseInt(line));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //return numbers;
    }

}
