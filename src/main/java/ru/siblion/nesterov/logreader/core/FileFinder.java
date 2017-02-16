package ru.siblion.nesterov.logreader.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.siblion.nesterov.logreader.util.AppConfig;
import ru.siblion.nesterov.logreader.type.LocationType;
import ru.siblion.nesterov.logreader.util.AppLogger;
import ru.siblion.nesterov.logreader.util.Utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 14.12.2016.
 */

/* Класс для поиска лог-файлов WebLogic */
public class FileFinder {

    private String domainDirectory;
    private String domainName;

    private static final Logger logger = AppLogger.getLogger();

    public FileFinder() {
        domainDirectory = AppConfig.DOMAIN_DIRECTORY;
        Pattern domainPattern = Pattern.compile("\\w+$");
        Matcher domainMatcher = domainPattern.matcher(domainDirectory);
        if (domainMatcher.find()) {
            domainName = domainMatcher.group(); // переписать, используя  в регулярном выражении
        }

    }

    private Set<String> getDomainLogFiles() {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(domainDirectory + "\\config\\appConfig.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(domainConfigFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(("server"));

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    servers.add(eElement.getElementsByTagName("name").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при парсинге appConfig.xml", e) ;
        }

        File serverLogDirectory;
        Set<String> logFiles = new HashSet<>();
        for (String server : servers) {
            serverLogDirectory = new File(domainDirectory + "\\servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*|" + domainName + ".log[0-9]*");
            List<String> listOfLogFiles = Utils.getFilesMatching(serverLogDirectory, LogFileRegExp);
            logFiles.addAll(listOfLogFiles);
        }
        return logFiles;
    }

    private Set<String> getClusterLogFiles(String clusterName) {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(domainDirectory + "\\config\\appConfig.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(domainConfigFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName(("server"));

            for (int i = 1; i < nList.getLength(); i++) { // в случае кластера нас не интересует administration server (i = 0)
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getElementsByTagName("cluster").item(0).getTextContent().equals(clusterName)) {
                        servers.add(eElement.getElementsByTagName("name").item(0).getTextContent());
                    }
                }
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Ошибка при парсинге appConfig.xml", e) ;
        }
        File serverLogDirectory;
        Set<String> logFiles = new HashSet<>();
        for (String server : servers) {
            serverLogDirectory = new File(domainDirectory + "\\servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*");
            List<String> listOfLogFiles = Utils.getFilesMatching(serverLogDirectory, LogFileRegExp);
            logFiles.addAll(listOfLogFiles);
        }
        return logFiles;

    }

    private  Set<String> getServerLogFiles(String location) {
        Set<String> logFiles = new HashSet<>();
        String serverName = location;
        File serverLogDirectory = new File(domainDirectory + "\\servers\\" + serverName + "\\logs\\");
        if (serverLogDirectory.exists()) {
            logFiles.addAll(Utils.getFilesMatching(serverLogDirectory, (serverName + ".log[0-9]*")));
        }
        return logFiles;
    }

    public  Set<String> getLogFiles(LocationType locationType, String location) {
        switch(locationType) {
            case DOMAIN: return getDomainLogFiles();
            case CLUSTER: return getClusterLogFiles(location);
            case SERVER: return getServerLogFiles(location);
            default: return null;
        }
    }


}
