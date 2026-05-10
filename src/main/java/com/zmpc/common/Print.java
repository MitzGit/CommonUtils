package com.zmpc.common;

public class Print {

    public static void println(Object obj) {
        System.out.println(obj == null ? null : obj.toString());
    }

    public static void println() {
        System.out.println();
    }

    public static void print(Object obj) {
        System.out.println(obj == null ? null : obj.toString());
    }

    public static void print() {
        System.out.println();
    }

    public static void w(Object... objs) {
        if (objs.length == 0) {
            System.out.println();
        }
        for (Object obj : objs) {
            System.out.println(obj);
        }
    }
}
