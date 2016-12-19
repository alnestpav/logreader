package ru.siblion.nesterov.logreader.core;

import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;
import ru.siblion.nesterov.logreader.util.JaxbList;
import ru.siblion.nesterov.logreader.util.JaxbParser;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created by alexander on 19.12.2016.
 */
public class LogFileWriter {

    public static void write(List<LogMessage> logMessagesList, FileFormat fileFormat) {
        String directory = "C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\";
        File file = new File(directory + "my_file." + fileFormat);


        try (FileWriter fw = new FileWriter(file)) {
            LogMessages logMessages = new LogMessages();
            logMessages.setLogMessages(logMessagesList);

                switch(fileFormat) {
                    case DOC:
                        break;
                    case LOG:
                        break;
                    case PDF:
                        break;
                    case RTF:
                        break;
                    case TXT:
                        break;
                    case XML: JaxbParser.saveObject(file, logMessages);
                        break;
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
