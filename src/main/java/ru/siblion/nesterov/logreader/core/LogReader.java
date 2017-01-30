package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.*;
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.util.Utils;

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

    private String string;
    private List<DateInterval> dateIntervals;
    private Map<String, LogFile> logFiles;
    private String message;

    private static final Logger logger = MyLogger.getLogger();

    public LogReader(String string, List<DateInterval> dateIntervals, LocationType locationType, String location) {
        FileSearcher fileSearcher = new FileSearcher();
        logFiles = fileSearcher.getLogFiles(locationType, location);

        if (logFiles.size() == 0) {
            message = "Неверный параметр location";
            logger.log(Level.INFO, "Неверный параметр location");
        }
        // если лог-файлы отсутствуют, то можно дальнейшие операторы не выполнять!!
        this.dateIntervals = dateIntervals;
        this.string = string;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void getPositionsOfLinesWithString(String string) {
        if (logFiles.size() == 0) {
            return; // если лог-файлов для поиска сообщений нет, то нет смысла искать
        }
        StringBuilder filesString = new StringBuilder();
        String firstLogFilePath = null;
        for (LogFile logFile : logFiles.values()) {
            firstLogFilePath = logFile.getFilePath();
            filesString.append("\"" + logFile.getFilePath() + "\" "); // возможно стоит переписать, используя StringJoiner, чтобы не было пробела в конце
        }

        String findstrCommand;
        if (logFiles.size() == 1) {
            findstrCommand = "cmd /Q /C for /f \" delims=:\" %a in ('findstr /n /r /c \"" + string + "\" " + filesString + "') do echo %a";
        } else {
            findstrCommand = "cmd /Q /C for /f \" tokens=1-3 delims=:\" %a in ('findstr /n /r /c \"" + string + "\" " + filesString + "') do echo %a:%b:%c";
        }
        logger.log(Level.INFO, "command:\n" + findstrCommand);

        try {
            Process findstrProcess = Runtime.getRuntime().exec(findstrCommand);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));

            /* Строка представляет собой номер строки, в которой найдено выражение, если файл один
             * или файл:номер - если несколько файлов */
            String line = reader.readLine();

            List<Integer> linesWithStringNumbers = new ArrayList<>();
            if (logFiles.size() == 1) {
                while (line != null) {
                    linesWithStringNumbers.add(Integer.parseInt(line));
                    line = reader.readLine();
                }
                if (string.equals("####")) {
                    logFiles.get(firstLogFilePath).setPrefixPositions(linesWithStringNumbers);
                } else {
                    logFiles.get(firstLogFilePath).setPositionsOfString(linesWithStringNumbers);
                }
                return;
            }

            Pattern lineNumberPattern = Pattern.compile(":\\d+");
            Matcher lineNumberMatcher;

            Pattern fileNamePattern = Pattern.compile("^[^.]+\\.log\\d*");
            Matcher fileNameMatcher;
            fileNameMatcher = fileNamePattern.matcher(line);
            fileNameMatcher.find();
            String currentFileName = fileNameMatcher.group();

            String fileName;
            while (line != null) {
                fileNameMatcher = fileNamePattern.matcher(line);
                fileNameMatcher.find();
                fileName = fileNameMatcher.group();
                if (!fileName.equals(currentFileName)) { // проверить последний случай!
                    LogFile logFile = logFiles.get(currentFileName);
                    if (string.equals("####")) {
                        logFile.setPrefixPositions(linesWithStringNumbers);
                    } else {
                        logFile.setPositionsOfString(linesWithStringNumbers);
                    }
                }
                currentFileName = fileName;
                linesWithStringNumbers.clear();
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group().substring(1);
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));

                line = reader.readLine();
            }
            LogFile logFile = logFiles.get(currentFileName);
            if (string.equals("####")) {
                logFile.setPrefixPositions(linesWithStringNumbers);
            } else {
                logFile.setPositionsOfString(linesWithStringNumbers);
            }

        } catch(IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении номеров строк в файле", e) ;
        }
    }

    private Map<Integer, Integer> getBlockPositions(List<Integer> positionsOfLinesWithString,
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

    private String getBlock(LogFile logFile, int fromLineNumber, int toLineNumber)  {
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

    public List<LogMessage> getLogMessages() throws Exception {

        getPositionsOfLinesWithString(string);
        getPositionsOfLinesWithString("####");

        List<LogMessage> logMessageList = new ArrayList<>();
        for (LogFile logFile : logFiles.values()) {
            logger.log(Level.INFO, "Log файл обрабатываю: " + logFile + " ))))");

            if (logFile.getPositionsOfString() == null || logFile.getPrefixPositions() == null ) {
                continue; // Если лог-файл не содержит искомую строки или префиксы, то не обрабатываем его
            }

            Map<Integer, Integer> blockPositions;
            blockPositions = getBlockPositions(logFile.getPositionsOfString(), logFile.getPrefixPositions());
            logger.log(Level.INFO, "blockPositions " + blockPositions);

            String currentBlock;
            for (Map.Entry<Integer, Integer> entry : blockPositions.entrySet()) {
                currentBlock = (getBlock(logFile, entry.getKey(), entry.getValue())).substring(4); // удаляем префикс ####
                LogMessage logMessage = new LogMessage(currentBlock);
                XMLGregorianCalendar logMessageDate = logMessage.getDate();

                for (DateInterval dateInterval : dateIntervals) {
                    if (dateInterval.containsDate(logMessageDate)) {
                        logMessageList.add(logMessage);
                        break; // если дата лог-сообщения входит хотя бы в один интервал дат, то добавляет его и рассматриваем следующее
                    }
                }

            }
        }
        Collections.sort(logMessageList); // попробовать другую структуру данных, где не нужно сортировать в конце!
        return logMessageList;
    }

}
