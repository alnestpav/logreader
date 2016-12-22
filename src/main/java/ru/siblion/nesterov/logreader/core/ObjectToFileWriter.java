package ru.siblion.nesterov.logreader.core;

import org.apache.fop.apps.FOPException;
import ru.siblion.nesterov.logreader.test.FopConverter;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Created by alexander on 19.12.2016.
 */
public class ObjectToFileWriter {
    private Object object;
    private final static String DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";

    public ObjectToFileWriter(Object object) {
        this.object = object;
    }

    public void write(Object object, FileFormat fileFormat) {
        File file = new File(DIRECTORY + "export." + fileFormat);

        try (FileWriter fw = new FileWriter(file)) {
            switch(fileFormat) {
                case doc: writeDoc(object, file);
                    break;
                case html: writeHtml(object, file);
                    break;
                case log: writeLog(object, file);
                    break;
                case pdf: writePdf(object, file);
                    break;
                case rtf: writeRtf(object, file);
                    break;
                case xml: writeXml(object, file);
                    break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDoc(Object object, File file) {
        try {
            Converter.convert(object, FileFormat.doc, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeHtml(Object object, File file) {
        try {
            Converter.convert(object, FileFormat.html, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeLog(Object object, File file) {
        try (FileWriter logFileWriter = new FileWriter(file)) {
            LogMessages logMessages = (LogMessages) object;
            for (LogMessage logMessage : logMessages.getLogMessages()) {
                logFileWriter.write(logMessage.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePdf(Object object, File file) {
        File xmlFile = new File("temp\\tempLogFile.xml");
        writeXml(object, xmlFile);
        try {
            FopConverter.convertTo(xmlFile, FileFormat.pdf, file);
            xmlFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void writeRtf(Object object, File file) {
        File xmlFile = new File("temp\\tempLogFile.xml");
        writeXml(object, xmlFile);
        try {
            FopConverter.convertTo(xmlFile, FileFormat.rtf, file);
            xmlFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    private void writeXml(Object object, File file) {
        StreamResult streamResult;
        streamResult = new StreamResult(file);
        try {
            JaxbParser.saveObject(object, streamResult);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
