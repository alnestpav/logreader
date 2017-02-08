package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.AppConfig;
import ru.siblion.nesterov.logreader.type.FileFormat;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Properties;

/**
 * Created by alexander on 21.12.2016.
 */

/* Класс для конвертации объекта в документ, используется для doc и html */
public class Converter {
    private static final Properties appConfigProperties = AppConfig.getInstance().getProperties();

    public Converter() {}

    public void convert(Object jaxbObject, FileFormat fileFormat, File file) throws JAXBException {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        JaxbParser.objectToXml(jaxbObject, streamResult);


        File xslFile = null;
        switch (fileFormat) {
            case doc: xslFile = new File(appConfigProperties.getProperty("doc-template"));
                break;
            case html: xslFile = new File(appConfigProperties.getProperty("html-template"));
                break;
        }
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile));
            Source xmlSource = new StreamSource(new StringReader(writer.toString()));
            StreamResult output = new StreamResult(file);
            transformer.transform(xmlSource, output);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
