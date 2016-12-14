package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 13.12.2016.
 */
public class LogReader {

    private static List<Integer> getPositionsOfLinesWithString(String string, String filePath) {
        List<Integer> linesWithStringNumbers = new ArrayList<>();

        String command = "findstr /n /r /c:" + "\"" + string + "\"" + " " + filePath ;
        try {
            Process findstrProcess = Runtime.getRuntime().exec(command);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));
            String line = reader.readLine();
            Pattern lineNumberPattern = Pattern.compile("\\d+");
            Matcher lineNumberMatcher;
            while (line != null) {
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group();
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesWithStringNumbers;
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

        List<Integer> positionsOfLinesWithString;
        List<Integer> prefixPositions;
        Map<Integer, Integer> blockPositions;

        List<String> logFiles = FileSearcher.getLogFiles(location);
        List<LogMessage> logMessageList = new ArrayList<>();

        for (String logFile : logFiles) {
            positionsOfLinesWithString = getPositionsOfLinesWithString(string, logFile);
            prefixPositions = getPositionsOfLinesWithString("####", logFile);

            blockPositions = getBlockPositions(positionsOfLinesWithString, prefixPositions);

            String currentBlock;
            for (Map.Entry<Integer, Integer> entry : blockPositions.entrySet()) {
                currentBlock = (getBlock(logFile, entry.getKey(), entry.getValue())).substring(4); // удаляем префикс ####
                LogMessage logMessage = new LogMessage(currentBlock);
                XMLGregorianCalendar logMessageDate = logMessage.getDate();
                if (logMessageDate.compare(dateFrom) >= 0 && logMessageDate.compare(dateTo) <= 0 ) {
                    logMessageList.add(logMessage);
                }
            }
        }
        return logMessageList;
    }

}
