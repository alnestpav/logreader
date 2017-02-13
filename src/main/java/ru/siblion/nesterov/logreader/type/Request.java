package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.AppConfig;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @XmlTransient
    private File outputFile;

    @XmlTransient
    private Date date;

    private final static Properties APP_CONFIG_PROPERTIES = AppConfig.getProperties();

    private final String DIRECTORY = APP_CONFIG_PROPERTIES.getProperty("directory");

    public Request() {}

    /* afterUnmarshal - аналог @PostConstruct для JAXB
       Метод инициализирует поля date и outputFile, необходим для работы веб-сервисов */
    void afterUnmarshal(Unmarshaller u, Object parent) {
        this.date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ");
        String formattedDate = simpleDateFormat.format(date);
        outputFile =  new File(DIRECTORY + "\\log-d" + formattedDate + "h" + this.hashCode() + "." + fileFormat);

        if (dateIntervals == null) {
            List<DateInterval> emptyDateIntervals = new ArrayList<>();
            emptyDateIntervals.add(new DateInterval(null, null));
        }
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


    public File getOutputFile() { return outputFile; }

    public void setOutputFile(File outputFile) { this.outputFile = outputFile; }

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
