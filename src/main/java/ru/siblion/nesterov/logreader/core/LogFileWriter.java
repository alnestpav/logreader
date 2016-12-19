package ru.siblion.nesterov.logreader.core;

import org.apache.fop.apps.FOPException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.siblion.nesterov.logreader.test.FopConverter;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.List;

/**
 * Created by alexander on 19.12.2016.
 */
public class LogFileWriter {
    private final static String DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";
    public static void write(List<LogMessage> logMessageList, FileFormat fileFormat) {
        File file = new File(DIRECTORY + "my_file." + fileFormat);

        try (FileWriter fw = new FileWriter(file)) {
                switch(fileFormat) {
                    case doc: writeDoc(logMessageList, file);
                        break;
                    case log: writeLog(logMessageList, file);
                        break;
                    case pdf: writePdf(logMessageList, file);
                        break;
                    case rtf: writeRtf(logMessageList, file);
                        break;
                    case txt: writeTxt(logMessageList, file);
                        break;
                    case xml: writeXml(logMessageList, file);
                        break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeXml(List<LogMessage> logMessageList, File file) {
        LogMessages logMessages = new LogMessages();
        logMessages.setLogMessages(logMessageList);
        try {
            JaxbParser.saveObject(file, logMessages);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static void writePdf(List<LogMessage> logMessageList, File file) {
        File xmlFile = new File("temp\\tempLogFile.xml");
        writeXml(logMessageList, xmlFile);

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

    private static void writeRtf(List<LogMessage> logMessageList, File file) {
        File xmlFile = new File("temp\\tempLogFile.xml");
        writeXml(logMessageList, xmlFile);

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


    private static void writeTxt(List<LogMessage> logMessageList, File file) {
        try (FileWriter logFileWriter = new FileWriter(file)) {
            for (LogMessage logMessage : logMessageList) {
                logFileWriter.write(logMessage.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeLog(List<LogMessage> logMessageList, File file) {
        writeTxt(logMessageList, file);
    }

    private static void writeDoc(List<LogMessage> logMessageList, File file) {
        XWPFDocument document= new XWPFDocument();
        try (FileOutputStream out = new FileOutputStream(file)) {
            XWPFParagraph paragraph;
            XWPFRun run;
            for (LogMessage logMessage : logMessageList) {
                paragraph = document.createParagraph();
                run = paragraph.createRun();
                run.setText(logMessage.toString());
            }
            document.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
