package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.util.Methods;

import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by alexander on 06.12.2016.
 */
public class Test {
    public static void main(String[] args) {
        String filePath = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\ws_server1.log02354";
        entry();

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

    public static void entry() {
        String filePath = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\servers\\webl_server1\\logs\\test.log";
        List<Integer> regExpPositions = Methods.getExpressionPositions("баланс", filePath);
        List<Integer> blockPositions = Methods.getExpressionPositions("####", filePath);
        System.out.println(regExpPositions);
        System.out.println(blockPositions);
        Map<Integer, Integer> regExpBlocksPositions = Methods.getRegExpBlocksPositions(regExpPositions, blockPositions);
        System.out.println(regExpBlocksPositions);

        for (Map.Entry<Integer, Integer> entry : regExpBlocksPositions.entrySet()) {
            System.out.println("ENTRY");
            System.out.println(Methods._getBlock(filePath, entry.getKey(), entry.getValue()));
            //break;
        }

    }

}
