package ru.siblion.nesterov.logreader.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * Created by alexander on 19.12.2016.
 */
public class JaxbParser {
    public static void saveObject(File file, Object o) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        System.out.println(o);
        jaxbMarshaller.marshal(o, file);
    }
}
