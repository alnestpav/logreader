package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.*;
import ru.siblion.nesterov.logreader.util.AppConfig;
import ru.siblion.nesterov.logreader.util.AppLogger;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * Created by alexander on 13.02.2017.
 */

/* Класс принимает запрос пользователя и создает ответ */
public class RequestController {
    private Request request;
    private Response response = new Response(); // создаем ответ на запрос

    private final static Properties APP_CONFIG_PROPERTIES = AppConfig.getProperties();

    /* Директория на сервере, в которую будут сохраняться найденные логи */
    private final String FOUND_LOGS_DIRECTORY = APP_CONFIG_PROPERTIES.getProperty("directory");

    private static final int NUMBER_OF_THREADS = 10;
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final Logger logger = AppLogger.getLogger();

    public RequestController(Request request) {
        this.request = request;

        /* Если интервалы дат не заданы, то в запросе задается интервал (null, null) ,
           так как метод getLogMessagesForLogFile в классе LogReader работает хотя бы с одним интервалом */
        if (request.getDateIntervals() == null) {
            List<DateInterval> emptyDateIntervals = new ArrayList<>();
            emptyDateIntervals.add(new DateInterval(null, null));
            request.setDateIntervals(emptyDateIntervals);
        }
    }


    private List<LogMessage> findLogMessages() {
        LogReader logReader = new LogReader(request.getString(), request.getDateIntervals(), request.getLocationType(), request.getLocation());
        List<LogMessage>  logMessageList = null;
        try {
            logMessageList = logReader.getLogMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setMessage(logReader.getMessage());
        return logMessageList;
    }

    /* Метод проверяет может ли для данного запроса уже существовать кэшированный файл с найденными логами */
    private boolean canRequestHaveCashedFile() {
        try {
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();

            for (DateInterval dateInterval: request.getDateIntervals()) {
                Date currentDate = new Date();
                XMLGregorianCalendar xmlGregorianDate;
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(currentDate);
                XMLGregorianCalendar dateTo = dateInterval.getDateTo();
                xmlGregorianDate = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
                if (dateTo != null && dateTo.compare(xmlGregorianDate) > 0) {
                    return false;
                }
            }
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return true;
    }

    /* Метод ищет в папке с найденными ранее логами файл, который соотвествует
       текущему запросу пользователя и при успешном поиске возвращает его */
    private String returnCachedFileForRequestIfExists() {
        List<String> cachedFiles = Utils.getFilesMatching(new File(FOUND_LOGS_DIRECTORY), ".+" + hashCode() + "\\." + request.getFileFormat());
        if (cachedFiles.isEmpty()) {
            return null;
        } else {
            return cachedFiles.get(0);
        }
    }

    public Response getResponse() {
        if (request.getString() == null) {
            response.setMessage("Неверный параметр string");
            return response;
        }
        if (request.getLocationType() == null) {
            response.setMessage("Неверный параметр locationType");
            return response;
        }
        if (request.getLocation() == null) {
            response.setMessage("Отсутствует параметр location");
            return response;
        }

        /* Если формат файла не задан, то ответ возвращает список найденных логов,
        *  иначе происходит сохранения найденных логов в файл */
        if (request.getFileFormat() == null) {
            response.setLogMessages(findLogMessages());
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
            Date requestDate = new Date(); // дата, время получения запроса
            String formattedDate = simpleDateFormat.format(requestDate);

            /* Ответ немедленно возвращает путь к файлу с найденными логамии,
               который в асинхронном режиме генерируется в отдельном потоке */
            response.setOutputFile(FOUND_LOGS_DIRECTORY + "\\log-d" + formattedDate + "h" + request.hashCode() + "." + request.getFileFormat());

            executorService.submit(() -> {
                String cachedFileForRequest = canRequestHaveCashedFile() ? returnCachedFileForRequestIfExists() : null;
                if (cachedFileForRequest != null) {
                    /* Если кэшированный файл существует, то возвращаем его в ответе */
                    response.setOutputFile(returnCachedFileForRequestIfExists());
                } else {
                    /* Иначе получает список логов и сохраняем его в файл */
                    List<LogMessage> logMessageList = findLogMessages();
                    ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter();
                    objectToFileWriter.write(new LogMessages(request, logMessageList), request.getFileFormat(), response.getOutputFile());
                }
            });

        }
        return response;
    }

}
