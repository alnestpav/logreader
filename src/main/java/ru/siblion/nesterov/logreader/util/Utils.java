package ru.siblion.nesterov.logreader.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by alexander on 06.12.2016.
 */

/* Класс, предоставляющий вспомогательные методы для других классов */
public class Utils {

    private static final Logger logger = AppLogger.getLogger();

    /* Метод раньше был в классе FileFinder, но перенес в класс Utils, так как он также используется еще и в классе Request */
    public static List<String> getFilesMatching(File root, String regExp) {
        try {
            if (!root.isDirectory()) {
                throw new IllegalArgumentException(root + " это не директория.");
            }
        } catch(IllegalArgumentException e) {
            logger.log(Level.SEVERE, "Ошибка при поиске файлов в некоторой папке", e) ;
        }
        final Pattern p = Pattern.compile(regExp);

        File[] files = root.listFiles(file -> p.matcher(file.getName()).matches());

        List<String> filesMatching = new ArrayList<>();
        for (File file : files) {
            filesMatching.add(file.toString());
        }
        return filesMatching;
    }

}