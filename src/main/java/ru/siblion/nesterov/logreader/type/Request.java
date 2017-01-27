package ru.siblion.nesterov.logreader.type;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.xml.bind.annotation.*;
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

import static ru.siblion.nesterov.logreader.core.LogReader.getLogMessages;

/**
 * Created by alexander on 15.12.2016.
 */

/* Класс, инкапсулирующий запрос пользователя */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD) // решает ошибку для локального теста "Class has two properties of the same name"
public class Request {

    @XmlElement(name = "string")
    private String string;

    @XmlElement(name = "locationType")
    private LocationType locationType;

    @XmlElement(name = "location")
    private String location;

    @XmlElement(name = "dateIntervals")
    private List<DateInterval> dateIntervals;

    @XmlElement(name = "fileFormat")
    private FileFormat fileFormat;

    private static final Logger logger = MyLogger.getLogger(); // проверить правильно работает в xml

    @XmlTransient
    private File outputFile;

    @XmlTransient
    private Date date;

    @XmlTransient
    private boolean needsToCache = false; // нужно ли кешировать лог-файл, получаемый при запросе

    @XmlTransient
    private Response response = new Response();


    //private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test
    private static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private static Config config = Config.getConfig(configFile);

    private static final String DIRECTORY = config.getDirectory().toString(); // возможно изменить getDirectory возвр. знач. на String

    private static final int NUMBER_OF_THREADS = 10;

    private static ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private Request(String string, LocationType locationType, String location, List<DateInterval> dateIntervals, FileFormat fileFormat) {
        logger.log(Level.INFO, "Конструктор " + "string " + string + "location " + location + "dateIntervals " + dateIntervals + "fileFormat " + fileFormat);
        this.string = string;
        this.locationType = locationType;
        this.location = location;
        this.dateIntervals = dateIntervals;
        this.fileFormat = fileFormat;
    }

    /* Метод инициализирует поля date и outputFile, необходим для работы веб-сервисов */
    public void initSomeFields() {
        logger.log(Level.INFO, "Конфигурация запроса: " + this);
        this.date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
        String formattedDate = simpleDateFormat.format(date);
        outputFile =  new File(DIRECTORY + "\\log-d" + formattedDate + "h" + this.hashCode() + "." + fileFormat);

        if (dateIntervals == null) {
            List<DateInterval> emptyDateIntervals = new ArrayList<>();
            emptyDateIntervals.add(new DateInterval(null, null));
        }
    }

    public static Request getNewRequest(String string, LocationType locationType, String location,
                                        List<DateInterval> dateIntervals, FileFormat fileFormat) {
        logger.log(Level.INFO, "Инициализация запроса... ");
        Request request = new Request(string, locationType, location, dateIntervals, fileFormat);
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
    public void setDateIntervals(List<DateInterval> dateIntervals) {
        this.dateIntervals = dateIntervals;
    }


    public FileFormat getFileFormat() {
        return fileFormat;
    }
    public void setFileFormat(FileFormat fileFormat) {
        this.fileFormat = fileFormat;
    }

    public LocationType getLocationType() {
        return locationType;
    }
    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
    }


    public List<LogMessage> getListOfLogMessages() {
        logger.log(Level.INFO, "Начинается получение листа логов");
        List<LogMessage> logMessageList = null;
        logger.log(Level.INFO, "DateIntervals " + dateIntervals);
        for (DateInterval dateInterval : dateIntervals) {
            logger.log(Level.INFO, "date Interval " + dateInterval);
            try {
                logger.log(Level.INFO, "Пробую получить лист логов");
                logMessageList = getLogMessages(string, locationType, location, dateInterval.getDateFrom(), dateInterval.getDateTo());
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

    private boolean checkCacheFile() {
        for (DateInterval dateInterval: dateIntervals) {
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
            if (dateTo != null && dateTo.compare(xmlGregorianDate) < 0) {
                needsToCache = true;
            }
        }
        return needsToCache; // возможно стоит поменять тип возвращаемого значения
    }

    private File searchCacheFile() {
        List<String> files = Utils.getFilesMatching(config.getDirectory(), ".+" + hashCode() + "\\." + fileFormat);
        if (files.isEmpty()) {
            return null;
        } else {
            return new File(files.get(0));
        }
    }

    public Response getResponse() {
        initSomeFields();
        if (fileFormat == null) {
             System.out.println("fileFormat==null");
            response.setLogMessages(getListOfLogMessages());
        } else {
            System.out.println("fileFormat!=null");
            response.setOutputFile(outputFile);
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("NEW THREAD");

                if (checkCacheFile() == true && searchCacheFile() != null) {
                    outputFile = searchCacheFile();
                } else {
                    saveResultToFile();
                }

                }
            }, "searching and writing logs");
            //executorService.shutdown(); // для лок теста нужен, для веб - нет
        }
        logger.log(Level.INFO, "Main Process");
        return response;
    }

    @Override
    public String toString() {
        return "Request:" + "\n\tString: " + string + "\n\tLocationType: " + locationType + "\n\tLocation: " + location + "\n\tDateIntervals: " + dateIntervals + "\n\tFileFormat: " + fileFormat;
    }

    @Override
    public int hashCode() {
        int result = string != null ? string.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (dateIntervals != null ? dateIntervals.hashCode() : 0);
        result = 31 * result + (fileFormat != null ? fileFormat.hashCode() : 0);
        return result;
    }
}
