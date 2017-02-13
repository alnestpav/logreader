package ru.siblion.nesterov.logreader.core;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 13.02.2017.
 */

/* Класс, созданный для того, чтобы вынести из класса Request всю бизнес-логику */
public class RequestController {
    private Request request;
    private Response response = new Response();

    private final static Properties APP_CONFIG_PROPERTIES = AppConfig.getProperties();
    private final String DIRECTORY = APP_CONFIG_PROPERTIES.getProperty("directory");

    private static final int NUMBER_OF_THREADS = 10;
    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final Logger logger = AppLogger.getLogger();

    public RequestController(Request request) {
        this.request = request;

        if (request.getDateIntervals() == null) {
            List<DateInterval> emptyDateIntervals = new ArrayList<>();
            emptyDateIntervals.add(new DateInterval(null, null));
        }
    }


    private List<LogMessage> getListOfLogMessages() {
        LogReader logReader = new LogReader(request.getString(), request.getDateIntervals(), request.getLocationType(), request.getLocation());
        List<LogMessage>  logMessageList = null;
        try {
            logMessageList = logReader.getLogMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setMessage(logReader.getMessage());
        logger.log(Level.INFO, "Запрос " + request + " успешно завершен");
        return logMessageList;
    }

    private void saveResultToFile() {
        List<LogMessage> logMessageList = getListOfLogMessages();
        LogMessages logMessages = new LogMessages(request, logMessageList);
        ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter();
        objectToFileWriter.write(logMessages, request.getFileFormat(), response.getOutputFile());
    }

    private boolean checkCacheFile() {
        for (DateInterval dateInterval: request.getDateIntervals()) {
            Date currentDate = new Date();
            XMLGregorianCalendar xmlGregorianDate = new XMLGregorianCalendarImpl();
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(currentDate);
            XMLGregorianCalendar dateTo = dateInterval.getDateTo();
            try {
                xmlGregorianDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException e) {
                logger.log(Level.SEVERE, "Ошибка при получения экземпляра XMLGregorianCalendar", e) ;
            }
            if (dateTo != null && dateTo.compare(xmlGregorianDate) > 0) {
                return false;
            }
        }
        return true; // возможно стоит поменять тип возвращаемого значения
    }

    private File searchCacheFile() {
        List<String> files = Utils.getFilesMatching(
                new File(APP_CONFIG_PROPERTIES.getProperty("directory")), ".+" + hashCode() + "\\." + request.getFileFormat());
        if (files.isEmpty()) {
            return null;
        } else {
            return new File(files.get(0));
        }
    }

    public Response getResponse() {
        if (request.getFileFormat() == null) {
            response.setLogMessages(getListOfLogMessages());
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
            Date requestDate = new Date(); // дата, время получения запроса
            String formattedDate = simpleDateFormat.format(requestDate);
            response.setOutputFile(
                    new File(DIRECTORY + "\\log-d" + formattedDate + "h" + request.hashCode() + "." + request.getFileFormat()));
            executorService.submit(() -> {
                if (checkCacheFile() && searchCacheFile() != null) {
                    response.setOutputFile(searchCacheFile());
                } else {
                    saveResultToFile();
                }
            });
            /* Если не закомментировать следующую строку,
               то нельзя более одного запроса отправить на поиск логов со скачиванием файла */
            // executorService.shutdown(); // TODO: 09.02.2017 Почитать про ExecutorService
        }
        return response;
    }

}
