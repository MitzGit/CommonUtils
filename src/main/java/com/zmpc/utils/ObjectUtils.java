package com.zmpc.utils;

public class ObjectUtils {

    public static void notNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object must not be null.");
        }
    }
}
