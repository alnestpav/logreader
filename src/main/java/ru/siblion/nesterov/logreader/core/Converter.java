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
    private static final String DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";

    private static final File xslFileForDoc = new File(DIRECTORY + "doc.xsl");
    private static final File xslFileForHtml = new File(DIRECTORY + "html.xsl");
    private static final File xslFileForRtf = new File(DIRECTORY + "rtf.xsl");

    public static void convert(Object jaxbObject, FileFormat fileFormat, File file) throws JAXBException {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        JaxbParser.objectToXml(jaxbObject, streamResult);
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;

        File xslFile = null;
        switch (fileFormat) {
            case doc: xslFile = xslFileForDoc;
                break;
            case html: xslFile = xslFileForHtml;
                break;
            case rtf: xslFile = xslFileForRtf;

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
