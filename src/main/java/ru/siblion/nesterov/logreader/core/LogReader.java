package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.*;
import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
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
    private Set<String> logFiles;
    private String message;
    private int countMessage = 0;

    private static final Logger logger = MyLogger.getLogger();

    public LogReader(String string, List<DateInterval> dateIntervals, LocationType locationType, String location) {
        FileSearcher fileSearcher = new FileSearcher();
        logFiles = fileSearcher.getLogFiles(locationType, location);
        if (logFiles.size() > 0) {
            this.dateIntervals = dateIntervals;
            this.string = string;
        } else {
            message = "Неверный параметр location";
            logger.log(Level.INFO, "Неверный параметр location");
        }
    }

    public String getMessage() {
        return message;
    }

    private Map<String, List<Integer>> getPositionsOfLinesWithString(String string) {
        Map<String, List<Integer>> positions = new HashMap<>();

        if (logFiles.size() == 0) {
            return null; // если лог-файлов для поиска сообщений нет, то нет смысла искать
        }

        StringBuilder filesString = new StringBuilder();
        for (String logFile : logFiles) {
            filesString.append(logFile + " "); // возможно стоит переписать, используя StringJoiner, чтобы не было пробела в конце
        }

        String sFindstrParam = " ";
        String bFindstrParam = " ";
        if (logFiles.size() == 1)  sFindstrParam = " /s "; // параметр /s нужен для того, тобы при поиске в одном файле выводился путь файла
        if (string.equals("####")) bFindstrParam = " /b ";  // параметр /b добавляется, если ищутся ####, чтобы искать только в начале строки

        String findstrCommand = "findstr" + sFindstrParam + bFindstrParam + "/n /r /c:\"" + string + "\" " + filesString;
        Process findstrProcess = null;
        try {
            findstrProcess = Runtime.getRuntime().exec(findstrCommand);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            InputStreamReader findstrProcessInputStreamReader = new InputStreamReader(findstrProcessInputStream);
            BufferedReader reader = new BufferedReader(findstrProcessInputStreamReader)) {

            String line = reader.readLine();

            /* Строка представляет собой - файл:номер - если несколько файлов */
            Pattern lineNumberPattern = Pattern.compile("^.*?:(?<lineNumber>[0-9]+?):"); // TODO: 31.01.2017 Почитать про регулярные выражения
            Matcher lineNumberMatcher;

            Pattern filePattern = Pattern.compile("[^.]+\\.log\\d*"); // вернуть ^ в начало выражения
            Matcher fileMatcher;

            while (line != null) {
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group("lineNumber");
                Integer lineNumber = Integer.parseInt(lineNumberString);

                fileMatcher = filePattern.matcher(line);
                fileMatcher.find();
                String currentFile = fileMatcher.group();
                put(positions, currentFile, lineNumber);

                line = reader.readLine();
            }
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении номеров строк в файле", e) ;
        }
        return positions;
    }

    private void put(Map<String, List<Integer>> positions, String file, int lineNumber) {
        if (positions.containsKey(file)) {
            positions.get(file).add(lineNumber);
        } else {
            List<Integer> lineNumberList = new ArrayList<>();
            lineNumberList.add(lineNumber);
            positions.put(file, lineNumberList);
        }
    }

    private List<Pair<Integer, Integer>> getBlockPositions(String file, List<Integer> positionsOfLinesWithString,
                                                    List<Integer> prefixPositions) {
        List<Pair<Integer, Integer>> blockPositions = new ArrayList<>();
        int start;
        int end;
        for (int i = 0; i < positionsOfLinesWithString.size(); i++) {
            for (int j = 0; j < prefixPositions.size(); j++) {
                if (positionsOfLinesWithString.get(i) >= prefixPositions.get(j)
                        && j + 1 < prefixPositions.size()
                        && positionsOfLinesWithString.get(i) < prefixPositions.get(j + 1)) {
                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j + 1) - 1;
                    blockPositions.add(new Pair<>(start, end));
                    break;
                }
                if (j + 1 == prefixPositions.size()) { // Если дошли до конца, значит искомая строка внутри последней строки-блока
                    start =  prefixPositions.get(j);
                    int numberOfLinesInFile = 0;

                    try(FileReader fileReader = new FileReader(file); // FileReader или FileInputStream стоит использовать?
                        BufferedReader bufferedReader = new BufferedReader(fileReader)) {

                        while (bufferedReader.readLine() != null) {
                            numberOfLinesInFile++;
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    end =  numberOfLinesInFile;
                    blockPositions.add(new Pair<>(start, end));
                }
            }
        }
        return blockPositions;
    }

    private List<LogMessage> getLogMessagesForLogFile(String logFile, List<DateInterval> dateIntervals, List<Pair<Integer, Integer>> blockPositions)  {
        List<LogMessage> logMessages = new ArrayList<>();
        StringBuilder block;

        try(FileReader fileReader = new FileReader(logFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

            int fromLineNumber;
            int toLineNumber;
            int previousToLineNumber = 0;
            for (Pair<Integer, Integer> blockPosition : blockPositions) {
                fromLineNumber = blockPosition.getFirst();
                toLineNumber = blockPosition.getSecond();

                for (int i = previousToLineNumber; i < fromLineNumber - 1; i++) {
                    bufferedReader.readLine();
                }

                String firstBlockLine = bufferedReader.readLine();

                XMLGregorianCalendar logMessageDate = LogMessage.parseDate(datatypeFactory, firstBlockLine); // TODO: 31.01.2017 оптимизировать, так как много занимает время
                for (DateInterval dateInterval : dateIntervals) {
                    if (dateInterval.containsDate(logMessageDate)) {
                        block = new StringBuilder();
                        block.append(firstBlockLine + "\n");
                        for (int i = fromLineNumber + 1; i <= toLineNumber; i++) {
                            block.append(bufferedReader.readLine() + "\n");
                        }

                        logMessages.add(new LogMessage(logMessageDate, block.toString())); // в какой момент лучше преобразовывать в String?
                        countMessage++;
                        break; // если дата лог-сообщения входит хотя бы в один интервал дат, то добавляет его и рассматриваем следующее
                    } else {
                        for (int i = fromLineNumber + 1; i <= toLineNumber; i++) {
                            bufferedReader.readLine();
                        }
                    }
                }
                previousToLineNumber = toLineNumber;
            }
        } catch(IOException e){
            logger.log(Level.SEVERE, "Ошибка при парсинге блока", e) ;
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return logMessages;
    }

    public List<LogMessage> getLogMessages() throws Exception {

        Map<String, List<Integer>> prefixPositions = getPositionsOfLinesWithString("####");
        Map<String, List<Integer>> stringPositions = getPositionsOfLinesWithString(string);

        List<LogMessage> logMessageList = new ArrayList<>();
        for (String logFile : logFiles) {

            if (prefixPositions.get(logFile) == null || stringPositions.get(logFile)== null ) {
                continue; // Если лог-файл не содержит искомую строки или префиксы, то не обрабатываем его
            }
            List<Pair<Integer, Integer>> blockPositions;
            blockPositions = getBlockPositions(logFile, stringPositions.get(logFile), prefixPositions.get(logFile));
            logMessageList.addAll(getLogMessagesForLogFile(logFile, dateIntervals, blockPositions));
        }
        message = countMessage + " log messages found";
        Collections.sort(logMessageList); // попробовать другую структуру данных, где не нужно сортировать в конце!
        return logMessageList;
    }
}