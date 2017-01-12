package ru.siblion.nesterov.logreader.util;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by alexander on 19.12.2016.
 */

/* Класс для конвертирования объекта в xml-представление и конвертирования xml-файла в объект */
public class JaxbParser {
    public static void objectToXml(Object o, StreamResult streamResult) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println(o);
        jaxbMarshaller.marshal(o, streamResult);
    }

    public static Object xmlToObject(File xmlFile, Object o) throws JAXBException { // второй параметр возможно нужно поменять в сигнатуре метода
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        o = jaxbUnmarshaller.unmarshal(xmlFile);
        return o;
    }
}
