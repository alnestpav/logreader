package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 15.12.2016.
 */
@XmlRootElement(name = "DateInterval")
public class DateInterval {

    private XMLGregorianCalendar dateFrom;
    private XMLGregorianCalendar dateTo;

    private static final Logger logger = MyLogger.getLogger();

    public DateInterval(XMLGregorianCalendar dateFrom, XMLGregorianCalendar dateTo) {
        logger.log(Level.INFO, "dateFrom=" + dateFrom + "\n" + "dateTo=" + dateTo);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DateInterval() { // Нужен для JAXB

    }
    @XmlElement(name = "dateFrom")
    public XMLGregorianCalendar getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(XMLGregorianCalendar dateFrom) {
        this.dateFrom = dateFrom;
    }

    @XmlElement(name = "dateTo")
    public XMLGregorianCalendar getDateTo() {
        return dateTo;
    }

    public void setDateTo(XMLGregorianCalendar dateTo) {
        this.dateTo = dateTo;
    }

    @Override
    public String toString() {
        return "DateFrom: " + dateFrom + ", DateTo: " + dateTo;
    }
}
