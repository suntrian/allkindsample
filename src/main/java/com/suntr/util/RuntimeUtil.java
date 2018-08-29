package com.suntr.util;

public class RuntimeUtil {

    public static long totalMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long freeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

}
