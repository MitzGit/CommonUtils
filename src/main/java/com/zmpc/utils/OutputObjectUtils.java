package com.zmpc.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class OutputObjectUtils {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Map<Class, Map<String, Method>> cacheClassFieldsToMethodsMap = new HashMap<>();

    private static Map<String, Method> getMapOfFieldAndGetMethod(Class clazz) {
        // return from Cache
        if (cacheClassFieldsToMethodsMap.containsKey(clazz)) {
            //System.out.println(">> get ClassFieldsToMethodsMap from Cache");
            return cacheClassFieldsToMethodsMap.get(clazz);
        }

        Map<String, Method> declaredMethodNamesMap = new HashMap<>();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getParameterCount() > 0) continue;
            //if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) continue;
            if (!Modifier.isPublic(method.getModifiers())) continue;

            method.setAccessible(true);
            declaredMethodNamesMap.put(method.getName(), method);

            //System.out.println(" * " + method.getName() + " : " + method.getReturnType().getSimpleName());
        }

        Map<String, Method> mapFieldAndGetMethod = new LinkedHashMap<>();

        //System.out.println("Declared Fields :");
        String keyMethodName;
        Method keyMethod;
        for (Field field : clazz.getDeclaredFields()) {
            //System.out.println(" * " + field.getName());

            keyMethodName = "get" + capitalizeFirstLetter(field.getName());
            if (declaredMethodNamesMap.containsKey(keyMethodName)) {
                keyMethod = declaredMethodNamesMap.get(keyMethodName);
                if (keyMethod.getReturnType().equals(field.getType())) {
                    mapFieldAndGetMethod.put(field.getName(), keyMethod);
                }
            } else {
                //keyMethodName = "is" + capitalizeFirstLetter(field.getName());
                keyMethodName = field.getName();
                if (declaredMethodNamesMap.containsKey(keyMethodName)) {
                    keyMethod = declaredMethodNamesMap.get(keyMethodName);
                    if (keyMethod.getReturnType().equals(field.getType())) {
                        mapFieldAndGetMethod.put(field.getName(), keyMethod);
                    }
                }
            }
        }

        cacheClassFieldsToMethodsMap.put(clazz, mapFieldAndGetMethod);

        return mapFieldAndGetMethod;
    }

    public static String toString(Object obj) {
        return toString(obj, false);
    }

    public static String toString(Object obj, boolean isShortView) {
        if (obj instanceof CharSequence) {
            return "\"" + obj + "\"";
        } else if (obj instanceof Integer
                || obj instanceof Long
                || obj instanceof Double) {
            return "" + obj;
        }

        StringBuilder sb = new StringBuilder(obj.getClass().getSimpleName());
        sb.append(" {");

        try {
            Map<String, Method> fieldsAndMethodsMap = getMapOfFieldAndGetMethod(obj.getClass());
            boolean isFirstAttr = true;
            for (Map.Entry<String, Method> entry : fieldsAndMethodsMap.entrySet()) {

                if (isShortView) {
                    if (isFirstAttr) {
                        isFirstAttr = false;
                    } else {
                        sb.append(", ");
                    }
                } else {
                    sb.append("\n - ")
                            .append(entry.getKey())
                            .append(": ");
                }

                Object fieldValue = entry.getValue().invoke(obj);

                Class returnMethodType = entry.getValue().getReturnType();
                if (fieldValue == null) {
                    sb.append("null");
                } else if (String.class.equals(returnMethodType)) {
                    sb.append("\"").append(fieldValue).append("\"");
                } else if (LocalDateTime.class.equals(returnMethodType)) {
                    LocalDateTime localDateTime = (LocalDateTime) fieldValue;
                    sb.append(localDateTime.format(isShortView ? DATE_TIME_FORMATTER_SHORT : DATE_TIME_FORMATTER));
                } else {
                    sb.append(fieldValue);
                }

                //System.out.println(" * " + entry.getKey() + " : " + entry.getValue().getName());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        sb.append(isShortView ? "}" : "\n}");

        return sb.toString();
    }

    public static void printObject(Object obj) {
        System.out.println(toString(obj));
    }

    public static void printObjectShort(Object obj) {
        System.out.println(toString(obj, true));
    }

    public static void printObject(List objList) {
        System.out.println(toString(objList));
    }

    public static String toString(List objList) {
        //return outputListAsTable(objList);
        return outputListShortly(objList);
    }

    public static String toString(Object[] objArray) {
        return outputArrayShortly(objArray);
    }

    public static String toString(int[] arr) {
        return outputArrayShortly(Arrays.stream(arr).boxed().toArray());
    }

    public static String toString(long[] arr) {
        return outputArrayShortly(Arrays.stream(arr).boxed().toArray());
    }

    public static String toString(double[] arr) {
        return outputArrayShortly(Arrays.stream(arr).boxed().toArray());
    }

    public static <T> String outputListAsTable(List<T> list) {
        return outputListAsTable(list, true, "|");
    }

    public static <T> String outputListAsTable(List<T> list, boolean isShowHeaders, String splitStr) {
        if (list == null) return "null";
        if (list.isEmpty()) return "{ -- empty List -- }";

        Class<?> clazz = list.get(0).getClass();
        Map<String, Method> mapOfFieldAndGetMethod = getMapOfFieldAndGetMethod(clazz);
        //System.out.println(mapOfFieldAndGetMethod.keySet());

        String[] headers = mapOfFieldAndGetMethod.keySet().toArray(new String[0]);
        //System.out.println(Arrays.toString(headers));

        int tableLength = headers.length;
        int[] maxColumnLengthArray = Arrays.stream(headers)
                .map(String::length).mapToInt(Integer::intValue).toArray();

        //System.out.println(Arrays.toString(maxColumnLengthArray));

        List<String[]> valuesList = new ArrayList<>();
        String[] elemValues;
        String value;
        // Fill maxColumnLengthArray with Max Values
        for (T obj : list) {
            elemValues = new String[tableLength];
            valuesList.add(elemValues);
            for (int i = 0; i < tableLength; i++) {
                try {
                    value = getObjectValue(mapOfFieldAndGetMethod.get(headers[i]).invoke(obj));
                    elemValues[i] = value;

                    //System.out.println(value);
                    if (maxColumnLengthArray[i] < value.length()) {
                        maxColumnLengthArray[i] = value.length();
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        StringBuilder sb = new StringBuilder();

        // Create Output Format String
        String outputFormat = ""; // "| %-20s | %-3s | %-8s | %-8s |%n";
        for (int len : maxColumnLengthArray) {
            outputFormat += splitStr + " %-" + len + "s ";
        }
        outputFormat += splitStr + "%n";

        String horizontalLine = "";
        if (headers != null && isShowHeaders) {
            sb.append(String.format(outputFormat, headers));

            int lineLength = Arrays.stream(maxColumnLengthArray).map(x -> x + 3).sum() + 1;
            horizontalLine = "-".repeat(lineLength);
            sb.append(horizontalLine).append('\n');
        }

        // Output Table Values
        for (String[] elemValuesArr : valuesList) {
            sb.append(String.format(outputFormat, elemValuesArr));
        }
        //sb.append(horizontalLine);

        return sb.toString();
    }

    public static <T> void printListAsTable(List<T> list) {
        System.out.println(outputListAsTable(list));
    }

    public static <T> void printListAsTableSimple(List<T> list) {
        System.out.println(outputListAsTable(list, false, ""));
    }

    public static <T> void printListAsTable(List<T> list, boolean isShowHeaders) {
        System.out.println(outputListAsTable(list, isShowHeaders, "|"));
    }

    public static <T> void printListAsTable(List<T> list, boolean isShowHeaders, String splitStr) {
        System.out.println(outputListAsTable(list, isShowHeaders, splitStr));
    }

    public static <T> String outputListShortly(List<T> list) {
        if (list == null) return "null";
        if (list.isEmpty()) return "{ -- empty List -- }";

        StringBuilder sb = new StringBuilder();
        for (T item : list) {
            if (!sb.isEmpty()) {
                sb.append("\n");
            }
            sb.append(toString(item, true));
        }
        return sb.toString();
    }

    public static <T> String outputArrayShortly(T[] arr) {
        if (arr == null) return "null";
        if (arr.length == 0) return "[]";

        StringBuilder sb = new StringBuilder();
        for (T item : arr) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(toString(item, true));
        }
        return "[" + sb + "]";
    }

    private static String getObjectValue(Object obj) {
        if (obj == null) return "null";

        if (String.class.equals(obj.getClass())) {
            return "\"" + obj + "\"";
        } else if (LocalDateTime.class.equals(obj.getClass())) {
            return ((LocalDateTime) obj).format(DATE_TIME_FORMATTER);
        }

        return String.valueOf(obj);
    }

    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        String firstLetter = str.substring(0, 1).toUpperCase();
        String restPart = str.substring(1);
        return firstLetter + restPart;
    }
}