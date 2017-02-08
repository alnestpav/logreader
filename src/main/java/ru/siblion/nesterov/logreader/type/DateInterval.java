package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.AppLogger;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 15.12.2016.
 */

/* Класс, инкапсулирующий интервал дат, используется в классе Request */
@XmlRootElement(name = "DateInterval")
public class DateInterval {

    private XMLGregorianCalendar dateFrom;
    private XMLGregorianCalendar dateTo;

    private static final Logger logger = AppLogger.getLogger();

    public DateInterval(XMLGregorianCalendar dateFrom, XMLGregorianCalendar dateTo) {
        logger.log(Level.INFO, "dateFrom=" + dateFrom + "\n" + "dateTo=" + dateTo);
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DateInterval() {}

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


    /* Метод раньше был в LogReader, но он достаточно общий, поэтому перенес в Utils,
     * а оттуда сюда, так как он логично вписывается в метод созданого класса DateInterval */
    public boolean containsDate(XMLGregorianCalendar date) {

        if (date == null) return false;

        if (dateFrom == null && dateTo == null) return true;

        if (dateFrom == null && date.compare(dateTo) <= 0) return true;

        if (dateTo == null && date.compare(dateFrom) >= 0) return true;

        if (date.compare(dateFrom) >= 0 && date.compare(dateTo) <= 0 ) return true;

        return false;
    }

    @Override
    public String toString() {
        return "DateFrom: " + dateFrom + ", DateTo: " + dateTo;
    }
}
