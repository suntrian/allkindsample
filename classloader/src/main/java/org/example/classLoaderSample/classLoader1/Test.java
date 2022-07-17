package org.example.classLoaderSample.classLoader1;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Date;

public class Test {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException, URISyntaxException {
        String cypheredClassPath;
        if (args!=null && args.length>0){
            cypheredClassPath = args[0];
        } else {
            cypheredClassPath = Test.class.getResource("\\com\\suntr\\classLoaderSample\\classLoader1").toURI().getPath();
        }


        Class clazz = new MyClassLoader(cypheredClassPath).findClass("ClassLoaderTest");
        //Class clazz = new MyClassLoader("G:\\WorkSpace\\exam\\src\\Text").loadClass("cClassLoaderText");
        // 使用loadClass和findClass略有不同
        Date d1 = (Date) clazz.getDeclaredConstructor().newInstance();
        System.out.println(d1);
    }

}
