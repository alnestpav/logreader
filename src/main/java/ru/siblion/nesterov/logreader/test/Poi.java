package ru.siblion.nesterov.logreader.test;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import ru.siblion.nesterov.logreader.type.LogMessage;
import ru.siblion.nesterov.logreader.type.LogMessages;

/**
 * Created by alexander on 19.12.2016.
 */
public class Poi {
    public static void writeDoc(List<LogMessage> logMessageList, File file) throws IOException {
        //Blank Document
        XWPFDocument document= new XWPFDocument();
        //Write the Document in file system
        FileOutputStream out = new FileOutputStream(file);
        //create Paragraph
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        LogMessages logMessages = new LogMessages();
        logMessages.setLogMessages(logMessageList);
        run.setText(logMessages.toString());
        document.write(out);
        out.close();
    }
}
