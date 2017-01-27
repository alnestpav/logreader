package ru.siblion.nesterov.logreader.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.siblion.nesterov.logreader.type.LocationType;
import ru.siblion.nesterov.logreader.util.MyLogger;
import ru.siblion.nesterov.logreader.type.LogFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 14.12.2016.
 */

/* Класс для поиска лог-файлов WebLogic */
public class FileSearcher {

    private String domainDirectory;
    private String domainName;

    private static final Logger logger = MyLogger.getLogger();

    public FileSearcher() {
        domainDirectory = (new File("").getAbsolutePath()); // если запускать на сервере
        //domainDirectory = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\webl_domain"; // если запускать в Test
        Pattern domainPattern = Pattern.compile("\\\\\\w+$");
        Matcher domainMatcher = domainPattern.matcher(domainDirectory);
        domainMatcher.find();
        domainName = domainMatcher.group().substring(1);
        System.out.println(domainDirectory);
        System.out.println(domainName);
    }

    public FileSearcher(String domainDirectory) {
        this.domainDirectory = domainDirectory;
    }

    private List<LogFile> getDomainLogFiles() {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(domainDirectory + "\\config\\config.xml");

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
            logger.log(Level.SEVERE, "Ошибка при парсинге config.xml", e) ;
        }

        File serverLogDirectory;
        List<LogFile> logFiles = new ArrayList<>();
        for (String server : servers) {
            serverLogDirectory = new File(domainDirectory + "\\servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*|" + domainName + ".log[0-9]*");
            List<String> listOfLogFiles = getFilesMatching(serverLogDirectory, LogFileRegExp);
            for (String logFilePath : listOfLogFiles) {
                logFiles.add(new LogFile(logFilePath));
            }
        }
        return logFiles;
    }

    private List<LogFile> getClusterLogFiles(String clusterName) {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(domainDirectory + "\\config\\config.xml");

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
            logger.log(Level.SEVERE, "Ошибка при парсинге config.xml", e) ;
        }
        File serverLogDirectory;
        List<LogFile> logFiles = new ArrayList<>();
        for (String server : servers) {
            serverLogDirectory = new File(domainDirectory + "\\servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*");
            List<String> listOfLogFiles = getFilesMatching(serverLogDirectory, LogFileRegExp);
            for (String logFilePath : listOfLogFiles) {
                logFiles.add(new LogFile(logFilePath));
            }
        }
        return logFiles;

    }


    private List<LogFile> getServerLogFiles(String location) {
        String serverName = location;
        File serverLogDirectory = new File(domainDirectory + "\\servers\\" + serverName + "\\logs\\");
        if (serverLogDirectory.exists()) {
            List<LogFile> logFiles = new ArrayList<>();
            for (String logFilePath : getFilesMatching(serverLogDirectory, (serverName + ".log[0-9]*"))) {
                logFiles.add(new LogFile(logFilePath));
            }
            return logFiles;
        }
        return null;
    }

    /* Возможно стоит перенести метод в класс Utils, так как он также используется в классе Request */
    public List<String> getFilesMatching(File root, String regExp) {
        try {
            if (!root.isDirectory()) {
                throw new IllegalArgumentException(root + " это не директория.");
            }
        } catch(IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Ошибка при поиске файлов в некоторой папке", e) ;
        }
        final Pattern p = Pattern.compile(regExp);

        File[] files = root.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return p.matcher(file.getName()).matches();
            }
        });

        List<String> filesMatching = new ArrayList<>();
        for (File file : files) {
            filesMatching.add(file.toString());
        }
        return filesMatching;
    }

    public List<LogFile> getLogFiles(LocationType locationType, String location) throws Exception {
        System.out.println("LocationType: " + locationType);
        switch(locationType) {
            case domain: return getDomainLogFiles();
            case cluster: return getClusterLogFiles(location);
            case server: return getServerLogFiles(location);
            default: return null;
        }
    }


}
