package com.suntr.classLoaderSample.classLoader2;

public class ClassLoaderTest {

    public void print()
    {
        System.out.println("自定义类加载器要加载类的代码,我在被要先被加密，刚刚才解密");
    }

    public static void main(String[] args) {


        System.out.println(0xff);

    }
}
