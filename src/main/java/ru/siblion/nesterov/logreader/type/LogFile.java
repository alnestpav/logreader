package ru.siblion.nesterov.logreader.type;

import ru.siblion.nesterov.logreader.util.MyLogger;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by alexander on 15.12.2016.
 */
public class LogFile {
    private String filePath;
    private List<Integer> positionsOfString;
    private List<Integer> prefixPositions;

    private static final Logger logger = MyLogger.getLogger();

    public LogFile(String filePath) {
        this.filePath = filePath;
    }
    public LogFile(LogFile logFile) {
        this.filePath = logFile.getFilePath();
        this.positionsOfString = logFile.getPositionsOfString();
        this.prefixPositions = logFile.getPrefixPositions();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Integer> getPositionsOfString() {
        return positionsOfString;
    }

    public void setPositionsOfString(List<Integer> positionsOfString) {
        logger.log(Level.INFO, "setPositionsOfString (( " + positionsOfString + "))");
        this.positionsOfString = new ArrayList<>();
        this.positionsOfString.addAll(positionsOfString);
    }

    public List<Integer> getPrefixPositions() {
        return prefixPositions;
    }

    public void setPrefixPositions(List<Integer> prefixPositions) {
        logger.log(Level.INFO, "setPrefixPositions (( " + prefixPositions + "))");
        this.prefixPositions = new ArrayList<>();
        this.prefixPositions.addAll(prefixPositions);
    }

    @Override
    public String toString() {
        return "FilePath: " + filePath + " \n"
                + "PositionsOfString: " + positionsOfString + "\n"
                + "PrefixPositions: " + prefixPositions + "\n";
    }

}
