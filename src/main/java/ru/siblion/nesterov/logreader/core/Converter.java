package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.type.FileFormat;
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
    private final static File configFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\config\\logreader.xml");
    private static Config config;

    public static void convert(Object jaxbObject, FileFormat fileFormat, File file) throws JAXBException {

        try {
            config = (Config) JaxbParser.xmlToObject(configFile, new Config()); // второй параметр возможно нужно поменять в сигнатуре метода
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        JaxbParser.objectToXml(jaxbObject, streamResult);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;

        File xslFile = null;
        switch (fileFormat) {
            case doc: xslFile = config.getDocTemplate();
                break;
            case html: xslFile = config.getHtmlTemplate();
                break;
            case rtf: xslFile = config.getRtfTemplate();
                break;
        }
        try {
            transformer = factory.newTransformer(new StreamSource(xslFile));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        Source xmlSource = new StreamSource(new StringReader(writer.toString()));
        StreamResult output = new StreamResult(file);
        try {
            transformer.transform(xmlSource, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
