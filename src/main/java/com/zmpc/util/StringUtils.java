package com.zmpc.util;

public class StringUtils {

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String firstLetter = str.substring(0, 1).toUpperCase();
        String restPart = str.substring(1);
        return firstLetter + restPart;
    }

    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.isEmpty());
    }

    public static String emptyIfNull(String str) {
        return str == null ? "" : str;
    }

    public static String[] splitByIndex(String str, int index) {
        if (isNullOrEmpty(str)) {
            String[] emptyParts = {"", ""};
            return emptyParts;
        }
        if (index >= 0 && index < str.length()) {
            String[] parts = new String[2];
            parts[0] = str.substring(0, index);
            parts[1] = str.substring(index);
            return parts;
        }

        return new String[]{str};
    }
}
