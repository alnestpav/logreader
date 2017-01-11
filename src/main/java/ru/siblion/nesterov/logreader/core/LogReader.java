package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.LogFile;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 13.12.2016.
 */

/* Класс для получения лог-сообщений */
public class LogReader {

    private static final Logger logger = MyLogger.getLogger();

    private static void getPositionsOfLinesWithString(String string, List<LogFile> logFiles) {
        logger.log(Level.INFO, "log files: " + logFiles + "string=" + string);
        StringBuilder filesString = new StringBuilder();
        for (LogFile logFile: logFiles) {
            filesString.append(logFile.getFilePath() + " ");
        }
        System.out.println("filesString " + filesString);
        String command = "findstr /n /r /c:" + "\"" + string + "\"" + " " + filesString;
        logger.log(Level.INFO, "command:\n" + command);

        try {
            Process findstrProcess = Runtime.getRuntime().exec(command);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));
            String line = reader.readLine();

            List<Integer> linesWithStringNumbers = new ArrayList<>();
            if (logFiles.size() == 1) {
                while (line != null) {
                    Pattern lineNumberPattern = Pattern.compile("\\d+");
                    Matcher lineNumberMatcher;
                    lineNumberMatcher = lineNumberPattern.matcher(line);
                    lineNumberMatcher.find();
                    String lineNumberString = lineNumberMatcher.group();
                    linesWithStringNumbers.add(Integer.parseInt(lineNumberString));
                    line = reader.readLine();
                }
                if (!string.equals("####")) {
                    LogFile logFile = new LogFile(logFiles.get(0));
                    logFile.setPositionsOfString(linesWithStringNumbers);
                    logFiles.set(0, logFile);
                } else {
                    LogFile logFile = new LogFile(logFiles.get(0));
                    logFile.setPrefixPositions(linesWithStringNumbers);
                    logFiles.set(0, logFile);;
                }
                return;
            }
            Pattern lineNumberPattern = Pattern.compile(":\\d+");
            Matcher lineNumberMatcher;

            Pattern fileNamePattern = Pattern.compile("[^.]+\\.log\\d*");
            Matcher fileNameMatcher;
            fileNameMatcher = fileNamePattern.matcher(line);
            fileNameMatcher.find();
            String currentFileName = fileNameMatcher.group();

            while (line != null) {
                fileNameMatcher = fileNamePattern.matcher(line);
                fileNameMatcher.find();
                String fileName = fileNameMatcher.group();
                logger.log(Level.INFO, "fileName from findstr: " + fileName);
                if (!fileName.equals(currentFileName)) { // проверить последний случай!
                    for (LogFile logFile : logFiles) {
                        if (logFile.getFilePath().equals(currentFileName)) {
                            if (!string.equals("####")) {
                                logFile.setPositionsOfString(linesWithStringNumbers);
                            } else {
                                logFile.setPrefixPositions(linesWithStringNumbers);
                            }
                        }
                    }
                    currentFileName = fileName;
                    linesWithStringNumbers.clear();
                }
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group().substring(1);
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));

                line = reader.readLine();
            }
            for (LogFile logFile : logFiles) {
                if (logFile.getFilePath().equals(currentFileName)) {
                    if (!string.equals("####")) {
                        logFile.setPositionsOfString(linesWithStringNumbers);
                    } else {
                        logFile.setPrefixPositions(linesWithStringNumbers);
                    }
                }
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении номеров строк в файле", e) ;
        }
    }

    private static Map<Integer, Integer> getBlockPositions(List<Integer> positionsOfLinesWithString,
                                                          List<Integer> prefixPositions) {
        logger.log(Level.INFO, "getBlockPostitions");
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
        logger.log(Level.INFO, "getBlock(" + logFile.getFilePath() + ", " + fromLineNumber + ", " + toLineNumber + ")");
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(logFile.getFilePath());
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)){

            for (int i = 1; i < fromLineNumber; i++) {
                lineNumberReader.readLine();
            }
            for (int i = fromLineNumber; i <= toLineNumber; i++) {
                block.append(lineNumberReader.readLine() + "\n");
            }

        } catch(IOException e){
            logger.log(Level.SEVERE, "Ошибка при парсинге блока", e) ;
        }
        System.out.println(block.toString());
        return block.toString();
    }

    public static List<LogMessage> getLogMessages(String string,
                                                  String location,
                                                  XMLGregorianCalendar dateFrom,
                                                  XMLGregorianCalendar dateTo) throws Exception {
        logger.log(Level.INFO, "Получить лог сообщения");
        Map<Integer, Integer> blockPositions;
        FileSearcher fileSearcher = new FileSearcher();
        List<LogMessage> logMessageList = new ArrayList<>();
        List<LogFile> logFiles = fileSearcher.getLogFiles(location);
        logger.log(Level.SEVERE, "logFiles " + logFiles) ;
        if (logFiles.size() == 0) {
            LogMessage logMessage = new LogMessage();
            logMessage.setMessage("Неверный параметр location");
            logger.log(Level.INFO, "Неверный параметр location");
            logMessageList.add(logMessage);
            return logMessageList;
        }

        getPositionsOfLinesWithString(string, logFiles);
        getPositionsOfLinesWithString("####", logFiles);

        System.out.println("LOG FILES " + logFiles);
        logger.log(Level.INFO, "количество лог файлов: " + logFiles.size());
        for (LogFile logFile : logFiles) {
            logger.log(Level.INFO, "Log файл обрабатываю: " + logFile + " ))))");
            if (logFile.getPositionsOfString() == null || logFile.getPrefixPositions() == null ) {
                continue; // Если лог-файл не содержит искомую строки или префиксы, то не обрабатываем его
            }
            blockPositions = getBlockPositions(logFile.getPositionsOfString(), logFile.getPrefixPositions());
            logger.log(Level.INFO, "blockPositions " + blockPositions);
            String currentBlock;
            for (Map.Entry<Integer, Integer> entry : blockPositions.entrySet()) {
                currentBlock = (getBlock(logFile, entry.getKey(), entry.getValue())).substring(4); // удаляем префикс ####
                logger.log(Level.INFO, "CURRENT BLOCK: " + currentBlock);
                LogMessage logMessage = new LogMessage(currentBlock);
                XMLGregorianCalendar logMessageDate = logMessage.getDate();
                logger.log(Level.INFO, "CURRENT logMessageDate: " + logMessageDate);
                if (dateFrom == null && dateTo == null) {
                    logMessageList.add(logMessage);
                    logger.log(Level.INFO, "Добавлено сообщение");
                    continue;
                }
                if (dateFrom == null) {
                    if (logMessageDate.compare(dateTo) <= 0) {
                        logMessageList.add(logMessage);
                        logger.log(Level.INFO, "Добавлено сообщение");
                        continue;
                    }
                }
                if (dateTo == null) {
                    if (logMessageDate.compare(dateFrom) >= 0) {
                        logMessageList.add(logMessage);
                        logger.log(Level.INFO, "Добавлено сообщение");
                        continue;
                    }
                }
                if (logMessageDate.compare(dateFrom) >= 0 && logMessageDate.compare(dateTo) <= 0 ) {
                    logMessageList.add(logMessage);
                    logger.log(Level.INFO, "Добавлено сообщение");
                }
                else {
                    logger.log(Level.INFO, "ELSE УСЛОВИЕ");
                }
            }
        }
        return logMessageList;
    }
}
