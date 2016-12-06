package ru.siblion.nesterov.logreader.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by alexander on 06.12.2016.
 */
public class Methods {

    public static List<Integer> getExpressionPositions(String string, String filePath) {
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

    public static String getBlock(String filePath, int fromLine, int toLine) {
        System.out.println("getBlock(" + filePath + ", " + fromLine + ", " + toLine + ")");
        StringBuilder block = new StringBuilder();
        for (int i = fromLine; i <= toLine; i++) {
            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                block.append(lines.skip(i - 1).findFirst().get() + "\n");
            }  catch (IOException e) {
                e.getStackTrace();
            }
        }
        return block.toString();
    }

    public static String _getBlock(String filePath, int fromLine, int toLine)  {
        System.out.println("getBlock(" + filePath + ", " + fromLine + ", " + toLine + ")");
        StringBuilder block = new StringBuilder();
        FileReader fr = null;
        LineNumberReader lnr = null;
        try {
            fr = new FileReader(filePath);
            lnr = new LineNumberReader(fr);
            for (int i = 1; i < fromLine; i++) {
                lnr.readLine();
            }
            for (int i = fromLine; i <= toLine; i++) {
                block.append(lnr.readLine()+ "\n");
            }
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
        return block.toString();
    }

    public static Map<Integer, Integer> getRegExpBlocksPositions(List<Integer> regExpPositions, List<Integer> blockPositions) {
        Map<Integer, Integer> regExpBlocksPositions = new TreeMap<Integer, Integer>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < regExpPositions.size(); i++) {
            for (int j = 0; j <blockPositions.size(); j++) {
                if (regExpPositions.get(i) >= blockPositions.get(j) && j + 1 < blockPositions.size() && regExpPositions.get(i) < blockPositions.get(j + 1)) {
                    start =  blockPositions.get(j);
                    end =  blockPositions.get(j + 1) - 1;
                    regExpBlocksPositions.put(start, end);
                    break;
                }
                if (j + 1 == blockPositions.size()) {
                    start =  blockPositions.get(j);
                    end =  blockPositions.get(j);
                    regExpBlocksPositions.put(start, end);
                }
            }
        }
        return regExpBlocksPositions;
    }

    public static List<String> getFilePaths(String location) {
        List<String> filePaths = new ArrayList<String>();
        //File some = new File();



        return filePaths;
    }
}
