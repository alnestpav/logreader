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
        List<LogMessage> logMessageList = getLogMessageList(string, location);
        Collections.sort(logMessageList);
        for (LogMessage logMessage : logMessageList) {
            System.out.println("__________________");
            System.out.println(logMessage.getDate());
            System.out.println(logMessage.getMessage());
            System.out.println("__________________");
        }
    }

    public static void numberLineTest(int number) {
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            fr = new FileReader("C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\ws_server1.log02354");
            lnr = new LineNumberReader(fr);
            for (int i = 0; i < number - 1; i++) {
                lnr.readLine();
            }
            System.out.println(lnr.readLine());
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            // closes the stream and releases system resources
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
    }

    public static List<LogMessage> getLogMessageList(String string, String location) {

        List<Integer> regExpPositions;
        List<Integer> blockPositions;
        StringBuilder block = new StringBuilder();
        Map<Integer, Integer> regExpBlocksPositions;

        List<String> logFilePaths = Methods.getLogFilePaths(location);
        List<LogMessage> logMessageList = new ArrayList<LogMessage>();
        for (String logFilePath : logFilePaths) {
            regExpPositions = Methods.getExpressionPositions(string, logFilePath);
            blockPositions = Methods.getExpressionPositions("####", logFilePath);
            regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
            /*System.out.println(regExpBlocksPositions);
            System.out.println(blockPositions);*/
            String currentBlock;

            for (Map.Entry<Integer, Integer> entry : regExpBlocksPositions.entrySet()) {
                currentBlock = (Methods.getBlock(logFilePath, entry.getKey(), entry.getValue())).substring(4);
                /*System.out.println("CURRENT_BLOCK{{{" + currentBlock + "}}}");*/
                LogMessage logMessage = new LogMessage(currentBlock);
                logMessageList.add(logMessage);
                /*System.out.println("DATE ");
                System.out.println(logMessage.getDate());*/
                block.append(currentBlock);
            }
        }
        return logMessageList;
    }

    public static void testReg() {
        String date = "<07.12.2016, 4:46:25,363 PM MSK> <Notice> <Log Management>";
        String regex = "\\d\\d.\\d\\d.\\d\\d\\d\\d, \\d:\\d\\d:\\d\\d,\\d\\d\\d (PM|AM) (MSK)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(date);
        m.find();
        System.out.println("m.group() " + m.group());
    }

}
