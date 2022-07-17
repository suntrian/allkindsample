package org.example.classLoaderSample.classLoader2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class clazz = new MyClassLoader("C:\\Users\\suntri\\Projects\\allkinksample\\out\\production\\classes\\com\\suntr\\classLoaderSample\\classLoader2").loadClass("ClassLoaderTest_1");
        Method method = clazz.getMethod("print");
        method.invoke(clazz.getDeclaredConstructor().newInstance());
        System.out.println("解密调用成功！");
    }
}
