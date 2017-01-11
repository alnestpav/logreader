package ru.siblion.nesterov.logreader.core;

import org.apache.fop.apps.FOPException;
import ru.siblion.nesterov.logreader.test.ExportFromJar;
import ru.siblion.nesterov.logreader.type.Config;
import ru.siblion.nesterov.logreader.type.FileFormat;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;
import ru.siblion.nesterov.logreader.util.JaxbParser;
import ru.siblion.nesterov.logreader.util.MyLogger;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by alexander on 19.12.2016.
 */

/*  Класс для записи объекта в файл */
public class ObjectToFileWriter {
    private final static File configFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\config\\logreader.xml");
    private Config config;
    private Object object;

    private static final Logger logger = MyLogger.getLogger();

    public ObjectToFileWriter(Object object) {
        this.object = object;
        logger.log(Level.INFO, "Starting Export from Jar");
        try {
            ExportFromJar.exportResource("doc.xsl");
            logger.log(Level.INFO, "Export from Jar!!!!!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readConfig() {
        try {
            config = (Config) JaxbParser.xmlToObject(configFile, new Config()); // второй параметр возможно нужно поменять в сигнатуре метода
        } catch (JAXBException e) {
            e.printStackTrace();
        }
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
        readConfig();
        Converter converter = new Converter(config);
        try {
            converter.convert(object, FileFormat.doc, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void writeHtml(Object object, File file) {
        readConfig();
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
        readConfig();
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
        readConfig();
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
