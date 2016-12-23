package ru.siblion.nesterov.logreader.test;
import java.io.*;

import javax.xml.bind.JAXBException;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import ru.siblion.nesterov.logreader.core.FileFormat;
import ru.siblion.nesterov.logreader.util.JaxbParser;

public class FopConverter {
    private static final String XSL_DIRECTORY = "C:\\Users\\alexander\\IdeaProjects\\logreader\\xsl\\";
    /**
     * Method that will convert the given XML to PDF
     * @throws IOException
     * @throws FOPException
     * @throws TransformerException
     */
    public static void convert(Object jaxbObject, FileFormat fileFormat, File file) throws IOException, FOPException,
            TransformerException, JAXBException {
        StringWriter writer = new StringWriter();
        StreamResult streamResult = new StreamResult(writer);
        JaxbParser.objectToXml(jaxbObject, streamResult);

        Source xmlSource = new StreamSource(new StringReader(writer.toString()));
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        OutputStream out = null;
        File xslFile = null;
        String fopOutputFormat = null;
        switch(fileFormat) {
            case pdf: {
                out = new FileOutputStream(file);
                fopOutputFormat = MimeConstants.MIME_PDF;
                xslFile = new File(XSL_DIRECTORY + "pdf.xsl");
                break;
            }
            case rtf: {
                out = new FileOutputStream(file);
                fopOutputFormat = MimeConstants.MIME_RTF;
                xslFile = new File(XSL_DIRECTORY + "pdf.xsl"); //  rtf шаблон сделать
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