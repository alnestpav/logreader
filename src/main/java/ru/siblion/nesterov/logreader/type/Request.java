package ru.siblion.nesterov.logreader.type;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import ru.siblion.nesterov.logreader.core.LogReader;
import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.util.AppConfig;
import ru.siblion.nesterov.logreader.util.AppLogger;
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

    private static final Logger logger = AppLogger.getLogger(); // проверить правильно работает в xml

    @XmlTransient
    private File outputFile;

    @XmlTransient
    private Date date;

    @XmlTransient
    private boolean needsToCache = false; // нужно ли кешировать лог-файл, получаемый при запросе

    @XmlTransient
    private Response response = new Response();

    private final static Properties appConfigProperties = AppConfig.getInstance().getProperties();

    private final String DIRECTORY = appConfigProperties.getProperty("directory");

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

    public Request() {} // Нужен для JAXB или точнее для xml-object преобразования

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
        LogReader logReader = new LogReader(string, dateIntervals, locationType, location);
        List<LogMessage>  logMessageList = null;
        try {
            logMessageList = logReader.getLogMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setMessage(logReader.getMessage());
        logger.log(Level.INFO, "Запрос " + this + " успешно завершен");
        return logMessageList;
    }

    public void saveResultToFile() {
        List<LogMessage> logMessageList = getListOfLogMessages();
        LogMessages logMessages = new LogMessages(this, logMessageList);
        ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter();
        objectToFileWriter.write(logMessages, fileFormat, outputFile);
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
        List<String> files = Utils.getFilesMatching(new File(appConfigProperties.getProperty("directory")), ".+" + hashCode() + "\\." + fileFormat);
        if (files.isEmpty()) {
            return null;
        } else {
            return new File(files.get(0));
        }
    }

    public Response getResponse() {
        initSomeFields();
        if (fileFormat == null) {
            response.setLogMessages(getListOfLogMessages());
        } else {
            response.setOutputFile(outputFile);
            executorService.submit(() -> {
                if (checkCacheFile() == true && searchCacheFile() != null) {
                    outputFile = searchCacheFile();
                } else {
                    saveResultToFile();
                }
            });
            executorService.shutdown();
        }
        return response;
    }

    @Override
    public String toString() {
        return "Request:" + "\n\tString: " + string + "\n\tLocationType: " + locationType + "\n\tLocation: " + location +
                "\n\tDateIntervals: " + dateIntervals +"\n\tFileFormat: " + fileFormat;
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
