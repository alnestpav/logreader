package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogFile;
import ru.siblion.nesterov.logreader.type.LogMessage;
import sun.rmi.runtime.Log;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 13.12.2016.
 */
public class LogReader {

    private static List<LogFile> getPositionsOfLinesWithString(String string, List<LogFile> logFiles) {

        StringBuilder filesString = new StringBuilder();
        for (LogFile logFile: logFiles) {
            filesString.append(logFile.getFilePath() + " ");
        }
        String command = "findstr /n /r /c:" + "\"" + string + "\"" + " " + filesString;

        try {
            Process findstrProcess = Runtime.getRuntime().exec(command);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));
            String line = reader.readLine();

            Pattern fileNamePattern = Pattern.compile("[^.]+\\.log\\d*");
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
                System.out.println("fileName " + fileName);
                if (!fileName.equals(currentFileName)) { // проверить последний случай!
                    for (LogFile logFile : logFiles) {
                        if (logFile.getFilePath().equals(currentFileName)) {
                            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                            if (!string.equals("####")) {
                                logFile.setPositionsOfString(linesWithStringNumbers);
                            } else {
                                logFile.setPrefixPositions(linesWithStringNumbers);
                            }
                        }
                    }
                    currentFileName = fileName;
                    System.out.println("not clear " + linesWithStringNumbers);
                    linesWithStringNumbers.clear();
                    System.out.println("clear " + linesWithStringNumbers);
                    System.out.println("___________________________");
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
        return logFiles;
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

    private static String getBlock(LogFile logFile, int fromLineNumber, int toLineNumber)  {
        System.out.println("getBlock(" + logFile.getFilePath() + ", " + fromLineNumber + ", " + toLineNumber + ")");
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(logFile.getFilePath());
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
        Map<Integer, Integer> blockPositions;

        List<LogFile> logFiles = FileSearcher.getLogFiles(location);

        List<LogMessage> logMessageList = new ArrayList<>();

        getPositionsOfLinesWithString(string, logFiles);
        getPositionsOfLinesWithString("####", logFiles);

        System.out.println(logFiles);
        for (LogFile logFile : logFiles) {
            blockPositions = getBlockPositions(logFile.getPositionsOfString(), logFile.getPrefixPositions());

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

        }




        return logMessageList;
    }

}
