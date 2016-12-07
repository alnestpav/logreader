package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.util.Methods;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        entry();

        /*String logFilePath = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_AdminServer\\logs\\webl_domain4.log";
        List<Integer> blockPositions = Methods.getExpressionPositions("####", logFilePath);
        System.out.println(blockPositions);
        List<Integer> regExpPositions = Methods.getExpressionPositions("at weblogic.deploy.internal.targetserver.operations.RedeployOperation.createAndPrepareContainer(RedeployOperation.java:104)", logFilePath);
        System.out.println(regExpPositions);
        Map<Integer, Integer> regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
        System.out.println(regExpBlocksPositions);*/
        //System.out.println(Methods.getDate("05.12.2016, 4:23:36,322 PM"));

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

    public static String entry() {
        String location = "webl_domain";
        String string = "weblogic.wsee.jaxws.HttpServletAdapter.post(HttpServletAdapter.java:295)";
        String filePath;

        String myServerLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\webl_server1.log";
        String realServerLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\ws_server1.log02354";
        String testLogFile = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\test.log";
        String domainsDirectory = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains";

        //filePath = testLogFile;
        List<Integer> regExpPositions;
        List<Integer> blockPositions;
        StringBuilder block = new StringBuilder();
        Map<Integer, Integer> regExpBlocksPositions;

        List<String> logFilePaths = Methods.getLogFilePaths(location);
        System.out.println("LOGFILEPATHS");
        System.out.println(logFilePaths);

        for (String logFilePath : logFilePaths) {
            regExpPositions = Methods.getExpressionPositions(string, logFilePath);
            blockPositions = Methods.getExpressionPositions("####", logFilePath);
            regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
            System.out.println(regExpBlocksPositions);
            System.out.println(blockPositions);
            String currentBlock;
            for (Map.Entry<Integer, Integer> entry : regExpBlocksPositions.entrySet()) {
                currentBlock = (Methods.getBlock(logFilePath, entry.getKey(), entry.getValue())).substring(4);
                System.out.println("CURRENT_BLOCK{{{" + currentBlock + "}}}");
                LogMessage logMessage = new LogMessage(currentBlock);
                System.out.println("DATE ");
                System.out.println(logMessage.getDate());
                block.append(currentBlock);
            }

        }
        return block.toString();
    }

}
