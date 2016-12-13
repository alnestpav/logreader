package ru.siblion.nesterov.logreader.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by alexander on 06.12.2016.
 */
public class Utils {

    public static List<String> getFilesMatching(File root, String regExp) {
        if (!root.isDirectory()) {
            throw new IllegalArgumentException(root + " is no directory.");
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

}