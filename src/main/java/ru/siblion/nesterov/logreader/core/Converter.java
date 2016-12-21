package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
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

    public static void convert(Object jaxbObject, FileFormat fileFormat, File file) throws JAXBException {

        StreamResult streamResult;
            if (fileFormat == fileFormat.xml) {
                streamResult = new StreamResult(file);
                JaxbParser.saveObject(jaxbObject, streamResult);
                return;
        } else {
            StringWriter writer = new StringWriter();
            streamResult = new StreamResult(writer);
            JaxbParser.saveObject(jaxbObject, streamResult);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = null;
            try {
                transformer = factory.newTransformer(new StreamSource(xslFile));
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            }
            ;
            Source xmlSource = new StreamSource(new StringReader(writer.toString()));
            StreamResult output = new StreamResult(file);
            try {
                transformer.transform(xmlSource, output);
            } catch (TransformerException e) {
                e.printStackTrace();
            }
        }
    }
}
