package com.zmpc.util;

public class ObjectUtils {

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Object must not be null.");
        }
    }
}
