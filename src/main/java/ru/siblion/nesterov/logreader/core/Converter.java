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

/* Класс для конвертации объекта в документ, используется для doc и html */
public class Converter {
    private Config config;

    public Converter(Config config) {
        this.config = config;
    }

    public void convert(Object jaxbObject, FileFormat fileFormat, File file) throws JAXBException {
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
