package com.suntr.util;

import java.util.Collection;

public class PrintUtil {

    public static <T extends Collection> void print(T data) {
        data.forEach((Object t) -> System.out.println(t+","));
    }

    public static <T> void print(T data) {
        System.out.println(data);
    }

}
