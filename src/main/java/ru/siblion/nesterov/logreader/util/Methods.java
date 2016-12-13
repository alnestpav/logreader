package ru.siblion.nesterov.logreader.util;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.*;
import ru.siblion.nesterov.logreader.type.LogMessage;

import javax.xml.parsers.*;

/**
 * Created by alexander on 06.12.2016.
 */
public class Methods {

    public final static String DOMAIN_NAME = "webl_domain";
    public final static String DOMAIN_DIRECTORY = "C:\\Oracle\\Middleware\\Oracle_Home\\user_projects\\domains\\" + DOMAIN_NAME + "\\";

    public static List<Integer> getPositionsOfLinesWithString(String string, String filePath) {
        List<Integer> linesWithStringNumbers = new ArrayList<>();

        String command = "findstr /n /c:" + "\"" + string + "\"" + " " + filePath ;
        try {
            Process findstrProcess = Runtime.getRuntime().exec(command);
            InputStream findstrProcessInputStream = findstrProcess.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(findstrProcessInputStream));
            String line = reader.readLine();
            Pattern lineNumberPattern = Pattern.compile("\\d+");
            Matcher lineNumberMatcher;
            while (line != null) {
                lineNumberMatcher = lineNumberPattern.matcher(line);
                lineNumberMatcher.find();
                String lineNumberString = lineNumberMatcher.group();
                linesWithStringNumbers.add(Integer.parseInt(lineNumberString));
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return linesWithStringNumbers;
    }

    public static String getBlock(String filePath, int fromLineNumber, int toLineNumber)  {
        System.out.println("getBlock(" + filePath + ", " + fromLineNumber + ", " + toLineNumber + ")");
        StringBuilder block = new StringBuilder();

        try(FileReader fileReader = new FileReader(filePath);
            LineNumberReader lineNumberReader = new LineNumberReader(fileReader)){

            for (int i = 1; i < fromLineNumber; i++) {
                lineNumberReader.readLine();
            }
            for (int i = fromLineNumber; i <= toLineNumber; i++) {
                block.append(lineNumberReader.readLine());
            }

        } catch(IOException e){
            e.printStackTrace();
        }

        return block.toString();
    }

    public static Map<Integer, Integer> getBlockPositions(List<Integer> positionsOfLinesWithString, List<Integer> prefixPositions) {
        Map<Integer, Integer> blockPositions = new TreeMap<>();
        int start = 0;
        int end = 0;
        for (int i = 0; i < positionsOfLinesWithString.size(); i++) {
            for (int j = 0; j < prefixPositions.size(); j++) {
                if (positionsOfLinesWithString.get(i) >= prefixPositions.get(j)
                        && j + 1 < prefixPositions.size()
                        && positionsOfLinesWithString.get(i) < prefixPositions.get(j + 1)) {

                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j + 1) - 1;
                    blockPositions.put(start, end);
                    break;
                }
                if (j + 1 == prefixPositions.size()) {
                    start =  prefixPositions.get(j);
                    end =  prefixPositions.get(j);
                    blockPositions.put(start, end);
                }
            }
        }
        return blockPositions;
    }

    public static List<String> getLogFilePaths(String location) {
        List<String> filePaths = new ArrayList<String>();

        String serverName = location;
        File directory = new File(DOMAIN_DIRECTORY + "servers\\" + serverName + "\\logs\\");
        if (directory.exists()) {
            for (String logFilePath : Methods.getListFilesMatching(directory, (serverName + ".log[0-9]*"))) {
                filePaths.add(logFilePath);
            }
            return filePaths;
        }
        List<String> servers = new ArrayList<String>();

        if (location == DOMAIN_NAME) {
            try {
                File configFile = new File(DOMAIN_DIRECTORY + "config\\config.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(configFile);
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
        }

        Pattern p = Pattern.compile("webl_cluster[0-9]+");
        Matcher m = p.matcher(location);
        if (m.matches()) {
            try {
                File configFile = new File(DOMAIN_DIRECTORY + "config\\config.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(configFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName(("server"));
                for (int i = 1; i < nList.getLength(); i++) {
                    Node nNode = nList.item(i);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        if (eElement.getElementsByTagName("cluster").item(0).getTextContent().equals(location)) {
                            servers.add(eElement.getElementsByTagName("name").item(0).getTextContent());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File serverLogDirectory;
        for (String server : servers) {
            serverLogDirectory = new File(DOMAIN_DIRECTORY + "servers\\" + server + "\\logs\\");
            String regExp = (server + ".log[0-9]*|webl_domain.log[0-9]*");
            for (String logFilePath : Methods.getListFilesMatching(serverLogDirectory, regExp)) {
                filePaths.add(logFilePath);
            }
        }
        return filePaths;
    }

    public static List<String> getListFilesMatching(File root, String regex) {
        if(!root.isDirectory()) {
            throw new IllegalArgumentException(root + " is no directory.");
        }
        final Pattern p = Pattern.compile(regex); // careful: could also throw an exception!
        File[] files = root.listFiles(new FileFilter(){
            @Override
            public boolean accept(File file) {
                return p.matcher(file.getName()).matches();
            }
        });
        List<String> filesMatching = new ArrayList<String>();
        for (File file : files) {
            filesMatching.add(file.toString());
        }
        return filesMatching;
    }

    public static List<LogMessage> getLogMessageList(String string, String location) {

        List<Integer> positionsOfLinesWithString;
        List<Integer> prefixPositions;
        Map<Integer, Integer> blockPositions;

        List<String> logFilePaths = Methods.getLogFilePaths(location);
        List<LogMessage> logMessageList = new ArrayList<LogMessage>();
        for (String logFilePath : logFilePaths) {
            positionsOfLinesWithString = Methods.getPositionsOfLinesWithString(string, logFilePath);
            prefixPositions = Methods.getPositionsOfLinesWithString("####", logFilePath);
            blockPositions = Methods.getBlockPositions(positionsOfLinesWithString, prefixPositions);

            String currentBlock;
            for (Map.Entry<Integer, Integer> entry : blockPositions.entrySet()) {
                currentBlock = (Methods.getBlock(logFilePath, entry.getKey(), entry.getValue())).substring(4);
                LogMessage logMessage = new LogMessage(currentBlock);
                logMessageList.add(logMessage);
            }
        }
        return logMessageList;
    }

}