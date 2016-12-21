package ru.siblion.nesterov.logreader.core;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by alexander on 21.12.2016.
 */
public class Converter {
    private final static String DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";
    private static final File xslFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\test.xsl");

    public static void marshal(Object jaxbObject) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(jaxbObject.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        marshaller.marshal(jaxbObject, streamResult);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer(new StreamSource(xslFile));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        };
        Source xmlSource = new StreamSource(new StringReader(writer.toString()));
        File htmlFile = new File(DIRECTORY + "test.html");
        StreamResult output = new StreamResult(htmlFile);
        try {
            transformer.transform(xmlSource, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
