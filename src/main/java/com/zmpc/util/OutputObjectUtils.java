package com.zmpc.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.zmpc.util.StringUtils.capitalizeFirstLetter;

public final class OutputObjectUtils {

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
            if (!(method.getName().startsWith("get") || method.getName().startsWith("is"))) continue;

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
                keyMethodName = "is" + capitalizeFirstLetter(field.getName());
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
        //System.out.println(obj.getClass().getCanonicalName());

        StringBuilder sb = new StringBuilder(obj.getClass().getSimpleName());
        sb.append(" {");

        try {
            Map<String, Method> fieldsAndMethodsMap = getMapOfFieldAndGetMethod(obj.getClass());
            for (Map.Entry<String, Method> entry : fieldsAndMethodsMap.entrySet()) {

                sb.append("\n - ")
                        .append(entry.getKey())
                        .append(": ");

                Object fieldValue = entry.getValue().invoke(obj);

                Class returnMethodType = entry.getValue().getReturnType();
                if (fieldValue == null) {
                    sb.append(fieldValue);
                } else if (String.class.equals(returnMethodType)) {
                    sb.append("\"").append(fieldValue).append("\"");
                } else if (LocalDateTime.class.equals(returnMethodType)) {
                    LocalDateTime localDateTime = (LocalDateTime) fieldValue;
                    sb.append(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    sb.append(fieldValue);
                }

                //System.out.println(" * " + entry.getKey() + " : " + entry.getValue().getName());
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        sb.append("\n}");

        return sb.toString();
    }
}

