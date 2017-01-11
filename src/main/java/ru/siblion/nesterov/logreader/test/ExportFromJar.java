package ru.siblion.nesterov.logreader.test;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by alexander on 11.01.2017.
 */

@Startup
@Singleton
public class ExportFromJar {
    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public void exportResource(String resourceName) throws Exception {
        InputStream file = ExportFromJar.class.getResourceAsStream(resourceName);
        Files.copy(file, Paths.get("C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain\\xsl\\pdf.xsl"));
    }
}
