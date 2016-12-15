package ru.siblion.nesterov.logreader.type;

import java.util.List;

/**
 * Created by alexander on 15.12.2016.
 */
public class LogFile {
    private String filePath;
    private List<Integer> positionsOfString;
    private List<Integer> prefixPositions;

    public LogFile(String filePath) {
        this.filePath = filePath;
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
        this.positionsOfString = positionsOfString;
    }

    public List<Integer> getPrefixPositions() {
        return prefixPositions;
    }

    public void setPrefixPositions(List<Integer> prefixPositions) {
        this.prefixPositions = prefixPositions;
    }

    @Override
    public String toString() {
        return "FilePath: " + filePath + " \n"
                + "positionsOfString: " + positionsOfString + "\n"
                + "prefixPositions: " + prefixPositions + "\n";
    }


}
