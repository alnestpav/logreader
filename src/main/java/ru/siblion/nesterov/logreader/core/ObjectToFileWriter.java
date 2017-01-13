package ru.siblion.nesterov.logreader.core;

import org.apache.fop.apps.FOPException;
import ru.siblion.nesterov.logreader.util.ExportFromArchive;
import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.type.FileFormat;
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

/*  Класс для записи объекта в файл */
public class ObjectToFileWriter {
    //private final static String DOMAIN_DIRECTORY = (new File("").getAbsolutePath()); // если запускать на сервере
    private final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test
    private final static File configFile = new File(DOMAIN_DIRECTORY + "\\logreader\\config\\config.xml");
    private static Config config;
    private Object object;

    public ObjectToFileWriter(Object object) {
        this.object = object;
        ExportFromArchive.exportXsls();
        config = Config.getConfig(configFile);
    }

    public void write(FileFormat fileFormat, File file) {
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
    }

    private void writeDoc(Object object, File file) {
        Converter converter = new Converter(config);
        try {
            converter.convert(object, FileFormat.doc, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeHtml(Object object, File file) {
        Converter converter = new Converter(config);
        try {
            converter.convert(object, FileFormat.html, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeLog(Object object, File file) {
        try (FileWriter logFileWriter = new FileWriter(file)) {
            LogMessages logMessages = (LogMessages) object;
            logFileWriter.write(logMessages.getRequest().toString() + "\n");
            for (LogMessage logMessage : logMessages.getLogMessages()) {
                logFileWriter.write(logMessage.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writePdf(Object object, File file) {
        FopConverter fopConverter = new FopConverter(config);
        try {
            fopConverter.convert(object, FileFormat.pdf, file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeRtf(Object object, File file) {
        FopConverter fopConverter = new FopConverter(config);
        try {
            fopConverter.convert(object, FileFormat.rtf, file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FOPException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeXml(Object object, File file) {
        StreamResult streamResult;
        streamResult = new StreamResult(file);
        try {
            JaxbParser.objectToXml(object, streamResult);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
