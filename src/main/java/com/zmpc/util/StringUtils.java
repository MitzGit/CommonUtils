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
}
