package com.zmpc.utils;

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

    record Pair(int startIndex, int endIndex, boolean isFound) {
    }

    private static Pair findPairCharIndexForward(String text, char startChar, char endChar) {
        int count = 0;
        char ch;

        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < text.length(); i++) {
            ch = text.charAt(i);
            if (ch == startChar) {           // '{'
                if (count == 0) {
                    startIndex = i;
                }
                count++;
            } else if (ch == endChar && count > 0) {  // '}'
                count--;
                if (count == 0) {
                    endIndex = i;
                    return new Pair(startIndex, endIndex, true);
                }
            }
        }

        return new Pair(startIndex, endIndex, false);
    }

    private static Pair findPairCharIndexBackward(String text, char startChar, char endChar) {
        int count = 0;
        char ch;

        int startIndex = -1;
        int endIndex = -1;

        for (int i = text.length() - 1; i >= 0; i--) {
            ch = text.charAt(i);
            if (ch == endChar) {           // ')'
                if (count == 0) {
                    endIndex = i;
                }
                count++;
            } else if (ch == startChar && count > 0) {  // '('
                count--;
                if (count == 0) {
                    startIndex = i;
                    return new Pair(startIndex, endIndex, true);
                }
            }
        }

        return new Pair(startIndex, endIndex, false);
    }
}
