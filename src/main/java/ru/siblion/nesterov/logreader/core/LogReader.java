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
    private Map<String, List<Integer>> prefixPositions = new HashMap<>();
    private Map<String, List<Integer>> stringPositions = new HashMap<>();
    private Set<String> logFiles;
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
        for (String logFile : logFiles) {
            firstLogFilePath = logFile;
            filesString.append(logFile + " "); // возможно стоит переписать, используя StringJoiner, чтобы не было пробела в конце
        }

        String findstrCommand;
        findstrCommand = "findstr /n /r \"" + string + "\" " + filesString;
        System.out.println("findstr /n /r \"" + string + "\" " + filesString);

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
                    Pattern lineNumberPattern = Pattern.compile("\\d+");
                    Matcher lineNumberMatcher;
                    lineNumberMatcher = lineNumberPattern.matcher(line);
                    lineNumberMatcher.find();
                    String lineNumberString = lineNumberMatcher.group();
                    linesWithStringNumbers.add(Integer.parseInt(lineNumberString));
                    line = reader.readLine();
                }
                if (string.equals("####")) {
                    prefixPositions.put(firstLogFilePath, linesWithStringNumbers);
                } else {
                    stringPositions.put(firstLogFilePath, linesWithStringNumbers);
                }
                return;
            }

            Pattern lineNumberPattern = Pattern.compile(":\\d+");
            Matcher lineNumberMatcher;

            Pattern filePattern = Pattern.compile("[^.]+\\.log\\d*"); // вернуть ^ в начало выражения
            Matcher fileMatcher;
            fileMatcher = filePattern.matcher(line);
            fileMatcher.find();
            String currentFile = fileMatcher.group();

            String nextFile;

            while (line != null) {
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group().substring(1);
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));

                line = reader.readLine();
                System.out.println("LINE      :::: " + line);
                if (line != null) {
                    fileMatcher = filePattern.matcher(line);
                    fileMatcher.find();
                    nextFile = fileMatcher.group();

                    if (!nextFile.equals(currentFile)) { // проверить последний случай!
                        System.out.println("currentFile " + currentFile);
                        if (string.equals("####")) {
                            prefixPositions.put(currentFile, linesWithStringNumbers);
                        } else {
                            stringPositions.put(currentFile, linesWithStringNumbers);
                        }
                        System.out.println("prefixPositions " + prefixPositions);
                        System.out.println("stringPositions " + stringPositions);
                        currentFile = nextFile;
                        linesWithStringNumbers = new ArrayList<>();
                    }
                } else {
                    System.out.println("LINE IS NULL");
                }
            }
            System.out.println("currentFile " + currentFile);
            if (string.equals("####")) {
                prefixPositions.put(currentFile, linesWithStringNumbers);
            } else {
                stringPositions.put(currentFile, linesWithStringNumbers);
            }
            System.out.println("LAST prefixPositions " + prefixPositions);
            System.out.println("LAST stringPositions " + stringPositions);

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

    private String getBlock(String logFile, int fromLineNumber, int toLineNumber)  {
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(logFile);
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
        for (String logFile : logFiles) {

            if (prefixPositions.get(logFile) == null || stringPositions.get(logFile)== null ) {
                continue; // Если лог-файл не содержит искомую строки или префиксы, то не обрабатываем его
            }

            Map<Integer, Integer> blockPositions;
            blockPositions = getBlockPositions(stringPositions.get(logFile), prefixPositions.get(logFile));
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