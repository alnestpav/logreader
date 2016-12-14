package ru.siblion.nesterov.logreader.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexander on 14.12.2016.
 */
public class FileSearcher {

    public final static String DOMAIN_NAME = "webl_domain";
    public final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\" + DOMAIN_NAME + "\\";

    private static List<String> getDomainLogFiles() {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(DOMAIN_DIRECTORY + "config\\config.xml");

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
            e.printStackTrace();
        }

        File serverLogDirectory;
        List<String> logFiles = new ArrayList<>();
        for (String server : servers) {
            serverLogDirectory = new File(DOMAIN_DIRECTORY + "servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*|webl_domain.log[0-9]*");
            List<String> listOfLogFiles = getFilesMatching(serverLogDirectory, LogFileRegExp);
            logFiles.addAll(listOfLogFiles);
        }
        return logFiles;
    }

    private static List<String> getClusterLogFiles(String clusterName) {
        List<String> servers = new ArrayList<>();
        try {
            File domainConfigFile = new File(DOMAIN_DIRECTORY + "config\\config.xml");

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
            e.printStackTrace();
        }
        File serverLogDirectory;
        List<String> logFiles = new ArrayList<>();
        for (String server : servers) {
            serverLogDirectory = new File(DOMAIN_DIRECTORY + "servers\\" + server + "\\logs\\");
            String LogFileRegExp = (server + ".log[0-9]*");
            List<String> listOfLogFiles = getFilesMatching(serverLogDirectory, LogFileRegExp);
            logFiles.addAll(listOfLogFiles);
        }
        return logFiles;

    }

    private static List<String> getFilesMatching(File root, String regExp) {
        if (!root.isDirectory()) {
            throw new IllegalArgumentException(root + " это не директория.");
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

    public static List<String> getLogFiles(String location) throws Exception {

         /* Сначала проверяем, является ли местоположение location каким-либо сервером */
        String serverName = location;
        File serverLogDirectory = new File(DOMAIN_DIRECTORY + "servers\\" + serverName + "\\logs\\");
        if (serverLogDirectory.exists()) {
            List<String> logFiles = new ArrayList<>();
            for (String logFile : getFilesMatching(serverLogDirectory, (serverName + ".log[0-9]*"))) {
                logFiles.add(logFile);
            }
            return logFiles;
        }

        /* Проверяем, является ли местоположение location каким-либо кластером */
        Pattern clusterPattern = Pattern.compile("webl_cluster[0-9]+");
        Matcher clusterMatcher = clusterPattern.matcher(location);
        if (clusterMatcher.matches()) {
            return getClusterLogFiles(location);
        }

        /* Проверяем, является ли местоположение location доменом */
        if (location.equals(DOMAIN_NAME)) {
            return getDomainLogFiles();
        } else {
            throw new Exception();
        }
    }

}
