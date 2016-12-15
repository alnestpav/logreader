package ru.siblion.nesterov.logreader.type;

import java.util.List;

/**
 * Created by alexander on 15.12.2016.
 */
public class LogFile {
    private String filePath;
    private List<Integer> positionsOfLinesWithString;
    private List<Integer> prefixPositions;
}
