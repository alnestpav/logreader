package ru.siblion.nesterov.logreader.test;

import ru.siblion.nesterov.logreader.core.ObjectToFileWriter;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;

import java.io.File;

import static ru.siblion.nesterov.logreader.type.FileFormat.pdf;

/**
 * Created by alexander on 29.12.2016.
 */
public class TestPdf {
    public static void main(String[] args) {
        LogMessage logMessage = new LogMessage();
        ObjectToFileWriter objectToFileWriter = new ObjectToFileWriter(logMessage);
        objectToFileWriter.write(pdf, new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\test.pdf"));
    }
}
