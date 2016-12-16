package ru.siblion.nesterov.logreader.type;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Created by alexander on 15.12.2016.
 */
@XmlRootElement(name = "DateInterval")
public class DateInterval {
    private XMLGregorianCalendar dateFrom;
    private XMLGregorianCalendar dateTo;

    public DateInterval(XMLGregorianCalendar dateFrom, XMLGregorianCalendar dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }


    public DateInterval() { // Нужен для JAXB

    }

    public XMLGregorianCalendar getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(XMLGregorianCalendar dateFrom) {
        this.dateFrom = dateFrom;
    }

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
