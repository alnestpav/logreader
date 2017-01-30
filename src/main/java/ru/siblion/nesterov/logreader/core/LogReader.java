package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.*;
import ru.siblion.nesterov.logreader.util.MyLogger;

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

    /* Метод*/
    private Map<String, List<Integer>> getPositionsOfLinesWithString(String string) {
        Map<String, List<Integer>> positions = new HashMap<>();

        if (logFiles.size() == 0) {
            return null; // если лог-файлов для поиска сообщений нет, то нет смысла искать
        }

        StringBuilder filesString = new StringBuilder();
        String firstLogFilePath = null;
        for (String logFile : logFiles) {
            firstLogFilePath = logFile;
            filesString.append(logFile + " "); // возможно стоит переписать, используя StringJoiner, чтобы не было пробела в конце
        }

        String findstrCommand;
        findstrCommand = "findstr /n /r \"" + string + "\" " + filesString; // параметр /c: нужен ли?

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

            List<Integer> linesWithStringNumbers = new ArrayList<>();
            if (logFiles.size() == 1) { /* Строка представляет собой номер строки, в которой найдено выражение, если файл один */
                while (line != null) {
                    Pattern lineNumberPattern = Pattern.compile("\\d+");
                    Matcher lineNumberMatcher;
                    lineNumberMatcher = lineNumberPattern.matcher(line);
                    lineNumberMatcher.find();
                    String lineNumberString = lineNumberMatcher.group();
                    linesWithStringNumbers.add(Integer.parseInt(lineNumberString));
                    line = reader.readLine();
                }
                positions.put(firstLogFilePath, linesWithStringNumbers);
                return positions;
            }

            /* Строка представляет собой - файл:номер - если несколько файлов */
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
                if (line != null) {
                    fileMatcher = filePattern.matcher(line);
                    fileMatcher.find();
                    nextFile = fileMatcher.group();

                    if (!nextFile.equals(currentFile)) { // проверить последний случай!
                        positions.put(currentFile, linesWithStringNumbers);
                        currentFile = nextFile;
                        linesWithStringNumbers = new ArrayList<>();
                    }
                }
            }
            positions.put(currentFile, linesWithStringNumbers);
        } catch(IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении номеров строк в файле", e) ;
        }
        return positions;
    }

    private Collection<int[]> getBlockPositions(List<Integer> positionsOfLinesWithString,
                                                    List<Integer> prefixPositions) {
        Collection<int[]>  blockPositions = new ArrayList<>();
        int start;
        int end;
        for (int i = 0; i < positionsOfLinesWithString.size(); i++) {
            for (int j = 0; j < prefixPositions.size(); j++) {
                if (positionsOfLinesWithString.get(i) >= prefixPositions.get(j)
                        && j + 1 < prefixPositions.size()
                        && positionsOfLinesWithString.get(i) < prefixPositions.get(j + 1)) {

                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j + 1) - 1;
                    blockPositions.add(new int[] {start, end});
                    break;
                }
                if (j + 1 == prefixPositions.size()) { // Если дошли до конца, значит искомая строка внутри последней строки-блока
                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j);
                    blockPositions.add(new int[] {start, end});
                }
            }
        }
        return blockPositions;
    }

    private Collection<String> getBlocks(String logFile, List<DateInterval> dateIntervals, Collection<int[]> blockPositions)  {
        Collection<String> blocks = new ArrayList<>();
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(logFile);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)){

            int fromLineNumber;
            int toLineNumber;
            int previousToLineNumber = 1;
            for (int[] blockPosition: blockPositions) {
                fromLineNumber = blockPosition[0];
                toLineNumber = blockPosition[1];

                for (int i = previousToLineNumber + 1; i < fromLineNumber; i++) {
                    lineNumberReader.readLine();
                }
                String firstBlockLine = lineNumberReader.readLine();
                XMLGregorianCalendar logMessageDate = LogMessage.parseDate(firstBlockLine);
                for (DateInterval dateInterval : dateIntervals) {
                    if (dateInterval.containsDate(logMessageDate)) {
                        block = new StringBuilder();
                        block.append(firstBlockLine + "\n");
                        for (int i = fromLineNumber + 1; i <= toLineNumber; i++) {
                            block.append(lineNumberReader.readLine() + "\n");
                        }
                        blocks.add(block.toString()); // в какой момент лучше преобразовывать в String?
                        break; // если дата лог-сообщения входит хотя бы в один интервал дат, то добавляет его и рассматриваем следующее
                    }
                }
                previousToLineNumber = toLineNumber;
            }
        } catch(IOException e){
            logger.log(Level.SEVERE, "Ошибка при парсинге блока", e) ;
        }
        System.out.println(block.toString());
        return blocks;
    }

    public List<LogMessage> getLogMessages() throws Exception {

        Map<String, List<Integer>> prefixPositions = getPositionsOfLinesWithString("####");
        Map<String, List<Integer>> stringPositions = getPositionsOfLinesWithString(string);

        List<LogMessage> logMessageList = new ArrayList<>();
        for (String logFile : logFiles) {
            if (prefixPositions.get(logFile) == null || stringPositions.get(logFile)== null ) {
                continue; // Если лог-файл не содержит искомую строки или префиксы, то не обрабатываем его
            }

            Collection<int[]> blockPositions;
            blockPositions = getBlockPositions(stringPositions.get(logFile), prefixPositions.get(logFile));
            logger.log(Level.INFO, "blockPositions " + blockPositions);
            Collection<String> blocks = getBlocks(logFile, dateIntervals, blockPositions);
            logMessageList.add(blocks);
        }
        Collections.sort(logMessageList); // попробовать другую структуру данных, где не нужно сортировать в конце!
        return logMessageList;
    }
}