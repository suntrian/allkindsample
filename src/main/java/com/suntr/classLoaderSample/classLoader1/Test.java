package com.suntr.classLoaderSample.classLoader1;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        String cypheredClassPath;
        if (args!=null && args.length>0){
            cypheredClassPath = args[0];
        } else {
            cypheredClassPath = "C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com" +
                    "\\suntr\\classLoaderSample\\classLoader1";
        }


        Class clazz = new MyClassLoader(cypheredClassPath).findClass("ClassLoaderTest");
        //Class clazz = new MyClassLoader("G:\\WorkSpace\\exam\\src\\Text").loadClass("cClassLoaderText");
        // 使用loadClass和findClass略有不同
        Date d1 = (Date) clazz.getDeclaredConstructor().newInstance();
        System.out.println(d1);
    }

}
