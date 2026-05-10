package com.zmpc.common;

public class WPrint {

    /**
     * Makes {@code System.out.println(...)} easier
     * @param objs The {@code Object(s)} to be printed.
     */
    public static void w(Object... objs) {
        for (Object obj : objs) {
            System.out.println(obj);
        }
    }

    /**
     * The same as {@code System.out.println()}
     */
    public static void w() {
        System.out.println();
    }

    /**
     * Makes {@code System.out.println(...)} + additional empty line
     * @param objs The {@code Object(s)} to be printed.
     */
    public static void wn(Object... objs) {
        w(objs);
        w();
    }

    public static void nw(Object... objs) {
        w();
        w(objs);
    }

    public static void wr(Object... objs) {
        for (Object obj : objs) {
            System.out.println(obj);
        }
    }

    public static void wf(String format, Object... args) {
        System.out.printf(format, args);
        System.out.println();
    }
}
