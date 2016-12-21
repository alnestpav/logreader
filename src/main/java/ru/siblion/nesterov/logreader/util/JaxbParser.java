package ru.siblion.nesterov.logreader.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by alexander on 19.12.2016.
 */
public class JaxbParser {
    public static void saveObject(Object o, StreamResult streamResult) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        System.out.println(o);
        jaxbMarshaller.marshal(o, streamResult);
    }
}
