package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 15.12.2016.
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD) // решает ошибку для локального теста "Class has two properties of the same name"
public class Request {

    @XmlElement(name = "string")
    private String string;

    @XmlElement(name = "location")
    private String location;

    @XmlElement(name = "dateIntervals")
    private List<DateInterval> dateIntervals;

    @XmlElement(name = "fileFormat")
    private FileFormat fileFormat;

    private static final Logger logger = MyLogger.getLogger(); // проверить правильно работает в xml

    private static final String DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";

    private File outputFile;

    private Date date;

    private static final int NUMBER_OF_THREADS = 10;

    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private Request(String string, String location, List<DateInterval> dateIntervals, FileFormat fileFormat) {
        logger.log(Level.INFO, "Конструктор " + "string " + string + "location " + location + "dateIntervals " + dateIntervals + "fileFormat " + fileFormat);
        this.string = string;
        this.location = location;
        this.dateIntervals = dateIntervals;
        this.fileFormat = fileFormat;
    }
    public  void configure() {
        logger.log(Level.INFO, "Конфигурация запроса: " + this);
        this.date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
        String formattedDate = simpleDateFormat.format(date);
        outputFile =  new File(DIRECTORY + formattedDate + "-" + this.hashCode() + "." + fileFormat);
    }

    public static Request getNewRequest(String string, String location,
                                        List<DateInterval> dateIntervals, FileFormat fileFormat) {
        logger.log(Level.INFO, "Инициализация запроса... ");
        Request request = new Request(string, location, dateIntervals, fileFormat);
        logger.log(Level.INFO, "Создан новый запрос " + request);

        return request;
    }

    public Request() { // Нужен для JAXB или точнее для xml-object преобразования

    }

    public String getString() {
        return string;
    }
    public void setString(String string) {
        this.string = string;
    }


    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }


    public List<DateInterval> getDateIntervals() {
        return dateIntervals;
    }
    public void setDateFrom(List<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }


    public FileFormat getFileFormat() {
        return fileFormat;
    }
    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }


    public List<LogMessage> getListOfLogMessages() {
        logger.log(Level.INFO, "Начинается получение листа логов");
        List<LogMessage> logMessageList = null;
        logger.log(Level.INFO, "DateIntervals " + dateIntervals);
        for (DateInterval dateInterval : dateIntervals) {
            logger.log(Level.INFO, "date Interval " + dateInterval);
            try {
                logger.log(Level.INFO, "Пробую получить лист логов");
                logMessageList = getLogMessages(string, location, dateInterval.getDateFrom(), dateInterval.getDateTo());
                Collections.sort(logMessageList);
                logger.log(Level.INFO, "Отсортировал: " + logMessageList);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Проблема при получении лог-сообщения", e) ;
            }
        }
        logger.log(Level.INFO, "Запрос " + this + " успешно завершен");
        return logMessageList;
    }

    public void saveResultToFile() {
        logger.log(Level.INFO, "starting save result to file");
        List<LogMessage> logMessageList = getListOfLogMessages();
        logger.log(Level.INFO, "save log message list to file: \n" + logMessageList);
        System.out.println("LOG " + logMessageList);
        LogMessages logMessages = new LogMessages(this, logMessageList);
        ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter(logMessages);
        objectToFileWriter.write(fileFormat, outputFile);
    }

    public File getResponse() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("NEW THREAD");
                saveResultToFile();
            }
        }, "searching and writing logs");
        //executorService.shutdown();
        return outputFile;
    }

    @Override
    public String toString() {
        return "Request:" + "\n\tString: " + string + "\n\tLocation: " + location + "\n\tDateIntervals: " + dateIntervals + "\n\tFileFormat: " + fileFormat;
    }

}
