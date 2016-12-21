package ru.siblion.nesterov.logreader.test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import ru.siblion.nesterov.logreader.core.FileFormat;

public class FopConverter {
    private static final File xslFile = new File("C:\\Users\\alexander\\IdeaProjects\\logreader\\temp\\pdf.xsl");
    /**
     * Method that will convert the given XML to PDF
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    public static void convertTo(File xmlFile, FileFormat fileFormat, File file)  throws IOException, FOPException, TransformerException {
        StreamSource xmlSource = new StreamSource(xmlFile);
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out = null;

        String fopOutputFormat = null;
        switch(fileFormat) {
            case pdf: {
                out = new FileOutputStream(file);
                fopOutputFormat = MimeConstants.MIME_PDF;
                break;
            }
            case rtf: {
                out = new FileOutputStream(file);
                fopOutputFormat = MimeConstants.MIME_RTF;
                break;
            }
        }
        try {
            Fop fop = fopFactory.newFop(fopOutputFormat, foUserAgent, out);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xslFile));

            Result res = new SAXResult(fop.getDefaultHandler());

            transformer.transform(xmlSource, res);
        } finally {
            out.close();
        }
    }
}