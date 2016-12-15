package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 13.12.2016.
 */
public class LogReader {

    private static Map<String, List<Integer>> getPositionsOfLinesWithString(String string, List<String> files) {

        StringBuilder filesString = new StringBuilder();
        for (String file : files) {
            filesString.append(file + " ");
        }

        Map<String, List<Integer>> linesWithStringNumbersInFile = new HashMap<>();
        String command = "findstr /n /r /c:" + "\"" + string + "\"" + " " + filesString;

        try {
            Process findstrProcess = Runtime.getRuntime().exec(command);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));
            String line = reader.readLine();

            Pattern fileNamePattern = Pattern.compile("\\w+.log\\d*");
            Matcher fileNameMatcher;

            Pattern lineNumberPattern = Pattern.compile(":\\d+");
            Matcher lineNumberMatcher;

            fileNameMatcher = fileNamePattern.matcher(line);
            fileNameMatcher.find();
            String currentFileName = fileNameMatcher.group();

            List<Integer> linesWithStringNumbers = new ArrayList<>();

            while (line != null) {
                fileNameMatcher = fileNamePattern.matcher(line);
                fileNameMatcher.find();
                String fileName = fileNameMatcher.group();
                if (!fileName.equals(currentFileName)) {
                    linesWithStringNumbersInFile.put(currentFileName, linesWithStringNumbers);
                    currentFileName = fileName;
                    linesWithStringNumbers.clear();
                }
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group().substring(1);
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesWithStringNumbersInFile;
    }

    private static Map<Integer, Integer> getBlockPositions(List<Integer> positionsOfLinesWithString,
                                                          List<Integer> prefixPositions) {
        Map<Integer, Integer> blockPositions = new TreeMap<>();
        int start;
        int end;
        for (int i = 0; i < positionsOfLinesWithString.size(); i++) {
            for (int j = 0; j < prefixPositions.size(); j++) {
                if (positionsOfLinesWithString.get(i) >= prefixPositions.get(j)
                        && j + 1 < prefixPositions.size()
                        && positionsOfLinesWithString.get(i) < prefixPositions.get(j + 1)) {

                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j + 1) - 1;
                    blockPositions.put(start, end);
                    break;
                }
                if (j + 1 == prefixPositions.size()) { // Если дошли до конца, значит искомая строка внутри последней строки-блока
                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j);
                    blockPositions.put(start, end);
                }
            }
        }
        return blockPositions;
    }

    private static String getBlock(String filePath, int fromLineNumber, int toLineNumber)  {
        System.out.println("getBlock(" + filePath + ", " + fromLineNumber + ", " + toLineNumber + ")");
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(filePath);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)){

            for (int i = 1; i < fromLineNumber; i++) {
                lineNumberReader.readLine();
            }
            for (int i = fromLineNumber; i <= toLineNumber; i++) {
                block.append(lineNumberReader.readLine());
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return block.toString();
    }

    public static List<LogMessage> getLogMessages(String string,
                                                  String location,
                                                  XMLGregorianCalendar dateFrom,
                                                  XMLGregorianCalendar dateTo) throws Exception {

        Map<String, List<Integer>> positionsOfLinesWithString;
        Map<String, List<Integer>> prefixPositions;

        Map<Integer, Integer> blockPositions;

        List<String> logFiles = FileSearcher.getLogFiles(location);
        List<LogMessage> logMessageList = new ArrayList<>();

        positionsOfLinesWithString = getPositionsOfLinesWithString(string, logFiles);
        prefixPositions = getPositionsOfLinesWithString("####", logFiles);

        blockPositions = getBlockPositions(positionsOfLinesWithString, prefixPositions);

        String currentBlock;
        for (Map.Entry<Integer, Integer> entry : blockPositions.entrySet()) {
            currentBlock = (getBlock(logFile, entry.getKey(), entry.getValue())).substring(4); // удаляем префикс ####
            LogMessage logMessage = new LogMessage(currentBlock);
            XMLGregorianCalendar logMessageDate = logMessage.getDate();
            if (dateFrom == null && dateTo == null) {
                System.out.println("add Log Message");
                logMessageList.add(logMessage);
                break;
            }
            if (dateFrom == null) {
                if (logMessageDate.compare(dateTo) <= 0) {
                    System.out.println("add Log Message");
                    logMessageList.add(logMessage);
                    break;
                }
            }
            if (dateTo == null) {
                if (logMessageDate.compare(dateFrom) >= 0) {
                    System.out.println("add Log Message");
                    logMessageList.add(logMessage);
                    break;
                }
            }
            if (logMessageDate.compare(dateFrom) >= 0 && logMessageDate.compare(dateTo) <= 0 ) {
                System.out.println("add Log Message");
                logMessageList.add(logMessage);
            }
            }
        return logMessageList;
    }

}
