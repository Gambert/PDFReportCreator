package com.shivsoftech.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;

import static com.shivsoftech.util.Constants.EMPTY_STRING;

public class Utility {

    private static Logger LOG = LoggerFactory.getLogger(Utility.class);

    public static boolean isEmpty(Collection collection) {

        return  (!(collection != null && !collection.isEmpty()));
    }

    public static boolean isEmpty(String text) {

        if (text == null) {
            return true;
        }

        return (EMPTY_STRING.equalsIgnoreCase(text.trim()));
    }

    private static boolean isExist(File file) {

        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    public static boolean isFileExist(String filename) {

        if (Utility.isEmpty(filename)) {
            return false;
        }

        File file = new File(filename);
        boolean isExist = isExist(file);
        boolean isFile = file.isFile() && !file.isDirectory();
        file = null;

        return (isExist && isFile);
    }

    public static boolean createDirectory(String filename) {

        if (Utility.isEmpty(filename)) {
            return false;
        }

        File file = new File(filename);
        if (isDirectoryExist(file.getParent())) {
            return false;
        } else {
            return file.getParentFile().mkdirs();
        }
    }

    public static boolean isDirectoryExist(String filename) {

        if (Utility.isEmpty(filename)) {
            return false;
        }

        File file = new File(filename);
        boolean isExist = isExist(file);
        boolean isDirectory = file.isDirectory() && !file.isFile();
        file = null;

        return (isExist && isDirectory);
    }

    public static String getRandomNumber() {
        long num16Digits = (long) (Math.random() * 1_000_000_000_000_000L);
        return Long.toString(num16Digits);
    }

    public static String generateTemparoryFilename() {

        return  System.getProperty("java.io.tmpdir") + File.separator + getRandomNumber();
    }

    public static boolean deleteFile(String filename) {
        File file = new File(filename);
        if (isExist(file)) {
            return file.delete();
        }
        return false;
    }
}
