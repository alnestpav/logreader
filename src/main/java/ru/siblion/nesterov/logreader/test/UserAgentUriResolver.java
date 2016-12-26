package ru.siblion.nesterov.logreader.test;

/**
 * Created by alexander on 26.12.2016.
 */
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class UserAgentUriResolver implements URIResolver {

    private static final String USER_AGENT = "whatever";

    @Override
    public Source resolve(String href, String base) throws TransformerException {
        try {
            URL url = new URL(href);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            return new StreamSource(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}